package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.StockVo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "stock")
public class StockController {
    @GetMapping(value = "/all")
    public List<StockVo> getStock() {
        // TODO:
        return null;
    }
    
}