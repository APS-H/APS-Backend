package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInScheduleProductionTableProductionVo {
    String id;
    Integer task;
    Date startTime;  // 该子订单开始时间
    Date endTime;
}