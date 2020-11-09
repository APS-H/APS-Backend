package apsh.backend.po;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import apsh.backend.vo.OrderInOrderProgressVo;
import apsh.backend.vo.ScheduleInSchedulePlanTableOrderVo;
import apsh.backend.vo.ScheduleOrderProductionTableRelationVo;
import apsh.backend.vo.ScheduleProductionTableProductionVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_production")
public class OrderProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @JoinColumn(name = "order_production_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuborderProduction> suborderProductions;

    public List<SuborderProduction> getSuborderProductionsByDate(Date date) {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);


        origin = origin.stream().filter(s -> {
            Date date1 = Date.from(s.getStartTime());


            return DateUtils.isSameDay(date, date1);
        }).collect(Collectors.toList());

        return origin;
    }

    public OrderInOrderProgressVo getOrderInOrderProgress(Date date) {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin = origin.stream().sorted((t1, t2) -> {
            if (t2.getStartTime().isAfter(t1.getStartTime())) {
                return 1;

            } else {
                return 0;
            }
        }).collect(Collectors.toList());

        long total = origin.stream().map(SuborderProduction::getWorkTime).collect(Collectors.toList()).stream().mapToLong(o -> o).sum();

        long work = origin.stream().filter(s -> {
            //Date date1 = Date.from(s.getStartTime());
            Date date2 = Date.from(s.getEndTime());
            return date.compareTo(date2) >= 0;
        }).collect(Collectors.toList())
                .stream().mapToLong(o -> o.getWorkTime()).sum();

        Double rate = ((double) work) / total;
        OrderInOrderProgressVo OIPVO = new OrderInOrderProgressVo(String.valueOf(id), rate, 1.0, false);
        return OIPVO;

    }

    public Date getStartTime(){
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin=origin.stream().sorted((t1,t2)->{
            Date date1 = Date.from(t1.getStartTime());
            Date date2 = Date.from(t2.getStartTime());

            return date1.compareTo(date2);
        }).collect(Collectors.toList());

        return Date.from(origin.get(0).getStartTime());
    }

    public Date getEndTime(){
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin=origin.stream().sorted((t1,t2)->{
            Date date1 = Date.from(t1.getStartTime());
            Date date2 = Date.from(t2.getStartTime());

            return date1.compareTo(date2);
        }).collect(Collectors.toList());

        return Date.from(origin.get(origin.size()-1).getEndTime());
    }

    public ScheduleInSchedulePlanTableOrderVo getScheduleInSchedulePlanTableOrderVo(){
        ScheduleInSchedulePlanTableOrderVo s=new ScheduleInSchedulePlanTableOrderVo(id,100,getStartTime(),getEndTime());
        return s;
    }


    public List<ScheduleOrderProductionTableRelationVo> getScheduleOrderProductionTableRelationVoS(){
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        List<ScheduleOrderProductionTableRelationVo> SOPTRVOS=origin.stream().map(o->{
            ScheduleOrderProductionTableRelationVo s=new ScheduleOrderProductionTableRelationVo(orderId,true,o.getSuborderId());
            return s;
        }).collect(Collectors.toList());

        return SOPTRVOS;
    }
}



