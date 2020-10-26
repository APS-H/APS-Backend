package apsh.backend.controller;

import apsh.backend.dto.SystemTime;
import apsh.backend.service.TimeService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import apsh.backend.vo.TimeVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/system-time")
public class TimeController {

    private final TimeService timeService;

    private final LogFormatter logger;

    @Autowired
    public TimeController(TimeService timeService) {
        this.timeService = timeService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(TimeController.class));
    }

    @PostMapping()
    public void postTime(@RequestBody TimeVo vo) {
        logger.infoControllerRequest("POST", "/system-time", vo);
        timeService.updateTime(new SystemTime(vo));
    }

    @PutMapping()
    public void putTime(@RequestBody TimeVo vo) {
        logger.infoControllerRequest("PUT", "/system-time", vo);
        timeService.setTime(new SystemTime(vo));
    }

    @GetMapping()
    public TimeVo getTime() {
        logger.infoControllerRequest("POST", "/system-time", null);
        SystemTime systemTime = timeService.getTime();
        return new TimeVo(systemTime);
    }

}