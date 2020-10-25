package apsh.backend.controller;

import apsh.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.OrderProgressVo;
import apsh.backend.vo.OrderVo;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public void postOrder(@RequestBody OrderVo vo) {
        // TODO:
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable String id) {
        // TODO:
    }

    @PutMapping(value = "/{id}")
    public void putOrder(@PathVariable String id, @RequestBody OrderVo vo) {
        // TODO:
    }

    @GetMapping(value = "/all")
    public List<OrderVo> getOrder(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

    /**
     * 获取订单进度
     *
     * @param date
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/progress/all")
    public OrderProgressVo getOrderProgress(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
                                            @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        // TODO:
        return null;
    }

}