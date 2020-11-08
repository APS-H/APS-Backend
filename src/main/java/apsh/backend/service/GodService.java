package apsh.backend.service;

import apsh.backend.dto.CustomerOrderDto;
import apsh.backend.dto.DeviceDto;
import apsh.backend.dto.ManpowerDto;
import apsh.backend.dto.OrderDto;
import apsh.backend.po.Craft;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface GodService {

    // 愿主保佑，排程算法一切顺利！
    default void godBlessMe(ScheduleService scheduleService) {
        new Thread(() -> {
            // 调用排程模块重新计算排程
            scheduleService.removeCurrentArrangement();
            List<ManpowerDto> manpowerDtos = prepareManPowers();
            List<DeviceDto> deviceDtos = prepareDevices();
            List<OrderDto> orderDtos = prepareOrders();

            scheduleService.arrangeInitialOrders(
                    manpowerDtos,
                    deviceDtos,
                    orderDtos,
                    schedulingStartTime()
            );
        }).start();
    }

    // 调用排程模块紧急插单，重新计算排程
    default void godBlessMeAgain(ScheduleService scheduleService, CustomerOrderDto urgentOrder) {
        new Thread(() -> {
            Map<String, Craft> crafts = prepareCrafts();
            List<ManpowerDto> manpowerDtos = prepareManPowers();
            List<DeviceDto> deviceDtos = prepareDevices();
            List<OrderDto> orderDtos = prepareOrders();

            Craft craft = crafts.get(String.valueOf(urgentOrder.getProductId()));
            if (craft == null) {
                // 说明该订单生产的物料没有对应的工艺，直接去掉该紧急订单，不需要重新排程
                return;
            }
            OrderDto urgentOrderDto = new OrderDto(urgentOrder, craft, true);

            scheduleService.arrangeUrgentOrder(
                    manpowerDtos, deviceDtos, orderDtos, urgentOrderDto,
                    schedulingInsertTime(), schedulingStartTime()
            );
        }).start();
    }

    Map<String, Craft> prepareCrafts();

    List<ManpowerDto> prepareManPowers();

    List<DeviceDto> prepareDevices();

    List<OrderDto> prepareOrders();

    default Date schedulingStartTime() {
        return null;
    }

    default Date schedulingInsertTime() {
        return null;
    }

}
