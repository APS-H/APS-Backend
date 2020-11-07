package apsh.backend.dto;

import apsh.backend.enums.ShiftType;
import apsh.backend.service.ShiftService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ManpowerDto {

    @Autowired
    private ShiftService shiftService;

    private String id;
    /**
     * 小组人数
     */
    private Integer peopleCount;
    /**
     * 一天之内的所有工作时间段
     */
    private List<TimeSectionDto> workSections;

    public ManpowerDto(String id, Integer peopleCount, List<TimeSectionDto> workSections) {
        this.id = id;
        this.peopleCount = peopleCount;
        this.workSections = workSections;
    }

    public ManpowerDto(HumanDto humanDto) {
        this.id = humanDto.getName();
        this.peopleCount = humanDto.getCount();
        this.workSections = Collections.singletonList(
                new TimeSectionDto(shiftService.getShift(humanDto.getShift().value())));
    }
}
