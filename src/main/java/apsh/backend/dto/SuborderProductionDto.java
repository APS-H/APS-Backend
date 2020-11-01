package apsh.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 子订单的生产不可分割 占用一段连续的时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuborderProductionDto {
    private String id;
    /**
     * 生产开始时间
     */
    private Date startTime;
    /**
     * 生产结束时间
     */
    private Date endTime;
    /**
     * 生产的人力Id列表
     */
    private List<String> manpowerIds;
    /**
     * 生产的设备Id
     */
    private String deviceId;
}
