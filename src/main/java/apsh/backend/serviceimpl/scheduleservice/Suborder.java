package apsh.backend.serviceimpl.scheduleservice;

import java.util.ArrayList;
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

    @PlanningVariable(valueRangeProviderRefs = "manpowerRange")
    private Manpower manpowerA;

    @PlanningVariable(valueRangeProviderRefs = "manpowerRange", nullable = true)
    private Manpower manpowerB;

    // @PlanningVariable(valueRangeProviderRefs = "manpowerRange", nullable = true)
    private Manpower manpowerC;

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
        List<String> res = new ArrayList<String>(3);
        if (manpowerA != null)
            res.add(manpowerA.getId());
        if (manpowerB != null)
            res.add(manpowerB.getId());
        if (manpowerC != null)
            res.add(manpowerC.getId());
        return res;
    }

    // 人力资源的人员总数
    public int manpowerPeopleCount() {
        int res = 0;
        if (manpowerA != null)
            res += manpowerA.getPeopleCount();
        if (manpowerB != null)
            res += manpowerB.getPeopleCount();
        if (manpowerC != null)
            res += manpowerC.getPeopleCount();
        return res;
    }

    // 人力资源重复使用
    public int manpowerOverlapCount() {
        int res = 0;
        if (manpowerB != null && manpowerB == manpowerA)
            res++;
        if (manpowerC != null && manpowerC == manpowerA)
            res++;
        if (manpowerC != null && manpowerC == manpowerB)
            res++;
        return res;
    }

    // 人力资源不可用的总数乘二 因为人力资源可用的优先级高于人数
    public int manpowerNotAvailableCountMul2() {
        int count = 0;
        if (manpowerA != null && !availableManpowerIdSet.contains(manpowerA.getId()))
            count++;
        if (manpowerB != null && !availableManpowerIdSet.contains(manpowerB.getId()))
            count++;
        if (manpowerC != null && !availableManpowerIdSet.contains(manpowerC.getId()))
            count++;
        return count * 2;
    }

    // 选择了两个以上的组但是不能同时工作
    public boolean manpowerCannotWorkTogether() {
        boolean hasDay = false;
        boolean hasNight = false;
        if (manpowerA != null) {
            hasDay = manpowerA.getWorkSections().size() == 1;
            hasNight = manpowerA.getWorkSections().size() != 1;
        }
        if (manpowerB != null) {
            hasDay = manpowerB.getWorkSections().size() == 1;
            hasNight = manpowerB.getWorkSections().size() != 1;
        }
        if (manpowerC != null) {
            hasDay = manpowerC.getWorkSections().size() == 1;
            hasNight = manpowerC.getWorkSections().size() != 1;
        }
        return hasDay && hasNight;
    }

    // 人力资源不能工作的总数
    public int manpowerCannotWorkCount() {
        if (timeGrain == null)
            return 0;
        int count = 0;
        if (manpowerA != null && !manpowerA.canWork(timeGrain.getHourOfDay()))
            count++;
        if (manpowerB != null && !manpowerB.canWork(timeGrain.getHourOfDay()))
            count++;
        if (manpowerC != null && !manpowerC.canWork(timeGrain.getHourOfDay()))
            count++;
        return count;
    }

    // 设备不可用
    public boolean deviceNotAvailable() {
        if (device == null)
            return false;
        return !availableDeviceTypeIdSet.contains(device.getDeviceTypeId());
    }

    // 两个订单使用了多少组相同的人力资源
    public int manpowerCrossCount(Suborder b) {
        List<Manpower> selfManpowers = new ArrayList<>(10);
        if (manpowerA != null)
            selfManpowers.add(manpowerA);
        if (manpowerB != null)
            selfManpowers.add(manpowerB);
        if (manpowerC != null)
            selfManpowers.add(manpowerC);
        int res = 0;
        if (selfManpowers.contains(b.manpowerA))
            res++;
        if (selfManpowers.contains(b.manpowerB))
            res++;
        if (selfManpowers.contains(b.manpowerC))
            res++;
        return res;
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