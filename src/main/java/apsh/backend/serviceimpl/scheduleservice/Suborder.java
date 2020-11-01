package apsh.backend.serviceimpl.scheduleservice;

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
    private Manpower manpower;

    @PlanningVariable(valueRangeProviderRefs = "deviceRange")
    private Device device;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    private TimeGrain timeGrain;

    public static Suborder create(Order order, int index, int needTimeInHour, int deadlineTimeGrainIndex) {
        return new Suborder(order.getId() + " " + index, order.getId(), needTimeInHour, order.getNeedPeopleCount(),
                order.getAvailableManpowerIdList(), order.getAvailableDeviceTypeIdList(), deadlineTimeGrainIndex);
    }
}