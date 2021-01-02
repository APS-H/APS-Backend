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
    private Integer lastTime;

    public static TimeSection fromDto(TimeSectionDto dto) {
        return new TimeSection(dto.getStart(), dto.getLastTime());
    }

    @Override
    public String toString() {
        return "{" + start.toString() + ", " + lastTime.toString() + "}";
    }
}