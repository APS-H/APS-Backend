package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.ManpowerVo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping(value = "/manpower")
public class ManpowerController {
    @PostMapping()
    public void postManpower(@RequestBody ManpowerVo vo) {
        // TODO:
    }

    @DeleteMapping(value = "/{id}")
    public void deleteManpower(@PathVariable String id) {
        // TODO:
    }

    @PutMapping(value="/{id}")
    public void putManpower(@PathVariable String id, @RequestBody ManpowerVo vo) {
        //TODO:
    }

    @GetMapping(value = "/all")
    public List<ManpowerVo> getManpower(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }
}