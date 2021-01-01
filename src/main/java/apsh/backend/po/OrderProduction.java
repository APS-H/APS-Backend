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
import javax.persistence.criteria.CriteriaBuilder;

import apsh.backend.serviceimpl.scheduleservice.Suborder;
import apsh.backend.vo.*;
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
            Date date1 = new Date(s.getStartTime().getTime());
            return isSameDay(date, date1);
        }).collect(Collectors.toList());

        return origin;
    }

    public OrderInOrderProgressVo getOrderInOrderProgress(Date date, Date deliveryDate) {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin = origin.stream().sorted((t1, t2) -> {
            Date date1 = new Date(t1.getEndTime().getTime());
            Date date2 = new Date(t2.getEndTime().getTime());
            if (date1.compareTo(date2) >= 0) {
                return 1;

            } else {
                return -1;
            }
        }).collect(Collectors.toList());
        Date EndDate = new Date(origin.get(origin.size()-1).getEndTime().getTime());
        long total = origin.stream().map(SuborderProduction::getWorkTime).collect(Collectors.toList()).stream()
                .mapToLong(o -> o).sum();

        long work = origin.stream().filter(s -> {
            // Date date1 = new Date(s.getStartTime().getTime());
            Date date2 = new Date(s.getEndTime().getTime());
            return date.compareTo(date2) >= 0;
        }).collect(Collectors.toList()).stream().mapToLong(o -> o.getWorkTime()).sum();

        Double rate = ((double) work) / total;

        OrderInOrderProgressVo OIPVO = new OrderInOrderProgressVo(String.valueOf(id), rate, 1.0, (deliveryDate.compareTo(EndDate)<0));
        return OIPVO;

    }

    public Date getStartTime() {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin = origin.stream().sorted((t1, t2) -> {
            Date date1 = new Date(t1.getStartTime().getTime());
            Date date2 = new Date(t2.getStartTime().getTime());

            return date1.compareTo(date2);
        }).collect(Collectors.toList());

        return new Date(origin.get(0).getStartTime().getTime());
    }

    public Date getEndTime() {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        origin = origin.stream().sorted((t1, t2) -> {
            Date date1 = new Date(t1.getStartTime().getTime());
            Date date2 = new Date(t2.getStartTime().getTime());

            return date1.compareTo(date2);
        }).collect(Collectors.toList());

        return new Date(origin.get(origin.size() - 1).getEndTime().getTime());
    }

    public ScheduleInSchedulePlanTableOrderVo getScheduleInSchedulePlanTableOrderVo() {
        ScheduleInSchedulePlanTableOrderVo s = new ScheduleInSchedulePlanTableOrderVo(id, 100, getStartTime(),
                getEndTime());
        return s;
    }

    public List<ScheduleOrderProductionTableRelationVo> getScheduleOrderProductionTableRelationVoS() {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);
        List<ScheduleOrderProductionTableRelationVo> SOPTRVOS = origin.stream().map(o -> {
            ScheduleOrderProductionTableRelationVo s = new ScheduleOrderProductionTableRelationVo(orderId, true,
                    o.getSuborderId());
            return s;
        }).collect(Collectors.toList());

        return SOPTRVOS;
    }


    public List<ScheduleProductionTableProductionVo> getScheduleProductionTableProductionVo(Integer stock_id) {
        List<ScheduleProductionTableProductionVo> s = new ArrayList<ScheduleProductionTableProductionVo>();
        for (SuborderProduction i : suborderProductions) {
            Date date1 = new Date(i.getStartTime().getTime());
            Date date2 = new Date(i.getEndTime().getTime());
            TaskInScheduleProductionTableProductionVo task = new TaskInScheduleProductionTableProductionVo(i.getSuborderId(), stock_id, date1, date2);
            List<TaskInScheduleProductionTableProductionVo> tasks = new ArrayList<>();
            tasks.add(task);
            ScheduleProductionTableProductionVo m = new ScheduleProductionTableProductionVo(i.getSuborderId(), i.getId(), tasks);
            s.add(m);
        }
        return s;
    }


    public List<ScheduleProductionResourceTableProductionVo> getScheduleProductionResourceTableProductionVoS(){
        List<ScheduleProductionResourceTableProductionVo> res=new ArrayList<>();
        for(SuborderProduction i:suborderProductions){
            res.add(i.getgetScheduleProductionResourceTableProductionVoS());
        }
        return  res;
    }


    private static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }
}
