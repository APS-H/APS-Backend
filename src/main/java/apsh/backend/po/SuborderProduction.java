package apsh.backend.po;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import apsh.backend.po.converter.StringListConverter;
import apsh.backend.vo.ResourceInScheduleProductionResourceTableProductionVo;
import apsh.backend.vo.ScheduleProductionResourceTableProductionVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "suborder_production")
public class SuborderProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "suborder_id")
    private String suborderId;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "manpower_ids")
    @Convert(converter = StringListConverter.class)
    private List<String> manpowerIds;

    @Column(name = "device_id")
    private String deviceId;

    public long getWorkTime() {
        return ChronoUnit.MINUTES.between(endTime.toLocalDateTime(), startTime.toLocalDateTime());
    };

    public ScheduleProductionResourceTableProductionVo getgetScheduleProductionResourceTableProductionVoS(){
        List<ResourceInScheduleProductionResourceTableProductionVo> resources=new ArrayList<>();
        for(String manid:manpowerIds){
            ResourceInScheduleProductionResourceTableProductionVo resource=new ResourceInScheduleProductionResourceTableProductionVo(manid,0,1);
            resources.add(resource);
        }
        ResourceInScheduleProductionResourceTableProductionVo dresource=new ResourceInScheduleProductionResourceTableProductionVo(deviceId,1,1);
        resources.add(dresource);
        ScheduleProductionResourceTableProductionVo res=new ScheduleProductionResourceTableProductionVo(suborderId,resources);
        return res;

    }

}