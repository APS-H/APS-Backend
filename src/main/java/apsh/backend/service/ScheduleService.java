package apsh.backend.service;

import java.util.Date;
import java.util.List;

import apsh.backend.dto.ManpowerDto;
import apsh.backend.dto.DeviceDto;
import apsh.backend.dto.OrderDto;
import apsh.backend.dto.OrderProductionDto;

public interface ScheduleService {
    /**
     * 安排初始订单
     */
    void arrangeInitialOrders(List<ManpowerDto> manpowerDtos, List<DeviceDto> deviceDtos, List<OrderDto> orderDtos, Date startTime);

    /**
     * 在已有初始订单安排的情况下安排新插入的紧急订单
     */
    void arrangeUrgentOrder(OrderDto orderDto, Date insertTime);

    /**
     * 移除当前的排程结果 如果有排程正在进行它将会被强制终止
     */
    void removeCurrentArrangement();

    /**
     * 尝试获取当前的排程结果
     * @return 如果目前正在排程将会返回null 否则返回排程结果
     */
    List<OrderProductionDto> tryGetCurrentArrangement();

    /**
     * 阻塞式获取排程结果
     */
    List<OrderProductionDto> getCurrentArrangment();
}