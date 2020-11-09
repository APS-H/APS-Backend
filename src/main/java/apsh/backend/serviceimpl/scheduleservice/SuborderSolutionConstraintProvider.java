package apsh.backend.serviceimpl.scheduleservice;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class SuborderSolutionConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { urgentOrderDelay(constraintFactory), manpowerNotAvailable(constraintFactory),
                manpowerWorkTimeNotAvailable(constraintFactory), deviceNotAvailable(constraintFactory),
                manpowerOverlap(constraintFactory), manpowerPeopleNotEnough(constraintFactory),
                manpowerConflict(constraintFactory), deviceConflict(constraintFactory), softDelay(constraintFactory) };
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
        return constraintFactory.from(Suborder.class).filter(suborder -> suborder.deviceNotAvailable())
                .penalize("Device not available", HardSoftScore.ONE_HARD);
    }

    private Constraint manpowerOverlap(ConstraintFactory constraintFactory) {
        // 人力资源重复使用
        return constraintFactory.from(Suborder.class).penalize("Manpower overlap", HardSoftScore.ONE_HARD,
                Suborder::manpowerOverlapCount);
    }

    private Constraint manpowerWorkTimeNotAvailable(ConstraintFactory constraintFactory) {
        // 人力资源A在时间内不可工作
        return constraintFactory.from(Suborder.class).penalize("Manpower A can not work", HardSoftScore.ONE_HARD,
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
                .penalize("Device conflict", HardSoftScore.ONE_SOFT); // TODO:
    }

    private Constraint softDelay(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Suborder.class).filter(Suborder::delay).penalize("Soft delay",
                HardSoftScore.ONE_SOFT);
    }
}