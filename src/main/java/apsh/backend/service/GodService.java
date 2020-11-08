package apsh.backend.service;

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
    default void godBlessMe(
            ScheduleService scheduleService,
            LegacySystemService legacySystemService,
            OrderService orderService,
            HumanService humanService,
            EquipmentService equipmentService,
            LocalDateTime now
    ) {
        new Thread(() -> {
            // TODO 调用排程模块重新计算排程
            scheduleService.removeCurrentArrangement();
            Map<String, Craft> crafts = legacySystemService.getAllCrafts().parallelStream()
                    .collect(Collectors.toMap(Craft::getProductionId, c -> c));
            List<ManpowerDto> manpowerDtos = humanService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                    .map(ManpowerDto::new)
                    .collect(Collectors.toList());
            List<DeviceDto> deviceDtos = equipmentService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                    .flatMap(e -> IntStream.range(0, e.getCount()).mapToObj(id -> new DeviceDto(id, e)))
                    .collect(Collectors.toList());
            List<OrderDto> orderDtos = orderService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                    .map(o -> {
                        Craft craft = crafts.get(String.valueOf(o.getProductId()));
                        if (craft == null) {
                            // 说明该订单生产的物料没有对应的工艺，直接去掉该订单
                            return null;
                        }
                        return new OrderDto(o, craft);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            Date startTime = Date.from(LocalDateTime.of(now.toLocalDate(), LocalTime.of(now.getHour(), 0))
                    .atZone(ZoneId.systemDefault()).toInstant());
            scheduleService.arrangeInitialOrders(
                    manpowerDtos,
                    deviceDtos,
                    orderDtos,
                    startTime
            );
        }).start();
    }

}
