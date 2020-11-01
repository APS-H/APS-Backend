package apsh.backend.vo;

import java.util.ArrayList;
import java.util.List;

import apsh.backend.dto.HumanDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HumanVo {
    String humanId;
    String name;
    String teamName;
    Integer count;
    Integer shift;
    List<Integer> workDay;

    public HumanVo(HumanDto humanDto) {
        this.humanId = String.valueOf(humanDto.getHumanId());
        this.name = humanDto.getName();
        this.teamName = humanDto.getTeamName();
        this.count = humanDto.getCount();
        this.shift = humanDto.getShift().intValue();
        this.workDay = new ArrayList<>(humanDto.getWorkDay());
    }
}