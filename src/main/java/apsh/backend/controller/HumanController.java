package apsh.backend.controller;

import apsh.backend.dto.HumanDto;
import apsh.backend.service.HumanService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.HumanVo;

import java.util.List;
import java.util.stream.Collectors;

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
    public void postHuman(@RequestBody HumanVo vo) {
        logger.infoControllerRequest("POST", "/hr", vo);
        humanService.update(new HumanDto(vo));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteHuman(@PathVariable String id) {
        logger.infoControllerRequest("DELETE", "/hr/" + id, new Object());
        humanService.delete(Integer.valueOf(id));
    }

    @PutMapping(value = "/{id}")
    public void putHuman(@PathVariable String id, @RequestBody HumanVo vo) {
        logger.infoControllerRequest("PUT", "/hr/" + id, vo);
        humanService.add(Integer.valueOf(id), new HumanDto(vo));
    }

    @GetMapping(value = "/all")
    public List<HumanVo> getHuman(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/hr/all", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<HumanVo> humanVos = humanService.getAll(pageSize, pageNum).parallelStream()
                .map(HumanVo::new).collect(Collectors.toList());
        logger.infoControllerResponse("GET", "/hr/all", humanVos);
        return humanVos;
    }
}