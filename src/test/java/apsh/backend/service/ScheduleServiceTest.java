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
    static final TimeSectionDto dayTime = new TimeSectionDto(7, 12);
    // 晚班
    static final TimeSectionDto nightTime = new TimeSectionDto(19, 12);

    // 1班
    static final TimeSectionDto firstTime = new TimeSectionDto(7, 8);
    // 2班
    static final TimeSectionDto secondTime = new TimeSectionDto(15, 8);
    // 3班
    static final TimeSectionDto thirdTime = new TimeSectionDto(23, 8);

    @Test
    void testGetArrangementSimple() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("丁A", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("刘B", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("张C", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("赵D", 3, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line2"));
        deviceDtos.add(new DeviceDto("dev4", "line2"));
        deviceDtos.add(new DeviceDto("dev5", "line3"));
        deviceDtos.add(new DeviceDto("dev6", "line3"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-10-01 7");
        orderDtos.add(new OrderDto("order1", false, 24, 4, dateFormat.parse("2020-10-02 09"), Arrays.asList("丁A", "刘B"),
                Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("order2", false, 96, 3, dateFormat.parse("2020-10-02 09"), Arrays.asList("张C", "赵D"),
                Arrays.asList("line2", "line3"), null));

        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }

    @Test
    void testGetArrangementCnm() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("丁A", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("刘B", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("张C", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("赵D", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("c1", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("c2", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("c3", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("c4", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("cnm", 3, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line2"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("devxx", "linex"));
        deviceDtos.add(new DeviceDto("dev4", "line2"));
        deviceDtos.add(new DeviceDto("dev5", "line3"));
        deviceDtos.add(new DeviceDto("dev6", "line3"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-10-01 7");
        orderDtos.add(new OrderDto("order1", false, 24, 4, dateFormat.parse("2020-10-02 09"),
                Arrays.asList("丁A", "刘B", "张C", "赵D"), Arrays.asList("line3"), null));
        orderDtos.add(new OrderDto("order2", false, 96, 3, dateFormat.parse("2020-10-02 09"),
                Arrays.asList("c1", "c2", "c3", "c4"), Arrays.asList("line1", "line2"), null));

        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }

    @Test
    void testDeviceLoadBalance() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 5, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));
        deviceDtos.add(new DeviceDto("dev3", "line1"));

        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-11-02 7");
        orderDtos.add(new OrderDto("order1", false, 24, 5, dateFormat.parse("2020-11-22 09"), Arrays.asList("man1"),
                Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("order2", false, 24, 5, dateFormat.parse("2020-11-22 12"), Arrays.asList("man2"),
                Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("order3", false, 24, 5, dateFormat.parse("2020-11-22 14"), Arrays.asList("man3"),
                Arrays.asList("line1"), null));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }

    @Test
    void testGetArrangementSmall() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man4", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man5", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man6", 5, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1 1", "line1"));
        deviceDtos.add(new DeviceDto("dev1 2", "line1"));
        deviceDtos.add(new DeviceDto("dev1 3", "line1"));
        deviceDtos.add(new DeviceDto("dev2 1", "line2"));
        deviceDtos.add(new DeviceDto("dev2 2", "line2"));
        deviceDtos.add(new DeviceDto("dev2 3", "line2"));
        deviceDtos.add(new DeviceDto("dev3 1", "line3"));
        deviceDtos.add(new DeviceDto("dev3 2", "line3"));
        deviceDtos.add(new DeviceDto("dev3 3", "line3"));
        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-11-02 7");
        orderDtos.add(new OrderDto("order1", false, 24, 5, dateFormat.parse("2020-11-02 09"),
                Arrays.asList("man1", "man2"), Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("order2", false, 24, 5, dateFormat.parse("2020-11-02 12"),
                Arrays.asList("man3", "man4"), Arrays.asList("line2"), null));
        orderDtos.add(new OrderDto("order3", false, 24, 5, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man5", "man6"), Arrays.asList("line3"), null));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }

    @Test
    void testStage() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 5, dayTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("dev1", "line1"));
        deviceDtos.add(new DeviceDto("dev2", "line1"));

        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2020-11-02 7");
        orderDtos.add(new OrderDto("order1 装配", false, 24, 5, dateFormat.parse("2020-11-22 09"), Arrays.asList("man1"),
                Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("order1 测试", false, 24, 5, dateFormat.parse("2020-11-22 12"), Arrays.asList("man2"),
                Arrays.asList("line1"), "order1 装配"));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
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
                Arrays.asList("man7", "man8", "man10", "man11"), Arrays.asList("line3", "line4"), null));
        orderDtos.add(new OrderDto("order2", false, 48, 8, dateFormat.parse("2020-11-02 12"),
                Arrays.asList("man5", "man6", "man14", "man15"), Arrays.asList("line1", "line2"), null));
        orderDtos.add(new OrderDto("order3", false, 24, 6, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man3", "man4", "man11", "man12"), Arrays.asList("line2", "line3"), null));
        orderDtos.add(new OrderDto("order4", false, 48, 8, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man1", "man2", "man12", "man13"), Arrays.asList("line1", "line4"), null));
        orderDtos.add(new OrderDto("order5", false, 60, 9, dateFormat.parse("2020-11-02 14"),
                Arrays.asList("man1", "man3", "man9", "man10"), Arrays.asList("line1", "line3"), null));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
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
                Arrays.asList("line1"), null));
        orderDtos.add(new OrderDto("3", false, 4, 6, dateFormat.parse("2020-11-02 14"), Arrays.asList("man3", "man4"),
                Arrays.asList("line1", "line2"), null));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
        // 插入订单
        OrderDto urgentOrderDto = new OrderDto("4", true, 4, 8, dateFormat.parse("2020-11-03 07"),
                Arrays.asList("man1", "man2", "man3"), Arrays.asList("line1", "line2"), null);
        service.arrangeUrgentOrder(manpowerDtos, deviceDtos, orderDtos, urgentOrderDto,
                dateFormat.parse("2020-11-01 13"), startTime);
        orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }

    @Test
    void testGetArrangementJuTmNiubi() throws ParseException {
        List<ManpowerDto> manpowerDtos = new ArrayList<>();
        manpowerDtos.add(new ManpowerDto("man1", 5, dayTime));
        manpowerDtos.add(new ManpowerDto("man2", 3, dayTime));
        manpowerDtos.add(new ManpowerDto("man3", 4, dayTime));
        manpowerDtos.add(new ManpowerDto("man4", 6, nightTime));
        manpowerDtos.add(new ManpowerDto("man5", 3, nightTime));
        manpowerDtos.add(new ManpowerDto("man6", 4, nightTime));
        manpowerDtos.add(new ManpowerDto("man7", 4, firstTime));
        manpowerDtos.add(new ManpowerDto("man8", 6, firstTime));
        manpowerDtos.add(new ManpowerDto("man9", 3, firstTime));
        manpowerDtos.add(new ManpowerDto("man10", 4, secondTime));
        manpowerDtos.add(new ManpowerDto("man11", 4, secondTime));
        manpowerDtos.add(new ManpowerDto("man12", 4, secondTime));
        manpowerDtos.add(new ManpowerDto("man13", 4, thirdTime));
        manpowerDtos.add(new ManpowerDto("man14", 4, thirdTime));
        manpowerDtos.add(new ManpowerDto("man15", 4, thirdTime));

        List<DeviceDto> deviceDtos = new ArrayList<>();
        deviceDtos.add(new DeviceDto("line1_1", "line1"));
        deviceDtos.add(new DeviceDto("line1_2", "line1"));
        deviceDtos.add(new DeviceDto("line1_3", "line1"));
        deviceDtos.add(new DeviceDto("line1_4", "line1"));
        deviceDtos.add(new DeviceDto("line1_5", "line1"));
        deviceDtos.add(new DeviceDto("line1_6", "line1"));
        deviceDtos.add(new DeviceDto("line1_7", "line1"));
        deviceDtos.add(new DeviceDto("line1_8", "line1"));
        deviceDtos.add(new DeviceDto("line1_9", "line1"));
        deviceDtos.add(new DeviceDto("line2_1", "line2"));
        deviceDtos.add(new DeviceDto("line2_2", "line2"));
        deviceDtos.add(new DeviceDto("line2_3", "line2"));
        deviceDtos.add(new DeviceDto("line2_4", "line2"));
        deviceDtos.add(new DeviceDto("line2_5", "line2"));
        deviceDtos.add(new DeviceDto("line2_6", "line2"));
        deviceDtos.add(new DeviceDto("line2_6", "line2"));
        deviceDtos.add(new DeviceDto("line3_1", "line3"));
        deviceDtos.add(new DeviceDto("line3_2", "line3"));
        deviceDtos.add(new DeviceDto("line3_3", "line3"));
        deviceDtos.add(new DeviceDto("line3_4", "line3"));
        deviceDtos.add(new DeviceDto("line3_5", "line3"));
        deviceDtos.add(new DeviceDto("line3_6", "line3"));
        deviceDtos.add(new DeviceDto("line3_7", "line3"));
        deviceDtos.add(new DeviceDto("line3_8", "line3"));
        deviceDtos.add(new DeviceDto("line3_9", "line3"));
        deviceDtos.add(new DeviceDto("line4_1", "line4"));
        deviceDtos.add(new DeviceDto("line4_2", "line4"));
        deviceDtos.add(new DeviceDto("line4_3", "line4"));
        deviceDtos.add(new DeviceDto("line4_4", "line4"));
        deviceDtos.add(new DeviceDto("line4_5", "line4"));
        deviceDtos.add(new DeviceDto("line4_6", "line4"));
        deviceDtos.add(new DeviceDto("line4_7", "line4"));
        deviceDtos.add(new DeviceDto("line4_8", "line4"));

        List<OrderDto> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date startTime = dateFormat.parse("2021-1-9 7");
        orderDtos.add(new OrderDto("order1", false, 72, 7, dateFormat.parse("2021-06-02 09"),
                Arrays.asList("man1", "man2", "man3", "man7", "man8", "man9", "13", "14", "15"),
                Arrays.asList("line1", "line2"), null));
        orderDtos.add(new OrderDto("order2", false, 48, 8, dateFormat.parse("2021-07-21 12"),
                Arrays.asList("man1", "man2", "man3", "man4", "man5", "man6"), Arrays.asList("line2", "line3"), null));
        orderDtos.add(new OrderDto("order3", false, 60, 6, dateFormat.parse("2021-08-29 14"),
                Arrays.asList("man4", "man5", "man6", "man10", "man11", "man12"), Arrays.asList("line3", "line4"),
                null));
        orderDtos.add(new OrderDto("order4", false, 48, 8, dateFormat.parse("2021-05-01 14"),
                Arrays.asList("man7", "man8", "man9", "man10", "man11", "man12", "man13", "man14", "man15"),
                Arrays.asList("line1", "line2", "line4"), null));
        orderDtos.add(new OrderDto("order5", false, 60, 9, dateFormat.parse("2021-05-01 14"),
                Arrays.asList("man1", "man2", "man3", "man7", "man8", "man9", "man13", "man14", "man15"),
                Arrays.asList("line1", "line3", "line4"), null));
        orderDtos.add(new OrderDto("order5 装配", false, 60, 9, dateFormat.parse("2020-06-02 14"),
                Arrays.asList("man1", "man2", "man3", "man7", "man8", "man9", "man13", "man14", "man15"),
                Arrays.asList("line1", "line3", "line4"), null));
        orderDtos.add(new OrderDto("order5 测试", false, 12, 2, dateFormat.parse("2020-06-02 14"),
                Arrays.asList("man1", "man2", "man3", "man7", "man8", "man9", "man13", "man14", "man15"),
                Arrays.asList("line1", "line3", "line4"), "order5 装配"));
        orderDtos.add(new OrderDto("order6", false, 72, 7, dateFormat.parse("2021-06-11 14"),
                Arrays.asList("man4", "man5", "man6", "man10", "man11", "man12", "man13", "man14", "man15"),
                Arrays.asList("line1", "line3", "line4"), null));
        orderDtos.add(new OrderDto("order6", false, 72, 7, dateFormat.parse("2021-05-13 14"),
                Arrays.asList("man4", "man5", "man6", "man7", "man8", "man9", "man13", "man14", "man15"),
                Arrays.asList("line2", "line4"), null));
        service.arrangeInitialOrders(manpowerDtos, deviceDtos, orderDtos, startTime);
        List<OrderProductionDto> orderProductionDtos = service.getCurrentArrangment();
        System.out.println(orderProductionDtos.size());
        assert (orderProductionDtos.size() >= 1);
    }
}