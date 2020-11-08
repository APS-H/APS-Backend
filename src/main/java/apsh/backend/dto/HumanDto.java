package apsh.backend.dto;

import apsh.backend.enums.ShiftType;
import apsh.backend.po.Human;
import apsh.backend.vo.HumanVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HumanDto {

    private Integer humanId;
    private String name;
    private String teamName;
    private Integer count;
    private ShiftType shift;
    List<Integer> workDay;

    public HumanDto(HumanVo vo) {
        this.humanId = Integer.valueOf(vo.getHumanId());
        this.name = vo.getName();
        this.teamName = vo.getTeamName();
        this.count = vo.getCount();
        this.shift = ShiftType.valueOf(vo.getShift());
        this.workDay = new ArrayList<>(workDay);
    }

    public HumanDto(Human human) {
        this.humanId = human.getId();
        this.teamName = human.getGroupName();
        this.count = human.getGroupSize();
        String value = human.getDailySchedule().getName().trim();
        if ("早班".equals(value)) {
            this.shift = ShiftType.DAY_SHIFT;
        } else if ("晚班".equals(value)) {
            this.shift = ShiftType.NIGHT_SHIFT;
        } else {
            this.shift = ShiftType.ALL_DAY_SHIFT;
        }
//        this.shift = ShiftType.valueOf(human.getDailySchedule().getName().trim());
        this.workDay = Arrays.stream(human.getWeeklySchedule().split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
