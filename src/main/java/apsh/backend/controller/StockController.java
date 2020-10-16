package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "stock")
public class StockController {
    @GetMapping(value = "/number/all")
    public List<String> getStockNumber() {
        // TODO:
        return null;
    }
    
}