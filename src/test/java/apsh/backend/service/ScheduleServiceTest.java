package apsh.backend.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import apsh.backend.dto.DeviceDto;
import apsh.backend.dto.ManpowerDto;
import apsh.backend.dto.OrderDto;
import apsh.backend.dto.OrderProductionDto;
import apsh.backend.dto.TimeSectionDto;

@SpringBootTest
public class ScheduleServiceTest {
    @Autowired
    ScheduleService service;

    @Test
    void testGetArrangement() {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, Arrays.asList(new TimeSectionDto(7, 19))));
        manpowerDtos.add(new ManpowerDto("man2", 5, Arrays.asList(new TimeSectionDto(7, 19))));
        manpowerDtos
                .add(new ManpowerDto("man3", 5, Arrays.asList(new TimeSectionDto(19, 24), new TimeSectionDto(0, 7))));
        manpowerDtos
                .add(new ManpowerDto("man4", 5, Arrays.asList(new TimeSectionDto(19, 24), new TimeSectionDto(0, 7))));
        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line2"));
        deviceDtos.add(new DeviceDto("dev4", "line2"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Date startTime = dateFormat.parse("2020-11-01 11");
            orderDtos.add(new OrderDto("1", 4, 7, dateFormat.parse("2020-11-02 09"), Arrays.asList("man1", "man2"),
                    Arrays.asList("line1")));
            orderDtos.add(new OrderDto("2", 4, 8, dateFormat.parse("2020-11-02 12"), Arrays.asList("man2", "man4"),
                    Arrays.asList("line2")));
            orderDtos.add(new OrderDto("3", 4, 6, dateFormat.parse("2020-11-02 14"), Arrays.asList("man3", "man4"),
                    Arrays.asList("line1", "line2")));
            service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
            List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
            System.out.println(orderProductionDtos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetArrangementPersistence() {
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
    }
}