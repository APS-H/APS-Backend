package apsh.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import apsh.backend.dto.OrderProductionDto;
import apsh.backend.dto.SystemTime;

@SpringBootTest
public class TimeServiceTest {
    @Autowired
    TimeService timeService;

    @Autowired
    ScheduleService scheduleService;

    @Test
    void setTime() throws InterruptedException {
        SystemTime systemTime = new SystemTime(
                LocalDateTime.of(2018, 11, 1, 7, 0, 0),
                2.0,
                LocalDateTime.now()
        );
        timeService.setTime(systemTime);
        Thread.sleep(60000);
        List<OrderProductionDto> orderProductionDtos = null;
        int count = 0;
        while (null == orderProductionDtos) {
            Thread.sleep(5000);
            orderProductionDtos = scheduleService.tryGetCurrentArrangement();
            count++;
        }
        System.out.println("count=" + count);
        orderProductionDtos.forEach(System.out::println);
    }
}