package apsh.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String id;
    private Boolean urgent;
    /**
     * 完成订单需要的总时间
     */
    private Integer needTimeInHour;
    /**
     * 需要的总人数
     */
    private Integer needPeopleCount;
    /**
     * ddl
     */
    private Date deadline;
    /**
     * 可供选择的人力资源id列表
     */
    private List<String> availableManpowerIdList;
    /**
     * 可供选择的设备id列表
     */
    private List<String> availableDeviceTypeIdList;
}