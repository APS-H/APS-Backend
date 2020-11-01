package apsh.backend.serviceimpl.scheduleservice;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Manpower {
    @NonNull
    private String id;
    @NonNull
    private Integer peopleCount;
    @NonNull
    private List<TimeSection> workSections;

    public boolean canWork(int startHourOfDay, int workTimeInHour) {
        while (startHourOfDay + workTimeInHour > 24) {
            TimeSection section = new TimeSection(startHourOfDay, 24);
            if (!canWork(section))
                return false;
            workTimeInHour -= 24 - startHourOfDay;
            startHourOfDay = 0;
        }
        if (workTimeInHour == 0)
            return true;
        return canWork(new TimeSection(startHourOfDay, startHourOfDay + workTimeInHour));
    }

    public boolean canWork(TimeSection section) {
        for (TimeSection workSection : workSections)
            if (workSection.cover(section))
                return true;
        return false;
    }
}