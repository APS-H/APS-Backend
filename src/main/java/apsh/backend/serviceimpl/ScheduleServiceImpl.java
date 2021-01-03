package apsh.backend.serviceimpl;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import apsh.backend.po.Craft;
import apsh.backend.service.LegacySystemService;
import apsh.backend.vo.*;
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
import apsh.backend.serviceimpl.scheduleservice.ManpowerCombination;
import apsh.backend.serviceimpl.scheduleservice.Order;
import apsh.backend.serviceimpl.scheduleservice.Suborder;
import apsh.backend.serviceimpl.scheduleservice.SuborderSolution;
import apsh.backend.serviceimpl.scheduleservice.TimeGrain;
import apsh.backend.serviceimpl.scheduleservice.TimeSection;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    static final Long millisecondCountPerHour = 60L * 60L * 1000L;

    // 划分间隔 子订单的最长持续时间 单位为小时 最好是12的因数
    static final int maxSuborderNeedTimeInHour = 6;

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

    private final LegacySystemService legacySystemService;

    @Autowired
    public ScheduleServiceImpl(LegacySystemServiceImpl legacySystemService) {
        this.legacySystemService = legacySystemService;
    }

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
                suborderProductionDtos.add(new SuborderProductionDto(po.getSuborderId(),
                        po.getStartTime() == null ? null : new Date(po.getStartTime().getTime()),
                        po.getEndTime() == null ? null : new Date(po.getEndTime().getTime()),
                        new ArrayList<>(po.getManpowerIds()), po.getDeviceId()));
            OrderProductionDto dto = new OrderProductionDto(orderProductionPo.getOrderId(), suborderProductionDtos);
            solutionDto.add(dto);
        }
        stateJobSubmitted = true;
        stateSolutionSaved = true;
    }

    @Override
    public void arrangeInitialOrders(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos,
            List<OrderDto> orderDtos, Date startTime, double denseFactor) {
        // 初始化状态
        List<Manpower> manpowers = manpowerDtos.stream().map(manpowerDto -> new Manpower(manpowerDto.getId(),
                manpowerDto.getPeopleCount(), TimeSection.fromDto(manpowerDto.getWorkSection())))
                .collect(Collectors.toList());
        List<ManpowerCombination> combinations = new ArrayList<>();
        for (int i = 0; i < manpowers.size(); i++) {
            combinations.add(new ManpowerCombination(manpowers.get(i), null));
            for (int j = i + 1; j < manpowers.size(); j++) {
                if (manpowers.get(i).canWorkWith(manpowers.get(j)))
                    combinations.add(new ManpowerCombination(manpowers.get(i), manpowers.get(j)));
            }
        }

        List<Device> devices = deviceDtos.stream().map(Device::fromDto).collect(Collectors.toList());
        List<Order> orders = orderDtos.stream().map(Order::fromDto).collect(Collectors.toList());
        currentSolverJob = null;
        solutionDto = null;
        stateJobSubmitted = true;
        stateSolutionSaved = false;

        List<TimeGrain> timeGrains = generateTimeGrains(orders, startTime, startTime, denseFactor);

        List<Suborder> suborders = splitOrders(orders, startTime, false);

        // 调用排程库
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        SuborderSolution solution = new SuborderSolution(startTimeCalendar.get(Calendar.HOUR_OF_DAY), new ArrayList<>(),
                combinations, devices, timeGrains, suborders, null);
        UUID problemId = UUID.randomUUID();
        currentSolverJob = solverManager.solve(problemId, solution);
    }

    @Override
    public void arrangeUrgentOrder(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos, List<OrderDto> orderDtos,
            OrderDto urgentOrderDto, Date insertTime, Date startTime, double denseFactor) {
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
        List<Manpower> manpowers = manpowerDtos.stream().map(manpowerDto -> new Manpower(manpowerDto.getId(),
                manpowerDto.getPeopleCount(), TimeSection.fromDto(manpowerDto.getWorkSection())))
                .collect(Collectors.toList());
        List<ManpowerCombination> combinations = new ArrayList<>();
        for (int i = 0; i < manpowers.size(); i++) {
            combinations.add(new ManpowerCombination(manpowers.get(i), null));
            for (int j = i + 1; j < manpowers.size(); j++) {
                if (manpowers.get(i).canWorkWith(manpowers.get(j)))
                    combinations.add(new ManpowerCombination(manpowers.get(i), manpowers.get(j)));
            }
        }
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
                        / millisecondCountPerHour / maxSuborderNeedTimeInHour);
                suborder.setDeadlineTimeGrainIndex(ddlTimeGrainIndex);
                if (dto.getStartTime().after(insertTime))
                    // 需要重新排程
                    dirtySuborders.add(suborder);
                else {
                    // 使用之前的结果
                    Manpower a = manpowerMap.get(dto.getManpowerIds().get(0));
                    Manpower b = manpowerMap.get(dto.getManpowerIds().get(1));
                    suborder.setManpowerCombination(new ManpowerCombination(a, b));
                    suborder.setDevice(deviceMap.get(dto.getDeviceId()));
                    fixedSuborders.add(suborder);
                }
            }

        List<TimeGrain> timeGrains = generateTimeGrains(orders, startTime, insertTime, denseFactor);

        // 重新排程
        dirtySuborders.addAll(urgentSuborder);
        // 调用排程库
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        SuborderSolution solution = new SuborderSolution(startTimeCalendar.get(Calendar.HOUR_OF_DAY), new ArrayList<>(),
                combinations, devices, timeGrains, dirtySuborders, null);
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
                e.printStackTrace();
                throw new RuntimeException("排程失败 原因未知");
            }
            System.out.println(solution.getScore());
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
            e.printStackTrace();
            throw new RuntimeException("排程失败 原因未知");
        }
        System.out.println(solution.getScore());
        solutionDto = getSolutionDto(solution);
        saveInputAndSolution();
        stateSolutionSaved = true;
        return solutionDto;
    }

    @Override
    public List<SchedulePlanTableOrderVo> getPlanTable() {
        Map<String, Craft> craftMap = legacySystemService.getAllCrafts().parallelStream()
                .collect(Collectors.toMap(Craft::getProductionId, craft -> craft));
        Map<Integer, apsh.backend.po.Order> orderMap = legacySystemService.getAllOrders().parallelStream()
                .collect(Collectors.groupingBy(apsh.backend.po.Order::getId)).entrySet().parallelStream()
                .map(e -> e.getValue().get(0)).collect(Collectors.toMap(apsh.backend.po.Order::getId, o -> o));
        return orderProductionRepository.findAll().parallelStream().map(op -> {
            apsh.backend.po.Order order = orderMap.get(Integer.valueOf(op.getOrderId()));
            SchedulePlanTableOrderVo vo = new SchedulePlanTableOrderVo();
            vo.setId(Integer.valueOf(op.getOrderId()));
            vo.setOrderNo(op.getOrderId());
            vo.setProductNum(order.getProductCount());
            vo.setSchedules(op.getSuborderProductions().parallelStream().map(sp -> {
                int productID = order.getProductId();
                int sc = craftMap.get(String.valueOf(productID)).getStandardCapability();
                ScheduleInSchedulePlanTableOrderVo v = new ScheduleInSchedulePlanTableOrderVo();
                v.setId(getNumber(sp.getSuborderId()));
                v.setStartTime(sp.getStartTime());
                v.setEndTime(sp.getEndTime());
                v.setProductNum(Math.toIntExact(sc * sp.getWorkHours()));
                return v;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());
    }

    private Integer getNumber(String suborderId) {
        StringBuilder sb = new StringBuilder();
        for (char ch : suborderId.toCharArray()) {
            if (Character.isDigit(ch)) {
                sb.append(ch);
            }
        }
        return Integer.valueOf(sb.toString());
    }

    @Override
    public List<ScheduleOrderProductionTableRelationVo> getOrderProductionTable() {
        List<ScheduleOrderProductionTableRelationVo> SOPTRVOList = new ArrayList<>();
        for (OrderProduction OP : orderProductionRepository.findAll()) {
            SOPTRVOList.addAll(OP.getScheduleOrderProductionTableRelationVoS());

        }
        return SOPTRVOList;
    }

    @Override
    public List<ScheduleProductionTableProductionVo> getProductionTable() {
        List<apsh.backend.po.Order> allOrders = legacySystemService.getAllOrders();

        List<ScheduleProductionTableProductionVo> SPTPVOList = new ArrayList<>();
        for (OrderProduction OP : orderProductionRepository.findAll()) {
            for (apsh.backend.po.Order order : allOrders) {
                if (Integer.parseInt(OP.getOrderId()) == order.getId()) {
                    SPTPVOList.addAll(OP.getScheduleProductionTableProductionVo(order.getProductId()));
                }
            }

        }
        return SPTPVOList;
    }

    @Override
    public List<ScheduleProductionResourceTableProductionVo> getProductionResourceTable() {
        List<ScheduleProductionResourceTableProductionVo> SPRTPVOList = new ArrayList<>();

        for (OrderProduction OP : orderProductionRepository.findAll()) {
            SPRTPVOList.addAll(OP.getScheduleProductionResourceTableProductionVoS());

        }

        return SPRTPVOList;
    }

    /**
     * 生成所有的时间粒度
     */
    private List<TimeGrain> generateTimeGrains(List<Order> orders, Date startTime, Date availableStartTime,
            double denseFactor) {
        int availableTimeGrainCount = 0;
        for (Order order : orders)
            availableTimeGrainCount += order.getNeedTimeInHour() / maxSuborderNeedTimeInHour + 1;
        availableTimeGrainCount = (int) ((double) availableTimeGrainCount * denseFactor) + 5;
        List<TimeGrain> timeGrains = new ArrayList<>(availableTimeGrainCount);
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        int startHourOfDay = startTimeCalendar.get(Calendar.HOUR_OF_DAY);
        Calendar tempCalendar = Calendar.getInstance();
        int tempDayOfWeek = -1;
        for (int i = 0; i < availableTimeGrainCount; i++) {
            Date date = new Date(startTime.getTime() + i * maxSuborderNeedTimeInHour * millisecondCountPerHour);
            // 周末不上班
            tempCalendar.setTime(date);
            tempDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);
            if (tempDayOfWeek == Calendar.SUNDAY || tempDayOfWeek == Calendar.SATURDAY) {
                availableTimeGrainCount++;
                startHourOfDay = (startHourOfDay + maxSuborderNeedTimeInHour) % 24;
                continue;
            }
            Date endDate = new Date(date.getTime() + maxSuborderNeedTimeInHour * millisecondCountPerHour);
            tempCalendar.setTime(endDate);
            tempDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);
            if (tempDayOfWeek == Calendar.SUNDAY || tempDayOfWeek == Calendar.SATURDAY) {
                availableTimeGrainCount++;
                startHourOfDay = (startHourOfDay + maxSuborderNeedTimeInHour) % 24;
                continue;
            }
            // 添加时间粒度
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
                    / millisecondCountPerHour / maxSuborderNeedTimeInHour);
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
            OrderProductionDto dto = orderProductionDtoMap.get(suborder.getOrderId());
            Date startTime = (suborder.getTimeGrain() == null) ? null : suborder.getTimeGrain().getTime();
            Date endTime = (startTime == null) ? null
                    : new Date(startTime.getTime() + suborder.getNeedTimeInHour() * millisecondCountPerHour);
            SuborderProductionDto suborderDto = new SuborderProductionDto(suborder.getId(), startTime, endTime,
                    suborder.getManpowerIds(), (suborder.getDevice() == null) ? null : suborder.getDevice().getId());
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
        List<OrderProduction> orderProductionPos = new ArrayList<>(solutionDto.size());
        for (OrderProductionDto orderProductionDto : solutionDto) {
            Set<SuborderProduction> suborderProductionPos = new HashSet<>(orderProductionDto.getSuborders().size());
            for (SuborderProductionDto dto : orderProductionDto.getSuborders()) {
                Timestamp startTimestamp = (dto.getStartTime() == null) ? null
                        : new Timestamp(dto.getStartTime().getTime());
                Timestamp endTimestamp = (dto.getEndTime() == null) ? null : new Timestamp(dto.getEndTime().getTime());
                suborderProductionPos.add(new SuborderProduction(null, dto.getId(), startTimestamp, endTimestamp,
                        dto.getManpowerIds(), dto.getDeviceId()));
            }
            orderProductionPos.add(new OrderProduction(null, orderProductionDto.getId(), suborderProductionPos));
        }
        orderProductionRepository.deleteAll();
        orderProductionRepository.saveAll(orderProductionPos);
    }

    private void deleteInputAndSolution() {
        orderProductionRepository.deleteAll();
    }
}