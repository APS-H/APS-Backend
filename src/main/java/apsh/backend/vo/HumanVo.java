package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HumanVo {
    String manpowerId;
    String name;
    String teamName;
    Integer count;
    Integer shift;
    List<Integer> workDay;
}