package apsh.backend.serviceimpl;

import apsh.backend.dto.SystemTime;
import apsh.backend.service.*;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeServiceImpl implements TimeService, GodService {

    @Value("${time-server-url}")
    private String timeServerUrl;

    private final ScheduleService scheduleService;
    private final LegacySystemService legacySystemService;
    private final OrderService orderService;
    private final HumanService humanService;
    private final EquipmentService equipmentService;

    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(TimeServiceImpl.class));

    @Autowired
    public TimeServiceImpl(
            ScheduleService scheduleService,
            LegacySystemService legacySystemService,
            OrderService orderService,
            HumanService humanService,
            EquipmentService equipmentService
    ) {
        this.scheduleService = scheduleService;
        this.legacySystemService = legacySystemService;
        this.orderService = orderService;
        this.humanService = humanService;
        this.equipmentService = equipmentService;
    }

    @Override
    public void updateTime(SystemTime systemTime) {
        if (systemTime.getStartTime() == null && systemTime.getTimeSpeed() == null) {
            logger.errorService("updateTime", systemTime, "startTime and timeSpeed cannot be both null");
            return;
        }
        systemTime.setTimestamp(LocalDateTime.now());
        saveTime(systemTime);
    }

    @Override
    public void setTime(SystemTime systemTime) {
        if (systemTime.getStartTime() == null || systemTime.getTimeSpeed() == null) {
            logger.errorService("setTime", systemTime, "startTime or timeSpeed cannot be null");
            return;
        }
        systemTime.setTimestamp(LocalDateTime.now());
        saveTime(systemTime);
    }

    @Override
    public SystemTime getTime() {
        return loadTime();
    }

    @Override
    public LocalDateTime now() {
        SystemTime systemTime = loadTime();
        assert systemTime != null;
        Duration d = Duration.between(systemTime.getTimestamp(), LocalDateTime.now());
        return systemTime.getStartTime().plusMinutes((long) (d.toMinutes() * systemTime.getTimeSpeed()));
    }

    private void saveTime(SystemTime systemTime) {
        Path path = Paths.get(timeServerUrl);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
                logger.errorService("saveTime", systemTime, e.getLocalizedMessage());
                return;
            }
        } else if (systemTime.getStartTime() == null || systemTime.getTimeSpeed() == null) {
            SystemTime curTime = loadTime();
            if (curTime == null) return;
            if (systemTime.getStartTime() == null) {
                systemTime.setStartTime(curTime.getStartTime());
            } else {
                systemTime.setTimeSpeed(curTime.getTimeSpeed());
            }
        }

        try {
            PrintWriter pw = new PrintWriter(path.toFile());
            pw.println(systemTime.getStartTime() + "," + systemTime.getTimeSpeed() + "," + systemTime.getTimestamp());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.errorService("saveTime", systemTime, e.getLocalizedMessage());
        }

        // 调用排程模块重新计算排程
        this.godBlessMe(
                scheduleService, legacySystemService, orderService, humanService, equipmentService,
                LocalDateTime.now()
        );
    }

    // return null if time recorded illegal
    private SystemTime loadTime() {
        Path path = Paths.get(timeServerUrl);
        if (!Files.exists(path)) {
            logger.errorService("loadTime", "", "file `" + timeServerUrl + "` doesn't exist");
            return null;
        }
        List<String> lines;
        try {
            lines = Files.lines(path).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            logger.errorService("loadTime", "", e.getLocalizedMessage());
            return null;
        }
        if (lines.size() != 1) {
            logger.errorService("loadTime", "", "broken file `" + timeServerUrl + "`");
            return null;
        }
        String[] values = lines.get(0).split(",");
        try {
            return new SystemTime(
                    LocalDateTime.parse(values[0]),
                    Double.parseDouble(values[1]),
                    LocalDateTime.parse(values[2])
            );
        } catch (Exception ignored) {
            return null;
        }
    }

}
