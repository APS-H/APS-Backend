package apsh.backend.serviceimpl.scheduleservice;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@PlanningEntity
public class Suborder {
    @NonNull
    private String id;
    @NonNull
    private String orderId;
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

    public static Suborder create(Order order, int index, int needTimeInHour, int deadlineTimeGrainIndex) {
        return new Suborder(order.getId() + " " + index, order.getId(), needTimeInHour, order.getNeedPeopleCount(),
                order.getAvailableManpowerIdList(), order.getAvailableDeviceTypeIdList(), deadlineTimeGrainIndex);
    }
}