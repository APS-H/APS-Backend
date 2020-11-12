package apsh.backend.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleProductionTableProductionVo {
    String productionNo;
    Integer id;
    List<TaskInScheduleProductionTableProductionVo> tasks;


}