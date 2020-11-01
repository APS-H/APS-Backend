package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInOrderProgressVo {
    String orderId;
    Double assembleRate;
    Double testRate;
    Boolean postpone;
}