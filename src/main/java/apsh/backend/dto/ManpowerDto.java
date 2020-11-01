package apsh.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManpowerDto {
    private String id;
    /**
     * 小组人数
     */
    private Integer peopleCount;
    /**
     * 一天之内的所有工作时间段
     */
    private List<TimeSectionDto> workSections;
}
