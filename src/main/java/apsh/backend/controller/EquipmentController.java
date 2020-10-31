package apsh.backend.controller;

import apsh.backend.dto.EquipmentDto;
import apsh.backend.service.EquipmentService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import apsh.backend.vo.EquipmentVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    private final LogFormatter logger;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(EquipmentController.class));
    }

    @PostMapping()
    public void postDevice(@RequestBody EquipmentVo vo) {
        logger.infoControllerRequest("POST", "/equipment", vo);
        this.equipmentService.update(new EquipmentDto(vo));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDevice(@PathVariable String id) {
        logger.infoControllerRequest("DELETE", "/equipment/" + id, new Object());
        this.equipmentService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public void putDevice(@PathVariable String id, @RequestBody EquipmentVo vo) {
        logger.infoControllerRequest("PUT", "/equipment" + id, vo);
        this.equipmentService.add(id, new EquipmentDto(vo));
    }

    @GetMapping(value = "/all")
    public List<EquipmentVo> getDevice(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/equipment/all", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<EquipmentVo> equipmentVos = this.equipmentService.getAll(pageSize, pageNum).parallelStream()
                .map(EquipmentVo::new).collect(Collectors.toList());
        logger.infoControllerResponse("GET", "/equipment/all", equipmentVos);
        return equipmentVos;
    }

}