package apsh.backend.serviceimpl;

import apsh.backend.dto.CustomerOrderDto;
import apsh.backend.enums.OrderStatus;
import apsh.backend.po.Order;
import apsh.backend.po.OrderProduction;
import apsh.backend.po.SuborderProduction;
import apsh.backend.repository.OrderProductionRepository;
import apsh.backend.repository.OrderRepository;
import apsh.backend.service.LegacySystemService;
import apsh.backend.service.OrderService;
import apsh.backend.service.TimeService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import apsh.backend.vo.OrderProgressVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductionRepository orderProductionRepository;
    private final TimeService timeService;
    private final LegacySystemService legacySystemService;
    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderServiceImpl.class));

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderProductionRepository orderProductionRepository,
            TimeService timeService,
            LegacySystemService legacySystemService
    ) {
        this.orderRepository = orderRepository;
        this.orderProductionRepository = orderProductionRepository;
        this.timeService = timeService;
        this.legacySystemService = legacySystemService;
    }

    @Override
    public List<CustomerOrderDto> getAll(Integer pageSize, Integer pageNum) {
        List<Order> legacySystemAllOrders = legacySystemService.getAllOrders();
        List<Order> incrementOrders = this.orderRepository.findAll();
        Map<Integer, Instant> orderStatusList = getOrderStatusList();
        Instant now = timeService.now().toInstant(ZoneOffset.UTC);
        List<CustomerOrderDto> mergedOrders = merge(legacySystemAllOrders, incrementOrders).parallelStream()
                .map(o -> {
                    CustomerOrderDto customerOrderDto = new CustomerOrderDto(o);
                    Instant deliveryDate = o.getDeliveryDate().toInstant();
                    Instant orderEndTime = orderStatusList.get(o.getId());
                    if (deliveryDate.isAfter(orderEndTime)) {   // 交付时间 晚于 订单完成时间
                        if (orderEndTime.isBefore(now)) {   // 订单完成时间 早于 当前时间
                            customerOrderDto.setState(OrderStatus.DELIVERED_ON_TIME);
                        } else {
                            customerOrderDto.setState(OrderStatus.ON_GOING);
                        }
                    } else { // 交付时间 早于 订单完成时间
                        if (orderEndTime.isBefore(now)) {   // 订单完成时间 早于 当前时间
                            customerOrderDto.setState(OrderStatus.DELAY_PRODUCTION);
                        } else {
                            customerOrderDto.setState(OrderStatus.DELIVERED_DELAY);
                        }
                    }
                    return customerOrderDto;
                }).sorted(((o1, o2) -> o1.getOrderId() < o2.getOrderId() ? 1 : 0)).collect(Collectors.toList());
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        return mergedOrders.subList(start, end);
    }

    private Map<Integer, Instant> getOrderStatusList() {
        return orderProductionRepository.findAll().parallelStream()
                .collect(Collectors.toMap(
                        op -> Integer.parseInt(op.getOrderId()),
                        op -> {
                            assert op.getSuborderProductions() != null && op.getSuborderProductions().size() != 0;
                            return op.getSuborderProductions().parallelStream()
                                    .map(SuborderProduction::getEndTime)
                                    .min(Comparator.naturalOrder())
                                    .get();
                        })
                );
    }

    private List<Order> merge(List<Order> legacySystemAllOrders, List<Order> incrementOrders) {
        Map<Integer, Order> mergedOrders = legacySystemAllOrders.parallelStream()
                // 解决这傻逼的数据 - 只取第一个：
                // 764104	3211498	4000	2018/11/19
                // 764104	3211498	1900	2018/11/20
                .collect(Collectors.groupingBy(Order::getId))
                .entrySet().parallelStream()
                .map(e -> e.getValue().get(0))
                .collect(Collectors.toMap(Order::getId, o -> o));
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
