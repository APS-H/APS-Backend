package apsh.backend.serviceimpl.scheduleservice;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class SuborderSolutionConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { urgentOrderDelay(constraintFactory), manpowerANotAvailable(constraintFactory),
                manpowerBNotAvailable(constraintFactory), manpowerCNotAvailable(constraintFactory),
                deviceNotAvailable(constraintFactory), manpowerOverlap(constraintFactory),
                manpowerPeopleNotEnough(constraintFactory), deviceConflict(constraintFactory),
                softDelay(constraintFactory) };
    }

    private Constraint urgentOrderDelay(ConstraintFactory constraintFactory) {
        // 紧急订单不能延期
        return constraintFactory.from(Suborder.class).filter(suborder -> suborder.getUrgent() && suborder.delay())
                .penalize("Urgent order delay", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerANotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源A不可用
        return constraintFactory.from(Suborder.class)
                .filter(suborder -> suborder.manpowerNotAvailable(suborder.getManpowerA()))
                .penalize("Manpower A not available", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerBNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源B不可用
        return constraintFactory.from(Suborder.class)
                .filter(suborder -> suborder.manpowerNotAvailable(suborder.getManpowerB()))
                .penalize("Manpower B not available", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerCNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源C不可用
        return constraintFactory.from(Suborder.class)
                .filter(suborder -> suborder.manpowerNotAvailable(suborder.getManpowerC()))
                .penalize("Manpower C not available", HardSoftScore.ONE_HARD);
    }

    private Constraint deviceNotAvailable(ConstraintFactory constraintFactory) {
        // 设备不可用
        return constraintFactory.from(Suborder.class).filter(suborder -> suborder.deviceNotAvailable())
                .penalize("Device not available", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerOverlap(ConstraintFactory constraintFactory) {
        // 人力资源重复使用
        return constraintFactory.from(Suborder.class).penalize("Manpower overlap", HardSoftScore.ONE_HARD,
                Suborder::manpowerOverlapCount);
    }

    private Constraint manpowerAWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源A在时间内不可工作
        return null;
    }

    private Constraint manpowerBWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源B在时间内不可工作
        return null;
    }

    private Constraint manpowerCWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源C在时间内不可工作
        return null;
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
                // TODO: manpower
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
}