package apsh.backend.serviceimpl.scheduleservice;

import apsh.backend.dto.TimeSectionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSection {
    @NonNull
    private Integer start;
    @NonNull
    private Integer end;

    public boolean cover(TimeSection other) {
        return start <= other.start && other.end <= end;
    }

    public static TimeSection fromDto(TimeSectionDto dto) {
        return new TimeSection(dto.getStart(), dto.getEnd());
    }

    @Override
    public String toString() {
        return "[" + start.toString() + ", " + end.toString() + "]";
    }
}