package apsh.backend.vo;

import apsh.backend.dto.SystemTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimeVo {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    LocalDateTime startTime;

    Double flowFactor;

    public TimeVo(SystemTime systemTime) {
        this.startTime = systemTime.getStartTime();
        this.flowFactor = systemTime.getTimeSpeed();
    }

}