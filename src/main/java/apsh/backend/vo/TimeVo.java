package apsh.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeVo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Date startTime;
    Double flowFactor;
}