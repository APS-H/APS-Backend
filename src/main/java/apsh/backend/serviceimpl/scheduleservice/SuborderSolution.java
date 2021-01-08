package apsh.backend.serviceimpl.scheduleservice;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class SuborderSolution {
    @NonNull
    @Getter
    private Integer startHourOfDay;

    @Getter
    private List<Suborder> fixedSuborders;

    @NonNull
    @ValueRangeProvider(id = "manpowerCombinationRange")
    @ProblemFactCollectionProperty
    private List<ManpowerCombination> manpowerCombinations;

    @NonNull
    @ValueRangeProvider(id = "deviceRange")
    @ProblemFactCollectionProperty
    private List<Device> devices;

    @NonNull
    @ValueRangeProvider(id = "timeGrainRange")
    @ProblemFactCollectionProperty
    private List<TimeGrain> timeGrains;

    @NonNull
    @Getter
    @PlanningEntityCollectionProperty
    private List<Suborder> suborders;

    @Getter
    @PlanningScore
    private HardSoftScore score;
}