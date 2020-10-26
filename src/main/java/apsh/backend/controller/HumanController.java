package apsh.backend.controller;

import apsh.backend.service.HumanService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.HumanVo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/hr")
public class HumanController {

    private final HumanService humanService;

    private final LogFormatter logger;

    @Autowired
    public HumanController(HumanService humanService) {
        this.humanService = humanService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(HumanController.class));
    }

    @PostMapping()
    public void postManpower(@RequestBody HumanVo vo) {
        // TODO:
    }

    @DeleteMapping(value = "/{id}")
    public void deleteManpower(@PathVariable String id) {
        // TODO:
    }

    @PutMapping(value="/{id}")
    public void putManpower(@PathVariable String id, @RequestBody HumanVo vo) {
        //TODO:
    }

    @GetMapping(value = "/all")
    public List<HumanVo> getManpower(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }
}