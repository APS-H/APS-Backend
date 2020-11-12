package apsh.backend.serviceimpl.scheduleservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import org.optaplanner.core.impl.score.stream.uni.DefaultUniConstraintCollector;

public class SuborderSolutionConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { urgentOrderDelay(constraintFactory), manpowerNotAvailable(constraintFactory),
                manpowerWorkTimeNotAvailable(constraintFactory), deviceNotAvailable(constraintFactory),
                manpowerOverlap(constraintFactory), manpowerPeopleNotEnough(constraintFactory),
                manpowerConflict(constraintFactory), deviceConflict(constraintFactory), softDelay(constraintFactory),
                softDeviceLoadBalance(constraintFactory) };
    }

    private Constraint urgentOrderDelay(ConstraintFactory constraintFactory) {
        // 紧急订单不能延期
        return constraintFactory.from(Suborder.class).filter(suborder -> suborder.getUrgent() && suborder.delay())
                .penalize("Urgent order delay", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源不可用
        return constraintFactory.from(Suborder.class).penalize("Manpower not available", HardSoftScore.ONE_HARD,
                Suborder::manpowerNotAvailableCountMul2);
    }

    private Constraint deviceNotAvailable(ConstraintFactory constraintFactory) {
        // 设备不可用
        return constraintFactory.from(Suborder.class).filter(Suborder::deviceNotAvailable)
                .penalize("Device not available", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerOverlap(ConstraintFactory constraintFactory) {
        // 人力资源重复使用
        return constraintFactory.from(Suborder.class).penalize("Manpower overlap", HardSoftScore.ONE_HARD,
                Suborder::manpowerOverlapCount);
    }

    // private Constraint manpowerCannotWorkTogether(ConstraintFactory constraintFactory) {
    //     // TODO: 同组内的人力资源工作时间不相同 不能同时工作 用于加速搜索
    //     return constraintFactory.from(Suborder.class).filter(Suborder::manpowerCannotWorkTogether)
    //             .penalize("Manpower can not work together", HardSoftScore.ONE_HARD);
    // }

    private Constraint manpowerWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源在时间内不可工作
        return constraintFactory.from(Suborder.class).penalize("Manpower can not work", HardSoftScore.ONE_HARD,
                Suborder::manpowerCannotWorkCount);
    }

    private Constraint manpowerPeopleNotEnough(ConstraintFactory constraintFactory) {
        // 人数不够
        return constraintFactory.from(Suborder.class)
                .filter(suborder -> suborder.manpowerPeopleCount() < suborder.getNeedPeopleCount())
                .penalize("Manpower people not enough", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerConflict(ConstraintFactory constraintFactory) {
        // 同一时间段内人力冲突
        return constraintFactory.from(Suborder.class)
                .join(Suborder.class, Joiners.lessThan(Suborder::getId), Joiners.equal(Suborder::getTimeGrain))
                .penalize("Manpower conflict", HardSoftScore.ONE_HARD, Suborder::manpowerCrossCount);
    }

    private Constraint deviceConflict(ConstraintFactory constraintFactory) {
        // 同一时间段内设备冲突
        return constraintFactory
                .from(Suborder.class).join(Suborder.class, Joiners.lessThan(Suborder::getId),
                        Joiners.equal(Suborder::getDevice), Joiners.equal(Suborder::getTimeGrain))
                .penalize("Device conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint softDelay(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Suborder.class).filter(Suborder::delay).penalize("Soft delay",
                HardSoftScore.ONE_SOFT);
    }

    // 设备负载均衡
    private Constraint softDeviceLoadBalance(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Suborder.class).groupBy(deviceLoadBalance(Suborder::getDevice))
                .penalize("Soft dense", HardSoftScore.ONE_SOFT, DeviceLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    // 员工负载均衡
    private Constraint softManpowerLoadBalance(ConstraintFactory constraintFactory) {
        // TODO: 测试
        return constraintFactory.from(Suborder.class)
                .groupBy(manpowerLoadBalance(Suborder::getManpowerA, Suborder::getManpowerB, Suborder::getManpowerC))
                .penalize("Soft manpower load balance", HardSoftScore.ONE_SOFT,
                        ManpowerLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    private static DefaultUniConstraintCollector<Suborder, ?, DeviceLoadBalanceData> deviceLoadBalance(
            Function<Suborder, Device> getDevice) {
        return new DefaultUniConstraintCollector<>(DeviceLoadBalanceData::new, (resultContainer, suborder) -> {
            Device dev = getDevice.apply(suborder);
            return resultContainer.apply(dev);
        }, resultContainer -> resultContainer);
    }

    private static DefaultUniConstraintCollector<Suborder, ?, ManpowerLoadBalanceData> manpowerLoadBalance(
            Function<Suborder, Manpower> aKeyGet, Function<Suborder, Manpower> bKeyGet,
            Function<Suborder, Manpower> cKeyGet) {
        return new DefaultUniConstraintCollector<>(ManpowerLoadBalanceData::new, (resultContainer, suborder) -> {
            Manpower a = aKeyGet.apply(suborder);
            Manpower b = bKeyGet.apply(suborder);
            Manpower c = cKeyGet.apply(suborder);
            return resultContainer.apply(a, b, c);
        }, resultContainer -> resultContainer);
    }

    private static final class DeviceLoadBalanceData {
        private final Map<String, Long> countMap = new LinkedHashMap<>();
        private long squaredSum = 0L;

        private Runnable apply(Device dev) {
            if (dev != null) {
                long count = countMap.compute(dev.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
                squaredSum += 2 * count - 1;
            }
            return () -> {
                if (dev != null) {
                    Long count = countMap.compute(dev.getId(), (key, value) -> (value == 1L) ? null : value - 1L);
                    squaredSum -= count == null ? 1 : (2 * count + 1);
                }
            };
        }

        public int getZeroDeviationSquaredSumRoot() {
            return (int) (Math.sqrt((double) squaredSum));
        }
    }

    private static final class ManpowerLoadBalanceData {
        private final Map<String, Long> countMap = new LinkedHashMap<>();
        private long squaredSum = 0L;

        private Runnable apply(Manpower a, Manpower b, Manpower c) {
            long deltaA = 0;
            if (a != null)
                deltaA += -1 + 2 * countMap.compute(a.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            if (b != null)
                deltaA += -1 + 2 * countMap.compute(b.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            if (c != null)
                deltaA += -1 + 2 * countMap.compute(c.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            squaredSum += deltaA;
            return () -> {
                long deltaB = 0;
                if (a != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(a.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                if (b != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(b.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                if (c != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(c.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                squaredSum -= deltaB;
            };
        }

        private long nullToZero(Long num) {
            return num == null ? 0L : num;
        }

        public int getZeroDeviationSquaredSumRoot() {
            return (int) (Math.sqrt((double) squaredSum));
        }
    }
}