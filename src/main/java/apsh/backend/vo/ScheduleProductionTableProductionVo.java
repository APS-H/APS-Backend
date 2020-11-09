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
    Integer id;
    String orderNo;
    Boolean isSplit;              // 原则上，schedules长度为1 等价于 isSplit为true
    Integer productNum;              // 该订单产品总数
    Date startTime;      // 该订单开始时间
    Date endTime;
    List<TaskInScheduleProductionTableProductionVo> tasks;

    public ScheduleProductionTableProductionVo(Integer id, String orderNo, Integer productNum) {
        this.id = id;
        this.orderNo = orderNo;
        this.productNum = productNum;
    }
}