package apsh.backend.serviceimpl;

import apsh.backend.dto.CustomerOrderDto;
import apsh.backend.dto.DeviceDto;
import apsh.backend.dto.ManpowerDto;
import apsh.backend.dto.OrderDto;
import apsh.backend.enums.OrderStatus;
import apsh.backend.po.Craft;
import apsh.backend.po.Order;
import apsh.backend.po.OrderProduction;
import apsh.backend.po.SuborderProduction;
import apsh.backend.repository.OrderProductionRepository;
import apsh.backend.repository.OrderRepository;
import apsh.backend.service.*;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import apsh.backend.vo.OrderInOrderProgressVo;
import apsh.backend.vo.OrderProgressVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderServiceImpl implements OrderService, GodService {

    private final ScheduleService scheduleService;
    private final LegacySystemService legacySystemService;
    private final HumanService humanService;
    private final EquipmentService equipmentService;
    private final TimeService timeService;

    private final OrderRepository orderRepository;
    private final OrderProductionRepository orderProductionRepository;

    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(OrderServiceImpl.class));

    @Autowired
    public OrderServiceImpl(ScheduleService scheduleService, HumanService humanService,
                            EquipmentService equipmentService, TimeService timeService, LegacySystemService legacySystemService,
                            OrderRepository orderRepository, OrderProductionRepository orderProductionRepository) {
        this.scheduleService = scheduleService;
        this.humanService = humanService;
        this.equipmentService = equipmentService;
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
        List<CustomerOrderDto> mergedOrders = merge(legacySystemAllOrders, incrementOrders).parallelStream().map(o -> {
            CustomerOrderDto customerOrderDto = new CustomerOrderDto(o);
            if (orderStatusList == null || orderStatusList.size() == 0) {
                // 排程算法还未开始计算或者尚未计算出结果，此时无法获取订单状态
                return customerOrderDto;
            }
            Instant deliveryDate = new Date(o.getDeliveryDate().getTime()).toInstant();
            Instant orderEndTime = orderStatusList.get(o.getId());
            if (orderEndTime != null) {
                if (deliveryDate.isAfter(orderEndTime)) { // 交付时间 晚于 订单完成时间
                    if (orderEndTime.isBefore(now)) { // 订单完成时间 早于 当前时间
                        customerOrderDto.setState(OrderStatus.DELIVERED_ON_TIME);
                    } else {
                        customerOrderDto.setState(OrderStatus.ON_GOING);
                    }
                } else { // 交付时间 早于 订单完成时间
                    if (orderEndTime.isBefore(now)) { // 订单完成时间 早于 当前时间
                        customerOrderDto.setState(OrderStatus.DELAY_PRODUCTION);
                    } else {
                        customerOrderDto.setState(OrderStatus.DELIVERED_DELAY);
                    }
                }
            }
            return customerOrderDto;
        }).sorted(((o1, o2) -> o1.getOrderId() < o2.getOrderId() ? 1 : 0)).collect(Collectors.toList());
        int start = Math.max(0, pageSize * (pageNum - 1));
        int end = Math.min(mergedOrders.size(), pageSize * pageNum);
        return mergedOrders.subList(start, end);
    }

    private Map<Integer, Instant> getOrderStatusList() {
        return orderProductionRepository.findAll().parallelStream()
                .collect(Collectors.toMap(op -> Integer.parseInt(op.getOrderId()), op -> {
                    assert op.getSuborderProductions() != null && op.getSuborderProductions().size() != 0;
                    return op.getSuborderProductions().parallelStream().map(SuborderProduction::getEndTime)
                            .map(Timestamp::toInstant).min(Comparator.naturalOrder()).get();
                }));
    }

    private List<Order> merge(List<Order> legacySystemAllOrders, List<Order> incrementOrders) {
        Map<Integer, Order> mergedOrders = legacySystemAllOrders.parallelStream()
                // 解决这傻逼的数据 - 只取第一个：
                // 764104 3211498 4000 2018/11/19
                // 764104 3211498 1900 2018/11/20
                .collect(Collectors.groupingBy(Order::getId)).entrySet().parallelStream().map(e -> e.getValue().get(0))
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
        List<OrderProduction> orderProductions = orderProductionRepository.findAll();
        List<OrderInOrderProgressVo> progress = orderProductions.parallelStream().map(o -> {
            return o.getOrderInOrderProgress(date);
        }).collect(Collectors.toList());
        OptionalDouble totalrate = progress.stream().mapToDouble(o -> o.getAssembleRate()).average();
        OrderProgressVo OPVO = new OrderProgressVo(totalrate.getAsDouble(), progress);
        return OPVO;
    }

    public void add(CustomerOrderDto order) {
        logger.infoService("add", order);

        // 用于排程算法调用，不包含插入的新订单
        List<CustomerOrderDto> customerOrderDtos = this.getAll(Integer.MAX_VALUE, 1);

        // 持久化新插入的订单
        Order o = new Order(order);
        o.setIsDeleted(Order.NOT_DELETED);
        this.orderRepository.saveAndFlush(o);

        // 调用排程模块紧急插单，重新计算排程
        this.godBlessMeAgain(scheduleService, order);
    }

    @Override
    public void update(CustomerOrderDto order) {
        logger.infoService("update", order);
        Optional<Order> orderOptional = this.orderRepository.findById(order.getOrderId());
        // 订单编号不存在，直接添加订单记录，删除标志置为0
        // 订单编号存在且逻辑删除标志为1，修改失败
        // 否则，更新该条订单记录，删除标志置为0
        if (orderOptional.isPresent() && orderOptional.get().getIsDeleted() == Order.DELETED) {
            logger.errorService("update", order,
                    "order " + order.getOrderId() + " has already been deleted! update fails");
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

    @Override
    public Map<String, Craft> prepareCrafts() {
        return legacySystemService.getAllCrafts().parallelStream()
                .collect(Collectors.toMap(Craft::getProductionId, c -> c));
    }

    @Override
    public List<ManpowerDto> prepareManPowers() {
        return humanService.getAll(Integer.MAX_VALUE, 1).parallelStream().map(ManpowerDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDto> prepareDevices() {
        return equipmentService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                .flatMap(e -> IntStream.range(0, e.getCount()).mapToObj(id -> new DeviceDto(id, e)))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> prepareOrders() {
        Map<String, Craft> crafts = prepareCrafts();
        return getAll(Integer.MAX_VALUE, 1).parallelStream().map(o -> {
            Craft craft = crafts.get(String.valueOf(o.getProductId()));
            if (craft == null) {
                // 说明该订单生产的物料没有对应的工艺，直接去掉该订单
                return null;
            }
            return new OrderDto(o, craft);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Date schedulingInsertTime() {
        // 获取系统当前时间
        LocalDateTime now = timeService.now();
        // 插单时间
        return Date.from(LocalDateTime.of(now.toLocalDate(), LocalTime.of(now.getHour(), 0))
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Date schedulingStartTime() {
        // 获取设置的系统初始时间
        LocalDateTime start = timeService.getTime().getStartTime();
        // 排程起始时间
        return Date.from(LocalDateTime.of(start.toLocalDate(), LocalTime.of(start.getHour(), 0))
                .atZone(ZoneId.systemDefault()).toInstant());

    }
}
