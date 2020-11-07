package apsh.backend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInResourceUseVo {
//    String productId;
    Integer production;
    Date startTime;
    Date endTime;
    Boolean isDelayed;
}