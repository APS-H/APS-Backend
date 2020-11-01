package apsh.backend.serviceimpl.scheduleservice;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class SuborderSolutionEasyScoreCalculator implements EasyScoreCalculator<SuborderSolution> {
    @Override
    public HardSoftScore calculateScore(SuborderSolution schedule) {
        int hardScore = 0;
        int softScore = 0;
        for (int i = 0; i < schedule.getSuborders().size(); i++) {
            Suborder a = schedule.getSuborders().get(i);
            // 人力资源不可用
            if (a.getManpower() != null && !a.getAvailableManpowerIdList().contains(a.getManpower().getId()))
                hardScore--;
            // 设备资源不可用
            if (a.getDevice() != null && !a.getAvailableDeviceTypeIdList().contains(a.getDevice().getDeviceTypeId()))
                hardScore--;

            // 人力资源工作时间不可用
            if (a.getManpower() != null && a.getTimeGrain() != null) {
                int startHourOfDay = (schedule.getStartHourOfDay() + a.getTimeGrain().getIndex()) % 24;
                if (!a.getManpower().canWork(startHourOfDay, a.getNeedTimeInHour()))
                    hardScore--;
            }

            // 检查子订单之间冲突
            for (int j = i + 1; j < schedule.getSuborders().size(); j++) {
                Suborder b = schedule.getSuborders().get(j);
                // 时间交叉
                if (suborderCross(a, b)) {
                    // 使用了同一组人力资源
                    if (a.getManpower() == b.getManpower())
                        hardScore--;
                    // 使用了同一个设备
                    if (a.getDevice() == b.getDevice())
                        hardScore--;
                }
            }

            // 延期
            if (a.getTimeGrain() != null
                    && a.getTimeGrain().getIndex() + a.getNeedTimeInHour() > a.getDeadlineTimeGrainIndex())
                softScore--;
        }
        return HardSoftScore.of(hardScore, softScore);
    }

    // 判断订单之间相交
    private boolean suborderCross(Suborder a, Suborder b) {
        // 还没分配 不可能相交
        if (a.getTimeGrain() == null || b.getTimeGrain() == null)

            return false;
        // 子订单a在b之前的相交
        if (a.getTimeGrain().getIndex() <= b.getTimeGrain().getIndex()
                && a.getTimeGrain().getIndex() + a.getNeedTimeInHour() > b.getTimeGrain().getIndex())
            return true;
        // 子订单b在a之前的相交
        if (b.getTimeGrain().getIndex() <= a.getTimeGrain().getIndex()
                && b.getTimeGrain().getIndex() + b.getNeedTimeInHour() > a.getTimeGrain().getIndex())
            return true;

        return false;
    }
}