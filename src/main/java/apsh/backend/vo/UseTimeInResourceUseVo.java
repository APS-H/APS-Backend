package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseTimeInResourceUseVo {
    Double startHour;
    Double endHour;
    Boolean isUsed;
    Boolean isDelayed;
    String stockNumber;
}