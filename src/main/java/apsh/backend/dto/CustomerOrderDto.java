package apsh.backend.dto;

import apsh.backend.enums.OrderStatus;
import apsh.backend.po.Order;
import apsh.backend.vo.OrderVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerOrderDto {

    private Integer orderId;
    private Integer productId;
    private Date dayOfDelivery;
    private OrderStatus state;
    private Integer productCount;

    public CustomerOrderDto(OrderVo vo) {
        this.orderId = Integer.valueOf(vo.getOrderId());
        this.productId = Integer.valueOf(vo.getStockId());
        this.dayOfDelivery = vo.getDayOfDelivery();
        this.state = OrderStatus.valueOf(vo.getState());
        this.productCount = vo.getOrderAmount();
    }

    public CustomerOrderDto(Order order) {
        this.orderId = order.getId();
        this.productId = order.getProductId();
        this.dayOfDelivery = new Date(order.getDeliveryDate().getTime());
        this.productCount = order.getProductCount();
    }
}
