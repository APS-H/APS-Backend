package apsh.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}