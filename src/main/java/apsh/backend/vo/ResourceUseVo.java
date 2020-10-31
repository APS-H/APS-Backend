package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceUseVo {
    String resourceId;
    String resourceName;
    Integer resourceType;
    Integer production;
    List<ProductInResourceUseVo> usedTimeList;
}