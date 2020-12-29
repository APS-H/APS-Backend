package apsh.backend.serviceimpl.scheduleservice;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ManpowerCombination {
    @NonNull
    private Manpower a;
    private Manpower b;
    private List<String> ids;
    private int peopleCount;

    public ManpowerCombination(Manpower a, Manpower b) {
        this.a = a;
        this.b = b;

        ids = new ArrayList<>();
        ids.add(a.getId());
        if (b != null)
            ids.add(b.getId());

        peopleCount = 0;
        peopleCount += a.getPeopleCount();
        if (b != null)
            peopleCount += b.getPeopleCount();
    }

    public boolean canWork(int startHourOfDay, int workTimeInHour) {
        return a.canWork(startHourOfDay, workTimeInHour);
    }

    public boolean cross(ManpowerCombination other) {
        return ids.contains(other.a.getId()) || (other.b != null && ids.contains(other.b.getId()));
    }
}