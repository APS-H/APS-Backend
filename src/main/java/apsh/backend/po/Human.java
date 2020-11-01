package apsh.backend.po;

import apsh.backend.dto.HumanDto;
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

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;
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

    public Human(HumanDto humanDto) {
        this.id = humanDto.getHumanId();
        this.groupName = humanDto.getTeamName();
        this.groupSize = humanDto.getCount();
        this.weeklySchedule = humanDto.getWorkDay().parallelStream()
                .map(String::valueOf).reduce((h1, h2) -> h1 + "," + h2)
                .orElse("");
    }

    public ResourceUseVo toResourceUseVo() {
        List<ProductInResourceUseVo> usedTimeList = new ArrayList<ProductInResourceUseVo>();
        ResourceUseVo RUVO = new ResourceUseVo(String.valueOf(id), groupName, 0, usedTimeList);
        return RUVO;
    }

}
