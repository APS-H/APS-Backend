package apsh.backend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

// 产品工艺路线
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Craft {

    /**
     * 物料编号
     */
    private String productionId;

    // 当前只考虑装配工艺，后续有需要再加其他工艺

    /**
     * 产品规定生产人员
     */
    private Integer needPeopleCount;

    /**
     * 可供选择的人力资源列表
     */
    private List<String> availableHumanList;

    /**
     * 可供选择的设备列表
     */
    private List<String> availableEquipmentList;

    /**
     * 标准产能，单位小时
     */
    private Integer standardCapability;

}
