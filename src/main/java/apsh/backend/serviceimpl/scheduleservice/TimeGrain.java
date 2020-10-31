package apsh.backend.serviceimpl.scheduleservice;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeGrain {
    private int index;
    private Date time;
}