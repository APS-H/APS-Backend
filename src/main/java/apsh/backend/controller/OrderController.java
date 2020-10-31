package apsh.backend.controller;

import apsh.backend.dto.OrderDto;
import apsh.backend.service.OrderService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apsh.backend.vo.OrderProgressVo;
import apsh.backend.vo.OrderVo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private final LogFormatter logger;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        this.logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderController.class));
    }

    @PostMapping()
    public void postOrder(@RequestBody OrderVo vo) {
        logger.infoControllerRequest("POST", "/order", vo);
        orderService.update(new OrderDto(vo));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable String id) {
        logger.infoControllerRequest("DELETE", "/order/" + id, new Object());
        orderService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public void putOrder(@PathVariable String id, @RequestBody OrderVo vo) {
        logger.infoControllerRequest("PUT", "/order/" + id, vo);
        orderService.add(new OrderDto(vo));
    }

    @GetMapping(value = "/all")
    public List<OrderVo> getOrder(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        logger.infoControllerRequest("GET", "/order/all", "pageSize=" + pageSize + ", pageNum=" + pageNum);
        List<OrderVo> orderVos = orderService.getAll(pageSize, pageNum).parallelStream()
                .map(OrderVo::new).collect(Collectors.toList());
        logger.infoControllerResponse("GET", "/order/all", orderVos);
        return orderVos;
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