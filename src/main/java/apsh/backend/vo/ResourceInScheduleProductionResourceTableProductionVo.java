package apsh.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInScheduleProductionResourceTableProductionVo {
    String resourceId;
    Integer resourceType;
    Integer resourceNum;
}