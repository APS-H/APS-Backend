package apsh.backend.serviceImpl;

import apsh.backend.dto.SystemTime;
import apsh.backend.service.TimeService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeServiceImpl implements TimeService {

    @Value("${time-server-url}")
    private String timeServerUrl;

    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(TimeServiceImpl.class));

    @Override
    public void updateTime(SystemTime systemTime) {
        if (systemTime.getStartTime() != null || systemTime.getTimeSpeed() != null) {
            logger.errorService("updateTime", systemTime, "startTime and timeSpeed cannot be null");
            return;
        }
        saveTime(systemTime);
    }

    @Override
    public void setTime(SystemTime systemTime) {
        saveTime(systemTime);
    }

    @Override
    public SystemTime getTime() {
        return loadTime();
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
        }
        try {
            PrintWriter pw = new PrintWriter(path.toFile());
            pw.println(systemTime.getStartTime() + "," + systemTime.getTimeSpeed());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.errorService("saveTime", systemTime, e.getLocalizedMessage());
        }
        // TODO 调用排程模块重新计算排程
    }

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
        return new SystemTime(LocalDateTime.parse(values[0]), Double.parseDouble(values[1]));
    }

}
