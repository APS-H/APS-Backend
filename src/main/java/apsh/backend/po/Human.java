package apsh.backend.po;

import apsh.backend.vo.ProductInResourceUseVo;
import apsh.backend.vo.ResourceUseVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "human_resources")
public class Human {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_size")
    private Integer groupSize;

    @Column(name = "weekly_schedule")
    private String weeklySchedule;

    @Column(name = "daily_schedule")
    @OneToOne(targetEntity = Shift.class)
    private Shift dailySchedule;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public ResourceUseVo toResourceUseVo(){
        List<ProductInResourceUseVo> usedTimeList = new ArrayList<ProductInResourceUseVo>();
        ResourceUseVo RUVO = new ResourceUseVo(id, groupName, 0, usedTimeList);
        return RUVO;
    }
}
