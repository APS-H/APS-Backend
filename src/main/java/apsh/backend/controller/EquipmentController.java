package apsh.backend.controller;

import apsh.backend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.EquipmentVo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping()
    public void postDevice(@RequestBody EquipmentVo vo) {
        //TODO:
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDevice(@PathVariable String id) {
        // TODO:
    }

    @PutMapping(value="/{id}")
    public void putDevice(@PathVariable String id, @RequestBody EquipmentVo vo) {
        // TODO:
    }

    @GetMapping(value = "/all")
    public List<EquipmentVo> getDevice(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }
    
}