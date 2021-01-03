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
    private TimeSection workSection;

    public boolean canWorkWith(Manpower other) {
        return workSection.equals(other.workSection);
    }

    public boolean canWork(int startHourOfDay, int workTimeInHour) {
        if (workSection.getStart() <= startHourOfDay
                && startHourOfDay + workTimeInHour <= workSection.getStart() + workSection.getLastTime())
            return true;
        startHourOfDay += 24;
        if (workSection.getStart() <= startHourOfDay
                && startHourOfDay + workTimeInHour <= workSection.getStart() + workSection.getLastTime())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "{ " + id.toString() + ", " + peopleCount.toString() + ", " + workSection.toString() + " }";
    }
}