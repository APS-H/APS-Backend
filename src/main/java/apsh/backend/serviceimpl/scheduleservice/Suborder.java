package apsh.backend.serviceimpl.scheduleservice;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@PlanningEntity
@ToString
public class Suborder {
    @NonNull
    private String id;
    @NonNull
    private String orderId;
    @NonNull
    private Boolean urgent;
    @NonNull
    private Integer needTimeInHour;
    @NonNull
    private Integer needPeopleCount;
    @NonNull
    private List<String> availableManpowerIdList;
    @NonNull
    private List<String> availableDeviceTypeIdList;
    @NonNull
    private Integer deadlineTimeGrainIndex;

    @PlanningVariable(valueRangeProviderRefs = "manpowerRange")
    private Manpower manpowerA;

    @PlanningVariable(valueRangeProviderRefs = "manpowerRange", nullable = true)
    private Manpower manpowerB;

    @PlanningVariable(valueRangeProviderRefs = "manpowerRange", nullable = true)
    private Manpower manpowerC;

    @PlanningVariable(valueRangeProviderRefs = "deviceRange")
    private Device device;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    private TimeGrain timeGrain;

    public List<String> getManpowerIds() {
        List<String> res = new ArrayList<String>(3);
        if (manpowerA != null)
            res.add(manpowerA.getId());
        if (manpowerB != null)
            res.add(manpowerB.getId());
        if (manpowerC != null)
            res.add(manpowerC.getId());
        return res;
    }

    public static Suborder create(Order order, int index, boolean urgent, int needTimeInHour,
            int deadlineTimeGrainIndex) {
        return new Suborder(order.getId() + " " + index, order.getId(), urgent, needTimeInHour,
                order.getNeedPeopleCount(), order.getAvailableManpowerIdList(), order.getAvailableDeviceTypeIdList(),
                deadlineTimeGrainIndex);
    }
}