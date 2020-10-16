package apsh.backend.controller;

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
    @PostMapping()
    public void postTime(@RequestBody TimeVo vo) {
        // TODO:
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