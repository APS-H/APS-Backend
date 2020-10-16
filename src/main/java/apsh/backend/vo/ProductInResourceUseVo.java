package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInResourceUseVo {
    String productId;
    String stockId;
    Double startHour;
    Double endHour;
    Boolean isUsed;
    Boolean isDelayed;
}