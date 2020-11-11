package apsh.backend.dto;

import apsh.backend.po.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * 一天里的时间段 前闭后开
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSectionDto {
    /**
     * 取值范围 [0,24)
     */
    private Integer start;
    /**
     * 取值范围 (0, 24]
     */
    private Integer end;

    public TimeSectionDto(Shift shift) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(shift.getStartTime());
        this.start = calendar.get(Calendar.HOUR_OF_DAY);

        if (shift.getEndTime().equals(Time.valueOf(LocalTime.MAX))) {
            this.end = 24;
        } else {
            calendar.setTime(shift.getEndTime());
            this.end = calendar.get(Calendar.HOUR_OF_DAY);
        }
    }
}