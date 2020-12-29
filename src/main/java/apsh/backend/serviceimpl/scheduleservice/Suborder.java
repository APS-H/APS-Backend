package apsh.backend.serviceimpl.scheduleservice;

import java.util.HashSet;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@PlanningEntity
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
    private HashSet<String> availableManpowerIdSet;
    @NonNull
    private HashSet<String> availableDeviceTypeIdSet;
    @NonNull
    private Integer deadlineTimeGrainIndex;

    @PlanningVariable(valueRangeProviderRefs = "manpowerCombinationRange")
    private ManpowerCombination manpowerCombination;

    @PlanningVariable(valueRangeProviderRefs = "deviceRange")
    private Device device;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    private TimeGrain timeGrain;

    public int timeGrainDiff(Suborder b) {
        if (timeGrain == null || b.timeGrain == null)
            return 0;
        return Math.abs(timeGrain.getIndex() - b.timeGrain.getIndex());
    }

    public List<String> getManpowerIds() {
        return manpowerCombination.getIds();
    }

    // 人力资源的人员总数
    public int manpowerPeopleCount() {
        return manpowerCombination.getPeopleCount();
    }

    // 人力资源不可用的总数乘二 因为人力资源可用的优先级高于人数
    public int manpowerNotAvailableCountMul2() {
        int count = 0;
        if (manpowerCombination.getA() != null && !availableManpowerIdSet.contains(manpowerCombination.getA().getId()))
            count++;
        if (manpowerCombination.getB() != null && !availableManpowerIdSet.contains(manpowerCombination.getB().getId()))
            count++;
        return count * 2;
    }

    // 人力资源不能工作
    public boolean manpowerCannotWork() {
        if (timeGrain == null)
            return false;
        if (manpowerCombination == null)
            return false;
        return manpowerCombination.canWork(timeGrain.getHourOfDay(), needTimeInHour);
    }

    // 设备不可用
    public boolean deviceNotAvailable() {
        if (device == null)
            return false;
        return !availableDeviceTypeIdSet.contains(device.getDeviceTypeId());
    }

    // 两个订单使用了相同的人力资源
    public boolean manpowerCross(Suborder other) {
        return manpowerCombination.cross(other.manpowerCombination);
    }

    public boolean delay() {
        return timeGrain != null && timeGrain.getIndex() >= deadlineTimeGrainIndex;
    }

    public static Suborder create(Order order, int index, boolean urgent, int needTimeInHour,
            int deadlineTimeGrainIndex) {
        return new Suborder(order.getId() + " " + index, order.getId(), urgent, needTimeInHour,
                order.getNeedPeopleCount(), order.getAvailableManpowerIdSet(), order.getAvailableDeviceTypeIdSet(),
                deadlineTimeGrainIndex);
    }
}