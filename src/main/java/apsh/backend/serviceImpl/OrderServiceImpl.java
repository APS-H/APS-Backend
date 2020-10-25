package apsh.backend.serviceImpl;

import apsh.backend.repository.OrderRepository;
import apsh.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
