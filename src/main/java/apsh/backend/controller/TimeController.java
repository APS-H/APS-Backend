package apsh.backend.controller;

import apsh.backend.service.TimeService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.TimeVo;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

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
    }

    @PutMapping()
    public void putTime(@PathVariable String id, @RequestBody TimeVo vo) {
        // TODO:
    }

    @GetMapping()
    public TimeVo getTime() {
        // TODO:
        return null;
    }

}