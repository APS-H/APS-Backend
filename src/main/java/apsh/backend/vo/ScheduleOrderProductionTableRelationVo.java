package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleOrderProductionTableRelationVo {
    String orderId;
    Boolean isSplit;
    String productionId;
}