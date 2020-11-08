package apsh.backend.po;

import apsh.backend.dto.EquipmentDto;
import apsh.backend.enums.ShiftType;
import apsh.backend.enums.day;
import apsh.backend.vo.ProductInResourceUseVo;
import apsh.backend.vo.ResourceUseVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.lang.reflect.Field;
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

    @JoinColumn(name = "daily_schedule")
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

    public Equipment(Object o) throws NoSuchFieldException, IllegalAccessException {

        Field f = o.getClass().getDeclaredField("code");
        f.setAccessible(true);
        this.name = (String) f.get(o);

        f = o.getClass().getDeclaredField("count");
        f.setAccessible(true);
        this.count = (int) f.get(o);
        f = o.getClass().getDeclaredField("day");
        f.setAccessible(true);
        String[] result1 = ((String) f.get(o)).split("-");
        int start = day.intValue(result1[0]);
        int end = day.intValue(result1[1]);
        StringBuilder Schedule = new StringBuilder("1");
        for (int i = start + 1; i <= end; i++) {
            Schedule.append(",").append(i);
        }

        this.weeklySchedule = Schedule.toString();

        f = o.getClass().getDeclaredField("shift");
        f.setAccessible(true);
        this.dailySchedule = ShiftType.valueOf(0).getShift((String) f.get(o));
    }
}

