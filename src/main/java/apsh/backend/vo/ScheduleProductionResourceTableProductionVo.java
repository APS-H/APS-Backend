package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleProductionResourceTableProductionVo {
    String productionId;
    List<ResourceInScheduleProductionResourceTableProductionVo> resources;
}