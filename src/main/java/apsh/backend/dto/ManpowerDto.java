package apsh.backend.dto;

import apsh.backend.po.Shift;
import apsh.backend.service.ShiftService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public ManpowerDto(HumanDto humanDto) {
        System.out.println("humanDto: " + humanDto);
        this.id = humanDto.getTeamName();
        this.peopleCount = humanDto.getCount();
        // TODO : get shift from database
        // System.out.println(humanDto);
        // Shift shift = shiftService.getShift(humanDto.getShift().intValue() + 1);
        Shift shift = humanDto.getShift().getShift();
        if (shift.getStartTime().after(shift.getEndTime())) {
            // +1天的情况，例如从晚上19:00到第二天上午7:00
            Shift part1 = new Shift();
            part1.setStartTime(shift.getStartTime());
            part1.setEndTime(Time.valueOf(LocalTime.MAX));
            Shift part2 = new Shift();
            part2.setStartTime(Time.valueOf(LocalTime.MIN));
            part2.setEndTime(shift.getEndTime());
            this.workSections = Arrays.asList(
                    new TimeSectionDto(part1),
                    new TimeSectionDto(part2)
            );
        } else {
            this.workSections = Collections.singletonList(new TimeSectionDto(shift));
        }
    }
}
