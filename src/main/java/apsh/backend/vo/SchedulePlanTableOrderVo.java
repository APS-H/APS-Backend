package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePlanTableOrderVo {
    String orderId;
    Boolean isSplit;
    List<ScheduleInSchedulePlanTableOrderVo> schedules;
}