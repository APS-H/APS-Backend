package apsh.backend.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apsh.backend.dto.ManpowerDto;
import apsh.backend.dto.DeviceDto;
import apsh.backend.dto.OrderDto;
import apsh.backend.dto.OrderProductionDto;
import apsh.backend.dto.SuborderProductionDto;
import apsh.backend.po.OrderProduction;
import apsh.backend.po.SuborderProduction;
import apsh.backend.repository.OrderProductionRepository;
import apsh.backend.service.ScheduleService;
import apsh.backend.serviceimpl.scheduleservice.Device;
import apsh.backend.serviceimpl.scheduleservice.Manpower;
import apsh.backend.serviceimpl.scheduleservice.Order;
import apsh.backend.serviceimpl.scheduleservice.Suborder;
import apsh.backend.serviceimpl.scheduleservice.SuborderSolution;
import apsh.backend.serviceimpl.scheduleservice.TimeGrain;
import apsh.backend.serviceimpl.scheduleservice.TimeSection;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    static final Long millisecondCountPerHour = 60L * 60L * 1000L;

    // 划分间隔 子订单的最长持续时间 单位为小时 最好是12的因数
    static final int maxSuborderNeedTimeInHour = 3;

    @Autowired
    private OrderProductionRepository orderProductionRepository;

    @Autowired
    private SolverManager<SuborderSolution, UUID> solverManager;

    // 当前的任务
    private SolverJob<SuborderSolution, UUID> currentSolverJob = null;

    // 排程结果
    private List<OrderProductionDto> solutionDto;

    // 是否添加过排程任务 排程可能正在进行或排程结束 包括数据库中读取的结果
    private boolean stateJobSubmitted = false;
    // 当前的排程任务是否持久化
    private boolean stateSolutionSaved = false;

    @PostConstruct
    private void init() {
        // 载入排程结果
        solutionDto = new ArrayList<>();
        List<OrderProduction> orderProductionPos = orderProductionRepository.findAll();
        if (orderProductionPos.size() == 0)
            return;
        for (OrderProduction orderProductionPo : orderProductionPos) {
            List<SuborderProductionDto> suborderProductionDtos = new ArrayList<>(
                    orderProductionPo.getSuborderProductions().size());
            for (SuborderProduction po : orderProductionPo.getSuborderProductions())
                suborderProductionDtos.add(new SuborderProductionDto(po.getSuborderId(), Date.from(po.getStartTime()),
                        Date.from(po.getEndTime()), new ArrayList<>(po.getManpowerIds()), po.getDeviceId()));
            OrderProductionDto dto = new OrderProductionDto(orderProductionPo.getOrderId(), suborderProductionDtos);
            solutionDto.add(dto);
        }
        stateJobSubmitted = true;
        stateSolutionSaved = true;
    }

    @Override
    public void arrangeInitialOrders(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos,
            List<OrderDto> orderDtos, Date startTime) {
        // 初始化状态
        List<Manpower> manpowers = manpowerDtos.stream()
                .map(manpowerDto -> new Manpower(manpowerDto.getId(), manpowerDto.getPeopleCount(),
                        manpowerDto.getWorkSections().stream().map(TimeSection::fromDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
        List<Device> devices = deviceDtos.stream().map(Device::fromDto).collect(Collectors.toList());
        List<Order> orders = orderDtos.stream().map(Order::fromDto).collect(Collectors.toList());
        currentSolverJob = null;
        solutionDto = null;
        stateJobSubmitted = true;
        stateSolutionSaved = false;

        List<TimeGrain> timeGrains = generateTimeGrains(orders, startTime, startTime);

        List<Suborder> suborders = splitOrders(orders, startTime, false);

        // 调用排程库
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        SuborderSolution solution = new SuborderSolution(startTimeCalendar.get(Calendar.HOUR_OF_DAY), new ArrayList<>(),
                manpowers, devices, timeGrains, suborders, null);
        UUID problemId = UUID.randomUUID();
        currentSolverJob = solverManager.solve(problemId, solution);
    }

    @Override
    public void arrangeUrgentOrder(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos, List<OrderDto> orderDtos,
            OrderDto urgentOrderDto, Date insertTime, Date startTime) {
        if (!stateJobSubmitted)
            throw new RuntimeException("还没有排程");
        // 如果结果没有保存说明排程可能正在运行
        if (!stateSolutionSaved) {
            if (currentSolverJob.getSolverStatus() != SolverStatus.NOT_SOLVING)
                throw new RuntimeException("请等待上一次排程结束");
            // 确保能读取上一次排程结果
            tryGetCurrentArrangement();
        }
        // 初始化状态
        List<Manpower> manpowers = manpowerDtos.stream()
                .map(manpowerDto -> new Manpower(manpowerDto.getId(), manpowerDto.getPeopleCount(),
                        manpowerDto.getWorkSections().stream().map(TimeSection::fromDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
        List<Device> devices = deviceDtos.stream().map(Device::fromDto).collect(Collectors.toList());
        List<Order> orders = orderDtos.stream().map(Order::fromDto).collect(Collectors.toList());
        Order urgentOrder = Order.fromDto(urgentOrderDto);
        orders.add(urgentOrder);
        stateSolutionSaved = false;

        // 根据插单时间划分之前排程结果的子订单
        HashMap<String, Order> orderMap = new HashMap<>();
        for (Order order : orders)
            orderMap.put(order.getId(), order);
        HashMap<String, Manpower> manpowerMap = new HashMap<>();
        for (Manpower manpower : manpowers)
            manpowerMap.put(manpower.getId(), manpower);
        HashMap<String, Device> deviceMap = new HashMap<>();
        for (Device device : devices)
            deviceMap.put(device.getId(), device);
        List<Suborder> urgentSuborder = splitOrders(Arrays.asList(urgentOrder), insertTime, true);
        List<Suborder> fixedSuborders = new ArrayList<>();
        List<Suborder> dirtySuborders = new ArrayList<>();
        for (OrderProductionDto orderProductionDto : solutionDto)
            for (SuborderProductionDto dto : orderProductionDto.getSuborders()) {
                String orderId = orderProductionDto.getId();
                Order order = orderMap.get(orderId);
                Suborder suborder = new Suborder();
                suborder.setId(dto.getId());
                suborder.setOrderId(orderId);
                suborder.setUrgent(order.getUrgent());
                suborder.setNeedTimeInHour(
                        (int) ((dto.getEndTime().getTime() - dto.getStartTime().getTime()) / millisecondCountPerHour));
                suborder.setNeedPeopleCount(order.getNeedPeopleCount());
                suborder.setAvailableManpowerIdSet(order.getAvailableManpowerIdSet());
                suborder.setAvailableDeviceTypeIdSet(order.getAvailableDeviceTypeIdSet());
                int ddlTimeGrainIndex = (int) ((order.getDeadline().getTime() - startTime.getTime())
                        / millisecondCountPerHour);
                suborder.setDeadlineTimeGrainIndex(ddlTimeGrainIndex);
                if (dto.getStartTime().after(insertTime))
                    // 需要重新排程
                    dirtySuborders.add(suborder);
                else {
                    // 使用之前的结果
                    suborder.setManpowerA(manpowerMap.get(dto.getManpowerIds().get(0)));
                    if (dto.getManpowerIds().size() > 1)
                        suborder.setManpowerB(manpowerMap.get(dto.getManpowerIds().get(1)));
                    if (dto.getManpowerIds().size() > 2)
                        suborder.setManpowerC(manpowerMap.get(dto.getManpowerIds().get(2)));
                    suborder.setDevice(deviceMap.get(dto.getDeviceId()));
                    fixedSuborders.add(suborder);
                }
            }

        List<TimeGrain> timeGrains = generateTimeGrains(orders, startTime, insertTime);

        // 重新排程
        dirtySuborders.addAll(urgentSuborder);
        // 调用排程库
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        SuborderSolution solution = new SuborderSolution(startTimeCalendar.get(Calendar.HOUR_OF_DAY), new ArrayList<>(),
                manpowers, devices, timeGrains, dirtySuborders, null);
        UUID problemId = UUID.randomUUID();
        currentSolverJob = solverManager.solve(problemId, solution);
    }

    @Override
    public void removeCurrentArrangement() {
        stateJobSubmitted = false;
        stateSolutionSaved = false;
        deleteInputAndSolution();
    }

    @Override
    public List<OrderProductionDto> tryGetCurrentArrangement() {
        if (!stateJobSubmitted)
            throw new RuntimeException("还没有输入排程任务");
        // 已经有结果
        if (stateSolutionSaved)
            return solutionDto;
        // 已经搜索完成但是没有保存
        if (currentSolverJob.getSolverStatus() == SolverStatus.NOT_SOLVING) {
            SuborderSolution solution = null;
            try {
                solution = currentSolverJob.getFinalBestSolution();
            } catch (Exception e) {
                throw new RuntimeException("排程失败 原因未知");
            }
            solutionDto = getSolutionDto(solution);
            saveInputAndSolution();
            stateSolutionSaved = true;
            return solutionDto;
        }
        // 还没有搜索完成
        return null;
    }

    @Override
    public List<OrderProductionDto> getCurrentArrangment() {
        if (!stateJobSubmitted)
            throw new RuntimeException("还没有输入排程任务");
        // 已经有结果
        if (stateSolutionSaved)
            return solutionDto;
        // 阻塞
        SuborderSolution solution = null;
        try {
            solution = currentSolverJob.getFinalBestSolution();
        } catch (Exception e) {
            throw new RuntimeException("排程失败 原因未知");
        }
        solutionDto = getSolutionDto(solution);
        System.out.println(solution.getScore());
        saveInputAndSolution();
        stateSolutionSaved = true;
        return solutionDto;
    }

    /**
     * 生成所有的时间粒度
     */
    private List<TimeGrain> generateTimeGrains(List<Order> orders, Date startTime, Date availableStartTime) {
        int totalNeedHours = 0;
        for (Order order : orders)
            totalNeedHours += order.getNeedTimeInHour();
        // 延迟系数
        float factor = 0.35f;
        int availableTimeInHour = (int) (totalNeedHours / maxSuborderNeedTimeInHour * factor) + 5;
        List<TimeGrain> timeGrains = new ArrayList<>(availableTimeInHour);
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        int startHourOfDay = startTimeCalendar.get(Calendar.HOUR_OF_DAY);
        for (int i = 0; i < availableTimeInHour; i++) {
            Date date = new Date(startTime.getTime() + i * maxSuborderNeedTimeInHour * millisecondCountPerHour);
            timeGrains.add(new TimeGrain(i, date, startHourOfDay));
            startHourOfDay = (startHourOfDay + maxSuborderNeedTimeInHour) % 24;
        }
        return timeGrains;
    }

    private List<Suborder> splitOrders(List<Order> orders, Date startTime, boolean urgent) {
        // 划分子订单
        List<Suborder> suborders = new ArrayList<>(orders.size());
        for (Order order : orders) {
            int suborderIndex = 0;
            int ddlTimeGrainIndex = (int) ((order.getDeadline().getTime() - startTime.getTime())
                    / millisecondCountPerHour);
            int remainTimeInHour = order.getNeedTimeInHour();
            while (remainTimeInHour > 0) {
                suborders.add(Suborder.create(order, suborderIndex++, urgent,
                        Math.min(maxSuborderNeedTimeInHour, remainTimeInHour), ddlTimeGrainIndex));
                remainTimeInHour -= maxSuborderNeedTimeInHour;
            }
        }
        return suborders;
    }

    // 根据Solution获取Dto
    private List<OrderProductionDto> getSolutionDto(SuborderSolution solution) {
        HashSet<String> orderIdSet = new HashSet<>();
        for (Suborder suborder : solution.getSuborders())
            orderIdSet.add(suborder.getOrderId());
        List<OrderProductionDto> res = new ArrayList<>();
        HashMap<String, OrderProductionDto> orderProductionDtoMap = new HashMap<>();
        for (String orderId : orderIdSet) {
            OrderProductionDto dto = new OrderProductionDto(orderId, new ArrayList<>());
            res.add(dto);
            orderProductionDtoMap.put(orderId, dto);
        }

        for (Suborder suborder : solution.getSuborders()) {
            System.out.println("suborder: " + suborder);
            OrderProductionDto dto = orderProductionDtoMap.get(suborder.getOrderId());
            Date startTime = suborder.getTimeGrain().getTime();
            Date endTime = new Date(startTime.getTime() + suborder.getNeedTimeInHour() * millisecondCountPerHour);
            SuborderProductionDto suborderDto = new SuborderProductionDto(suborder.getId(), startTime, endTime,
                    suborder.getManpowerIds(), suborder.getDevice().getId());
            dto.getSuborders().add(suborderDto);
        }
        for (Suborder suborder : solution.getFixedSuborders()) {
            OrderProductionDto dto = orderProductionDtoMap.get(suborder.getOrderId());
            Date startTime = suborder.getTimeGrain().getTime();
            Date endTime = new Date(startTime.getTime() + suborder.getNeedTimeInHour() * millisecondCountPerHour);
            SuborderProductionDto suborderDto = new SuborderProductionDto(suborder.getId(), startTime, endTime,
                    suborder.getManpowerIds(), suborder.getDevice().getId());
            dto.getSuborders().add(suborderDto);
        }
        return res;
    }

    /**
     * 把当前输入和排程结果保存到数据库中
     */
    private void saveInputAndSolution() {
        // TODO: 保存输入
        List<OrderProduction> orderProductionPos = new ArrayList<>(solutionDto.size());
        for (OrderProductionDto orderProductionDto : solutionDto) {
            Set<SuborderProduction> suborderProductionPos = new HashSet<>(orderProductionDto.getSuborders().size());
            for (SuborderProductionDto dto : orderProductionDto.getSuborders())
                suborderProductionPos.add(new SuborderProduction(null, dto.getId(), dto.getStartTime().toInstant(),
                        dto.getEndTime().toInstant(), dto.getManpowerIds(), dto.getDeviceId()));
            orderProductionPos.add(new OrderProduction(null, orderProductionDto.getId(), suborderProductionPos));
        }
        orderProductionRepository.deleteAll();
        orderProductionRepository.saveAll(orderProductionPos);
    }

    private void deleteInputAndSolution() {
        // TODO: 删除输入
        orderProductionRepository.deleteAll();
    }
}