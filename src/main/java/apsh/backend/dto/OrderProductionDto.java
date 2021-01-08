package apsh.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单被划分为多个子订单 每个子订单都占用一段连续的时间进行生产
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductionDto {
    /**
     * 订单id
     */
    private String id;
    /**
     * 前驱订单的id
     */
    private String predecessorOrderId;
    private List<SuborderProductionDto> suborders;
}