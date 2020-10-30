package apsh.backend.vo;

import java.util.Date;

import apsh.backend.dto.OrderDto;
import apsh.backend.po.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderVo {
    String orderId;
    String stockId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Date dayOfDelivery;
    Integer state;
    Integer orderAmount;

    public OrderVo(OrderDto orderDto) {
        this.orderId = String.valueOf(orderDto.getOrderId());
        this.stockId = String.valueOf(orderDto.getProductId());
        this.dayOfDelivery = orderDto.getDayOfDelivery();
        this.state = orderDto.getState().value();
        this.orderAmount = orderDto.getProductCount();
    }
}