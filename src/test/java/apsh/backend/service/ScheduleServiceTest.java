package apsh.backend.service;

import java.text.ParseException;
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

    // 早班
    static final List<TimeSectionDto> dayTime = Arrays.asList(new TimeSectionDto(7, 19));
    // 晚班
    static final List<TimeSectionDto> nightTime = Arrays.asList(new TimeSectionDto(19, 24), new TimeSectionDto(0, 7));

    @Test
    void testGetArrangementCnm() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("丁（3）", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("刘（3）", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("张（3）", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("赵（3）", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev3", "line3"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-10-01 7");
        orderDtos.add(new OrderDto("order1", false, 96, 4, dateFormat.parse("2020-10-02 09"),
                Arrays.asList("丁（3）", "刘（3）", "张（3）", "赵（3）"), Arrays.asList("line3")));

        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 3);
    }

    @Test
    void testGetArrangementSmall() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man4", 5, nightTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line2"));
        deviceDtos.add(new DeviceDto("dev3", "line3"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-10-30 7");
        orderDtos.add(new OrderDto("order1", false, 24, 7, dateFormat.parse("2020-11-02 09"),
                Arrays.asList("man1", "man2", "man3", "man4"), Arrays.asList("line3")));
        orderDtos.add(new OrderDto("order2", false, 24, 8, dateFormat.parse("2020-11-02 12"),
                Arrays.asList("man1", "man2"), Arrays.asList("line1", "line2")));
        orderDtos.add(new OrderDto("order3", false, 24, 6, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man3", "man4"), Arrays.asList("line2", "line3")));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 3);
    }

    @Test
    void testGetArrangement() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man4", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man5", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man6", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man7", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man8", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man9", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man10", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man11", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man12", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man13", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man14", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man15", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man16", 5, nightTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line1"));
        deviceDtos.add(new DeviceDto("dev4", "line1"));
        deviceDtos.add(new DeviceDto("dev5", "line2"));
        deviceDtos.add(new DeviceDto("dev6", "line2"));
        deviceDtos.add(new DeviceDto("dev7", "line2"));
        deviceDtos.add(new DeviceDto("dev8", "line2"));
        deviceDtos.add(new DeviceDto("dev9", "line3"));
        deviceDtos.add(new DeviceDto("dev10", "line3"));
        deviceDtos.add(new DeviceDto("dev11", "line3"));
        deviceDtos.add(new DeviceDto("dev12", "line3"));
        deviceDtos.add(new DeviceDto("dev13", "line4"));
        deviceDtos.add(new DeviceDto("dev14", "line4"));
        deviceDtos.add(new DeviceDto("dev15", "line4"));
        deviceDtos.add(new DeviceDto("dev16", "line4"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-10-11 7");
        orderDtos.add(new OrderDto("order1", false, 36, 7, dateFormat.parse("2020-11-02 09"),
                Arrays.asList("man7", "man8", "man10", "man11"), Arrays.asList("line3", "line4")));
        orderDtos.add(new OrderDto("order2", false, 48, 8, dateFormat.parse("2020-11-02 12"),
                Arrays.asList("man5", "man6", "man14", "man15"), Arrays.asList("line1", "line2")));
        orderDtos.add(new OrderDto("order3", false, 24, 6, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man3", "man4", "man11", "man12"), Arrays.asList("line2", "line3")));
        orderDtos.add(new OrderDto("order4", false, 48, 8, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man1", "man2", "man12", "man13"), Arrays.asList("line1", "line4")));
        orderDtos.add(new OrderDto("order5", false, 60, 9, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man1", "man3", "man9", "man10"), Arrays.asList("line1", "line3")));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 3);
    }

    @Test
    void testGetArrangementPersistence() {
        try {
            List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
            System.out.println(orderProductionDtos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetArrangementUrgentOrder() throws ParseException {
        // 初始排程
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 5, nightTime));
        manpowerDtos.add(new ManpowerDto("man4", 5, nightTime));
        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line2"));
        deviceDtos.add(new DeviceDto("dev4", "line2"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-11-01 11");
        orderDtos.add(new OrderDto("1", false, 4, 7, dateFormat.parse("2020-11-02 09"), Arrays.asList("man1", "man2"),
                Arrays.asList("line1")));
        orderDtos.add(new OrderDto("2", false, 4, 8, dateFormat.parse("2020-11-02 12"),
                Arrays.asList("man2", "man3", "man4"), Arrays.asList("line2")));
        orderDtos.add(new OrderDto("3", false, 4, 6, dateFormat.parse("2020-11-02 14"), Arrays.asList("man3", "man4"),
                Arrays.asList("line1", "line2")));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 3);
        // 插入订单
        OrderDto urgentOrderDto = new OrderDto("4", true, 4, 8, dateFormat.parse("2020-11-02 07"),
                Arrays.asList("man1", "man2", "man3"), Arrays.asList("line1", "line2"));
        service.arrangeUrgentOrder(manpowerDtos, deviceDtos, orderDtos, urgentOrderDto,
                dateFormat.parse("2020-11-01 13"), startTime);
        orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 4);
    }
}