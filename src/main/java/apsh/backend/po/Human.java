package apsh.backend.po;

import apsh.backend.dto.HumanDto;
import apsh.backend.enums.ShiftType;
import apsh.backend.enums.day;
import apsh.backend.service.ShiftService;
import apsh.backend.serviceimpl.ShiftServiceImpl;
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
@Entity(name = "human_resources")
public class Human {

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // 2组-丁梅（3）
    @Column(name = "group_name")
    private String groupName;

    // 3
    @Column(name = "group_size")
    private Integer groupSize;

    // 1,2,3,4,5 或者 1,2,3,4,5,6,7
    @Column(name = "weekly_schedule")
    private String weeklySchedule;

    // 早班、晚班、全天
    @JoinColumn(name = "daily_schedule")
    @OneToOne(targetEntity = Shift.class)
    private Shift dailySchedule;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public Human(Object o) throws NoSuchFieldException, IllegalAccessException {
        Field f;
        f = o.getClass().getDeclaredField("code");
        f.setAccessible(true);
        this.groupName = (String) f.get(o);


        f = o.getClass().getDeclaredField("count");
        f.setAccessible(true);
        this.groupSize = (Integer) f.get(o);

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

    public Human(HumanDto humanDto) {
        this.id = humanDto.getHumanId();
        this.groupName = humanDto.getTeamName();
        this.groupSize = humanDto.getCount();
        this.weeklySchedule = humanDto.getWorkDay().parallelStream()
                .map(String::valueOf).reduce((h1, h2) -> h1 + "," + h2)
                .orElse("");
    }


}
