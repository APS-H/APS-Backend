package apsh.backend.serviceimpl.scheduleservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeGrain {
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH");

    private int index;
    private Date time;
    private int hourOfDay;

    @Override
    public String toString() {
        return "{ " + String.valueOf(index) + ", " + dateFormat.format(time) + " }";
    }
}