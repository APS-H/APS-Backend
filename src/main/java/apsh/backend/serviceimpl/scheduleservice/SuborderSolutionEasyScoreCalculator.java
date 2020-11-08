package apsh.backend.serviceimpl.scheduleservice;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class SuborderSolutionEasyScoreCalculator implements EasyScoreCalculator<SuborderSolution> {
    @Override
    public HardSoftScore calculateScore(SuborderSolution schedule) {
        int hardScore = 0;
        int softScore = 0;
        for (int i = 0; i < schedule.getSuborders().size(); i++) {
            Suborder a = schedule.getSuborders().get(i);
            // 插单不能延期
            if (a.getTimeGrain() != null
                    && a.getTimeGrain().getIndex() + a.getNeedTimeInHour() > a.getDeadlineTimeGrainIndex())
                hardScore--;

            // 人力资源不可用
            if (!manpowerAvailable(a.getManpowerA(), a.getAvailableManpowerIdList()))
                hardScore--;
            if (!manpowerAvailable(a.getManpowerB(), a.getAvailableManpowerIdList()))
                hardScore--;
            if (!manpowerAvailable(a.getManpowerC(), a.getAvailableManpowerIdList()))
                hardScore--;
            // 设备资源不可用
            if (a.getDevice() != null && !a.getAvailableDeviceTypeIdList().contains(a.getDevice().getDeviceTypeId()))
                hardScore--;

            // 人力资源重复
            hardScore -= manpowerOverlapCount(a);
            // 人力资源工作时间不可用
            if (a.getTimeGrain() != null) {
                int startHourOfDay = (schedule.getStartHourOfDay() + a.getTimeGrain().getIndex()) % 24;
                if (a.getManpowerA() != null && !a.getManpowerA().canWork(startHourOfDay, a.getNeedTimeInHour()))
                    hardScore--;
                if (a.getManpowerB() != null && !a.getManpowerB().canWork(startHourOfDay, a.getNeedTimeInHour()))
                    hardScore--;
                if (a.getManpowerB() != null && !a.getManpowerB().canWork(startHourOfDay, a.getNeedTimeInHour()))
                    hardScore--;
            }
            // 人力资源人员数量不够
            if (manpowerPeopleCount(a) < a.getNeedPeopleCount())
                hardScore--;

            // 检查子订单之间冲突
            for (int j = i + 1; j < schedule.getSuborders().size(); j++) {
                Suborder b = schedule.getSuborders().get(j);
                // 时间交叉
                if (suborderCross(a, b)) {
                    // 使用了同种人力资源的数量
                    hardScore -= manpowerCrossCount(a, b);
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

    // 人力资源可用
    private boolean manpowerAvailable(Manpower manpower, List<String> availableManpowerIds) {
        if (manpower == null)
            return true;
        return availableManpowerIds.contains(manpower.getId());
    }

    // 人力资源重复
    private int manpowerOverlapCount(Suborder a) {
        int res = 0;
        if (a.getManpowerB() != null && a.getManpowerB() == a.getManpowerA())
            res++;
        if (a.getManpowerC() != null && a.getManpowerC() == a.getManpowerA())
            res++;
        if (a.getManpowerC() != null && a.getManpowerC() == a.getManpowerB())
            res++;
        return res;
    }

    // 人力资源的人员总数
    private int manpowerPeopleCount(Suborder a) {
        int res = 0;
        if (a.getManpowerA() != null)
            res += a.getManpowerA().getPeopleCount();
        if (a.getManpowerB() != null)
            res += a.getManpowerB().getPeopleCount();
        if (a.getManpowerC() != null)
            res += a.getManpowerC().getPeopleCount();
        return res;
    }

    // 两个订单使用了多少组相同的人力资源
    private int manpowerCrossCount(Suborder a, Suborder b) {
        List<Manpower> aManpowers = new ArrayList<>(10);
        if (a.getManpowerA() != null)
            aManpowers.add(a.getManpowerA());
        if (a.getManpowerB() != null)
            aManpowers.add(a.getManpowerB());
        if (a.getManpowerA() != null)
            aManpowers.add(a.getManpowerC());
        int res = 0;
        if (aManpowers.contains(b.getManpowerA()))
            res++;
        if (aManpowers.contains(b.getManpowerB()))
            res++;
        if (aManpowers.contains(b.getManpowerC()))
            res++;
        return res;
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