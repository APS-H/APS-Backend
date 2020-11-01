package apsh.backend.dto;

import apsh.backend.vo.TimeVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SystemTime {

    private LocalDateTime startTime;

    private Double timeSpeed;

    public SystemTime(TimeVo timeVo) {
        this.startTime = timeVo.getStartTime();
        this.timeSpeed = timeVo.getFlowFactor();
    }

}
