package apsh.backend.dto;

import apsh.backend.po.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

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
    private TimeSectionDto workSection;

    public ManpowerDto(HumanDto humanDto) {
        this.id = humanDto.getTeamName();
        this.peopleCount = humanDto.getCount();
        // TODO : get shift from database
        // System.out.println(humanDto);
        // Shift shift = shiftService.getShift(humanDto.getShift().intValue() + 1);
        Shift shift = humanDto.getShift().getShift();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(shift.getEndTime());
        int startHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(shift.getStartTime());
        int endHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int lastTime = endHourOfDay - startHourOfDay;
        if (lastTime < 0)
            lastTime += 24;
        this.workSection = new TimeSectionDto(startHourOfDay, lastTime);
    }
}
