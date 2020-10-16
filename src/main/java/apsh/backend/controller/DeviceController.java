package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.DeviceVo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping(value = "/device")
public class DeviceController {
    @PostMapping()
    public void postDevice(@RequestBody DeviceVo vo) {
        //TODO:
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDevice(@PathVariable String id) {
        // TODO:
    }

    @PutMapping(value="/{id}")
    public void putDevice(@PathVariable String id, @RequestBody DeviceVo vo) {
        // TODO:
    }

    @GetMapping(value = "/all")
    public List<DeviceVo> getDevice(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }
    
}