package apsh.backend.serviceImpl;

import apsh.backend.repository.OrderRepository;
import apsh.backend.service.OrderService;
import apsh.backend.vo.OrderProgressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderProgressVo getOrderProgress(Date date) {


        return null;
    }
}
