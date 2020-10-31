package apsh.backend.po;

import apsh.backend.dto.EquipmentDto;
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
@Entity(name = "equipment_resources")
public class Equipment {

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private Integer count;

    @Column(name = "weekly_schedule")
    private String weeklySchedule;

    @Column(name = "daily_schedule")
    @OneToOne(targetEntity = Shift.class)
    private Shift dailySchedule;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public Equipment(EquipmentDto equipmentDto) {
        this.id = equipmentDto.getDeviceId();
        this.name = equipmentDto.getName();
        this.count = equipmentDto.getCount();
        this.weeklySchedule = equipmentDto.getWorkDay().parallelStream()
                .map(String::valueOf)
                .reduce((e1, e2) -> e1 + "," + e2)
                .orElse("");
    }


    public ResourceUseVo toResourceUseVo() {
        List<ProductInResourceUseVo> usedTimeList = new ArrayList<ProductInResourceUseVo>();
        ResourceUseVo RUVO = new ResourceUseVo(id, name, 1, usedTimeList);
        return RUVO;
    }
}
