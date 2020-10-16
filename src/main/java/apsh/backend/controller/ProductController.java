package apsh.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.ProductRouteOrderVo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    /**
     * 产品路线
     */
    @GetMapping(value="/{id}/route")
    public List<ProductRouteOrderVo> getProductRoute(@PathVariable String id) {
        // TODO:
        return null;
    }
    
}