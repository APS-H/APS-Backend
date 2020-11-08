package apsh.backend.vo;

import java.util.Date;

import apsh.backend.dto.CustomerOrderDto;
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

    public OrderVo(CustomerOrderDto customerOrderDto) {
        this.orderId = String.valueOf(customerOrderDto.getOrderId());
        this.stockId = String.valueOf(customerOrderDto.getProductId());
        this.dayOfDelivery = customerOrderDto.getDayOfDelivery();
        if (customerOrderDto.getState() != null)    // 调度算法可能还没计算出生产单
            this.state = customerOrderDto.getState().value();
        this.orderAmount = customerOrderDto.getProductCount();
    }
}