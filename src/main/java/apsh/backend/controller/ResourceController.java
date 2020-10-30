package apsh.backend.controller;

import apsh.backend.service.ResourceService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.ResourceLoadVo;
import apsh.backend.vo.ResourceUseVo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/resource")
public class ResourceController {
    private final ResourceService resourceService;
    private final LogFormatter logger;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderController.class));
    }

    @GetMapping(value = "/load/all")
    public ResourceLoadVo getResourceLoad(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/use/all")
    public List<ResourceUseVo> getResourceUse(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

}