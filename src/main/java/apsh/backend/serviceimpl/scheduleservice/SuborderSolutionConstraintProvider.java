package apsh.backend.serviceimpl.scheduleservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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
                manpowerPeopleNotEnough(constraintFactory), manpowerConflict(constraintFactory),
                deviceConflict(constraintFactory), predecessorNotComplete(constraintFactory),
                softDelay(constraintFactory), softTotalEarlyFinish(constraintFactory),
                softSelfEarlyFinish(constraintFactory), softDeviceLoadBalance(constraintFactory),
                softManpowerLoadBalance(constraintFactory) };
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

    private Constraint manpowerWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源在时间内不可工作
        return constraintFactory.from(Suborder.class).filter(Suborder::manpowerCannotWork)
                .penalize("Manpower can not work", HardSoftScore.ONE_HARD);
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
                .join(Suborder.class, Joiners.lessThan(Suborder::getId), Joiners.equal(Suborder::getTimeGrain),
                        Joiners.filtering(Suborder::manpowerCross))
                .penalize("Manpower conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint deviceConflict(ConstraintFactory constraintFactory) {
        // 同一时间段内设备冲突
        return constraintFactory
                .from(Suborder.class).join(Suborder.class, Joiners.lessThan(Suborder::getId),
                        Joiners.equal(Suborder::getDevice), Joiners.equal(Suborder::getTimeGrain))
                .penalize("Device conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint predecessorNotComplete(ConstraintFactory constraintFactory) {
        // 测试、装配要有顺序
        return constraintFactory.from(Suborder.class).penalize("Suborder predecessor not complete",
                HardSoftScore.ONE_HARD, Suborder::predecessorNotComplete);
    }

    private Constraint softDelay(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Suborder.class).filter(Suborder::delay).penalize("Soft delay",
                HardSoftScore.ONE_SOFT);
    }

    private Constraint softTotalEarlyFinish(ConstraintFactory constraintFactory) {
        // 总体尽早结束
        return constraintFactory.from(Suborder.class).groupBy(earlyFinish(Suborder::getTimeGrain)).penalize(
                "Soft total early finish", HardSoftScore.ONE_SOFT, EarlyFinishData::getLatestDdlTimeGrainIndex);
    }

    private Constraint softSelfEarlyFinish(ConstraintFactory constraintFactory) {
        // 个体尽早结束
        return constraintFactory.from(Suborder.class).penalize("Soft self early finish", HardSoftScore.ONE_SOFT,
                Suborder::earlyFinishDay);
    }

    private Constraint softDeviceLoadBalance(ConstraintFactory constraintFactory) {
        // 设备利用率尽可能高
        return constraintFactory.from(Suborder.class).groupBy(deviceLoadBalance(Suborder::getDevice)).reward(
                "Soft device load balance", HardSoftScore.ONE_SOFT,
                DeviceLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    private Constraint softManpowerLoadBalance(ConstraintFactory constraintFactory) {
        // 员工负载均衡
        return constraintFactory.from(Suborder.class).groupBy(manpowerLoadBalance(Suborder::getManpowerCombination))
                .penalize("Soft manpower load balance", HardSoftScore.ONE_SOFT,
                        ManpowerLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    private static DefaultUniConstraintCollector<Suborder, ?, EarlyFinishData> earlyFinish(
            Function<Suborder, TimeGrain> getTimeGrain) {
        return new DefaultUniConstraintCollector<>(EarlyFinishData::new, (resultContainer, suborder) -> {
            TimeGrain timeGrain = getTimeGrain.apply(suborder);
            return resultContainer.apply(timeGrain);
        }, resultContainer -> resultContainer);
    }

    private static DefaultUniConstraintCollector<Suborder, ?, DeviceLoadBalanceData> deviceLoadBalance(
            Function<Suborder, Device> getDevice) {
        return new DefaultUniConstraintCollector<>(DeviceLoadBalanceData::new, (resultContainer, suborder) -> {
            Device dev = getDevice.apply(suborder);
            return resultContainer.apply(dev);
        }, resultContainer -> resultContainer);
    }

    private static DefaultUniConstraintCollector<Suborder, ?, ManpowerLoadBalanceData> manpowerLoadBalance(
            Function<Suborder, ManpowerCombination> keyGet) {
        return new DefaultUniConstraintCollector<>(ManpowerLoadBalanceData::new, (resultContainer, suborder) -> {
            ManpowerCombination combination = keyGet.apply(suborder);
            return resultContainer.apply(combination);
        }, resultContainer -> resultContainer);
    }

    private static final class EarlyFinishData {
        private final TreeMap<Integer, Integer> ddlCountMap = new TreeMap<>();

        private Runnable apply(TimeGrain timeGrain) {
            if (timeGrain != null)
                ddlCountMap.compute(timeGrain.getIndex(), (key, value) -> (value == null) ? 1 : value + 1);
            return () -> {
                if (timeGrain != null)
                    ddlCountMap.compute(timeGrain.getIndex(), (key, value) -> (value == 1) ? null : value - 1);
            };
        }

        public int getLatestDdlTimeGrainIndex() {
            return ddlCountMap.isEmpty() ? 0 : ddlCountMap.lastKey();
        }
    }

    private static final class DeviceLoadBalanceData {
        private final Map<String, Integer> countMap = new LinkedHashMap<>();
        private long squaredSum = 0L;

        private Runnable apply(Device dev) {
            if (dev != null) {
                long count = countMap.compute(dev.getId(), (key, value) -> (value == null) ? 1 : value + 1);
                squaredSum += 2 * count - 1;
            }
            return () -> {
                if (dev != null) {
                    Integer count = countMap.compute(dev.getId(), (key, value) -> (value == 1) ? null : value - 1);
                    squaredSum -= count == null ? 1 : (2L * count + 1L);
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

        private Runnable apply(ManpowerCombination combination) {
            Manpower a = combination.getA();
            Manpower b = combination.getB();
            long deltaA = 0;
            if (a != null)
                deltaA += -1 + 2 * countMap.compute(a.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            if (b != null)
                deltaA += -1 + 2 * countMap.compute(b.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            // if (c != null)
            // deltaA += -1 + 2 * countMap.compute(c.getId(), (key, value) -> (value ==
            // null) ? 1L : value + 1L);
            squaredSum += deltaA;
            return () -> {
                long deltaB = 0;
                if (a != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(a.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                if (b != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(b.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                // if (c != null)
                // deltaB += 1 + 2
                // * nullToZero(countMap.compute(c.getId(), (key, value) -> (value == 1) ? null
                // : value - 1L));
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