package apsh.backend.serviceImpl;

import apsh.backend.dto.CustomerOrderDto;
import apsh.backend.po.Order;
import apsh.backend.repository.OrderRepository;
import apsh.backend.service.LegacySystemService;
import apsh.backend.service.OrderService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import apsh.backend.vo.OrderProgressVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final LegacySystemService legacySystemService;
    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderServiceImpl.class));

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, LegacySystemService legacySystemService) {
        this.orderRepository = orderRepository;
        this.legacySystemService = legacySystemService;
    }

    @Override
    public List<CustomerOrderDto> getAll(Integer pageSize, Integer pageNum) {
        List<Order> legacySystemAllOrders = legacySystemService.getAllOrders();
        List<Order> incrementOrders = this.orderRepository.findAll();
        List<CustomerOrderDto> mergedOrders = merge(legacySystemAllOrders, incrementOrders).parallelStream()
                .map(o -> {
                    CustomerOrderDto customerOrderDto = new CustomerOrderDto(o);
                    // TODO: orderDto.setState();
                    return customerOrderDto;
                }).sorted(((o1, o2) -> o1.getOrderId() < o2.getOrderId() ? 1 : 0)).collect(Collectors.toList());
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        return mergedOrders.subList(start, end);
    }

    private List<Order> merge(List<Order> legacySystemAllOrders, List<Order> incrementOrders) {
        Map<Integer, Order> mergedOrders = legacySystemAllOrders.parallelStream().collect(Collectors.toMap(Order::getId, o -> o));
        incrementOrders.forEach(o -> {
            if (o.getIsDeleted() == Order.DELETED) {
                mergedOrders.remove(o.getId());
            } else {
                mergedOrders.put(o.getId(), o);
            }
        });
        return mergedOrders.values().parallelStream().collect(Collectors.toList());
    }

    @Override

    public OrderProgressVo getOrderProgress(Date date) {


        return null;
    }

    public void add(CustomerOrderDto order) {
        logger.infoService("add", order);
        Order o = new Order(order);
        o.setIsDeleted(Order.NOT_DELETED);
        this.orderRepository.saveAndFlush(o);
    }

    @Override
    public void update(CustomerOrderDto order) {
        logger.infoService("update", order);
        Optional<Order> orderOptional = this.orderRepository.findById(order.getOrderId());
        // 订单编号不存在，直接添加订单记录，删除标志置为0
        // 订单编号存在且逻辑删除标志为1，修改失败
        // 否则，更新该条订单记录，删除标志置为0
        if (orderOptional.isPresent() && orderOptional.get().getIsDeleted() == Order.DELETED) {
            logger.errorService("update", order, "order " + order.getOrderId() + " has already been deleted! update fails");
        } else {
            Order o = new Order(order);
            o.setIsDeleted(Order.NOT_DELETED);
            this.orderRepository.saveAndFlush(o);
        }
    }

    @Override
    public void delete(String id) {
        logger.infoService("delete", id);
        // 订单编号不存在，直接添加订单记录，删除标志置为1
        // 否则，将订单编号对应的订单记录的删除标志置1
        Optional<Order> orderOptional = this.orderRepository.findById(Integer.parseInt(id));
        Order o = orderOptional.orElseGet(Order::new);
        o.setIsDeleted(Order.DELETED);
        this.orderRepository.saveAndFlush(o);
    }
}
