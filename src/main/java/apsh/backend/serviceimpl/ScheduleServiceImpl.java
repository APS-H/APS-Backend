package apsh.backend.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    static Long millisecondCountPerHour = 60L * 60L * 1000L;

    @Autowired
    private SolverManager<SuborderSolution, UUID> solverManager;

    // 初始排程
    private List<Manpower> manpowers = null;
    private List<Device> devices = null;
    private List<Order> orders = null;
    // 插单
    private List<Order> urgentOrders = null;
    // 当前的任务
    private SolverJob<SuborderSolution, UUID> currentSolverJob = null;
    // 结果 TODO: 持久化
    private List<OrderProductionDto> solutionDto;

    // 是否正在排程或排程结束 即添加过排程任务
    private boolean solvingOrSolved = false;
    // 当前的排程任务是否已经存储在solutionDto变量中且持久化
    private boolean solutionSaved = false;

    @Override
    public void arrangeInitialOrders(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos,
            List<OrderDto> orderDtos, Date startTime) {
        // 初始化状态
        manpowers = manpowerDtos.stream()
                .map(manpowerDto -> new Manpower(manpowerDto.getId(), manpowerDto.getPeopleCount(),
                        manpowerDto.getWorkSections().stream().map(TimeSection::fromDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
        devices = deviceDtos.stream().map(Device::fromDto).collect(Collectors.toList());
        orders = orderDtos.stream().map(Order::fromDto).collect(Collectors.toList());
        urgentOrders = new ArrayList<>();
        currentSolverJob = null;
        solutionDto = null;
        solvingOrSolved = true;
        solutionSaved = false;

        // 生成所有的时间粒度
        Date timeGrainRange = startTime;
        for (Order order : orders)
            if (order.getDeadline().after(timeGrainRange))
                timeGrainRange = order.getDeadline();
        // 延迟系数
        int factor = 5;
        int availableTimeInHour = (int) ((timeGrainRange.getTime() - startTime.getTime()) / millisecondCountPerHour)
                * factor;
        List<TimeGrain> timeGrains = new ArrayList<>(availableTimeInHour);
        for (int i = 0; i < availableTimeInHour; i++)
            timeGrains.add(new TimeGrain(i, new Date(startTime.getTime() + i * millisecondCountPerHour)));

        // 划分子订单
        // 划分间隔 子订单的最长持续时间 单位为小时 最好是12的因数
        int maxSuborderNeedTimeInHour = 3;
        List<Suborder> suborders = new ArrayList<>(orders.size());
        for (Order order : orders) {
            int suborderIndex = 0;
            int ddlTimeGrainIndex = (int) ((order.getDeadline().getTime() - startTime.getTime())
                    / millisecondCountPerHour);
            int remainTimeInHour = order.getNeedTimeInHour();
            while (remainTimeInHour > 0) {
                suborders.add(Suborder.create(order, suborderIndex++,
                        Math.min(maxSuborderNeedTimeInHour, remainTimeInHour), ddlTimeGrainIndex));
                remainTimeInHour -= maxSuborderNeedTimeInHour;
            }
        }

        // 调用排程库
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        SuborderSolution solution = new SuborderSolution(startTimeCalendar.get(Calendar.HOUR_OF_DAY), manpowers,
                devices, timeGrains, suborders, null);
        UUID problemId = UUID.randomUUID();
        currentSolverJob = solverManager.solve(problemId, solution);
    }

    @Override
    public void arrangeUrgentOrder(OrderDto orderDto, Date insertTime) {
        if (!solvingOrSolved)
            throw new RuntimeException("还没有排程");
        if (currentSolverJob.getSolverStatus() != SolverStatus.NOT_SOLVING)
            throw new RuntimeException("请等待上一次排程结束");
        // 确保上一次排程被保存
        tryGetCurrentArrangement();
        // 维护状态
        urgentOrders.add(new Order(orderDto.getId(), orderDto.getNeedTimeInHour(), orderDto.getNeedPeopleCount(),
                orderDto.getDeadline(), orderDto.getAvailableManpowerIdList(),
                orderDto.getAvailableDeviceTypeIdList()));
        // TODO: 重新排程
    }

    @Override
    public void removeCurrentArrangement() {
        solvingOrSolved = false;
        deleteState();
    }

    @Override
    public List<OrderProductionDto> tryGetCurrentArrangement() {
        if (!solvingOrSolved)
            throw new RuntimeException("还没有输入排程任务");
        // 已经有保存的历史结果
        if (solutionSaved)
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
            saveState(manpowers, devices, orders, urgentOrders, solutionDto);
            solutionSaved = true;
        }
        // 还没有搜索完成
        return null;
    }

    @Override
    public List<OrderProductionDto> getCurrentArrangment() {
        tryGetCurrentArrangement();
        if (solutionDto != null)
            return solutionDto;
        // 阻塞
        SuborderSolution solution = null;
        try {
            solution = currentSolverJob.getFinalBestSolution();
        } catch (Exception e) {
            throw new RuntimeException("排程失败 原因未知");
        }
        solutionDto = getSolutionDto(solution);
        saveState(manpowers, devices, orders, urgentOrders, solutionDto);
        solutionSaved = true;
        return solutionDto;
    }

    // 根据Solution获取Dto
    private List<OrderProductionDto> getSolutionDto(SuborderSolution solution) {
        List<OrderProductionDto> res = new ArrayList<>();
        HashMap<String, OrderProductionDto> orderProductionDtoMap = new HashMap<>();
        for (Order order : orders) {
            OrderProductionDto dto = new OrderProductionDto(order.getId(), new ArrayList<>());
            res.add(dto);
            orderProductionDtoMap.put(order.getId(), dto);
        }
        for (Suborder suborder : solution.getSuborders()) {
            OrderProductionDto dto = orderProductionDtoMap.get(suborder.getOrderId());
            Date startTime = suborder.getTimeGrain().getTime();
            Date endTime = new Date(startTime.getTime() + suborder.getNeedTimeInHour() * millisecondCountPerHour);
            SuborderProductionDto suborderDto = new SuborderProductionDto(suborder.getId(), startTime, endTime,
                    suborder.getManpowerIds(), suborder.getDevice().getId());
            dto.getSuborders().add(suborderDto);
        }
        return res;
    }

    private void saveState(List<Manpower> manpowers, List<Device> devices, List<Order> orders, List<Order> urgentOrders,
            List<OrderProductionDto> solutionDto) {
        // TODO: 持久化
    }

    private void deleteState() {
        // TODO: 持久化
    }
}