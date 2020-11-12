package apsh.backend.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePlanTableOrderVo {
    Integer id;
    String orderNo;
    Boolean isSplit;              // 原则上，schedules长度为1 等价于 isSplit为true
    Integer productNum=0;              // 该订单产品总数
    Date startTime;      // 该订单开始时间
    Date endTime;
    List<ScheduleInSchedulePlanTableOrderVo> schedules;

    public SchedulePlanTableOrderVo(Integer id, String orderNo, Integer productNum) {
        this.id = id;
        this.orderNo = orderNo;
        this.productNum = productNum;
        this.schedules=new ArrayList<ScheduleInSchedulePlanTableOrderVo>();
    }

    public void addSchedule(ScheduleInSchedulePlanTableOrderVo newSchedule){
        schedules.add(newSchedule);
    }

    public void Caculate(){
        long total=0;
        for(ScheduleInSchedulePlanTableOrderVo i :schedules){
            total=total+i.getEndTime().getTime()-i.getStartTime().getTime();
        }
        for(ScheduleInSchedulePlanTableOrderVo i :schedules){
            double rate=((double)(i.getEndTime().getTime()-i.getStartTime().getTime()))/total;
            int num=(int)(productNum*rate);
            i.setProductNum(num);
        }
    }
}