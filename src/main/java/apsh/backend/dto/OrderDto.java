package apsh.backend.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import apsh.backend.po.Craft;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
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
    /**
     * 前驱订单的id，比如测试的前驱是装配
     */
    private String predeceesorOrderId;

    public OrderDto(CustomerOrderDto customerOrderDto, Craft craft) {
        this(customerOrderDto, craft, false);
    }

    public OrderDto(CustomerOrderDto customerOrderDto, Craft craft, boolean urgent) {
        this.id = String.valueOf(customerOrderDto.getOrderId());
        this.urgent = urgent;
        this.needTimeInHour = customerOrderDto.getProductCount() / craft.getStandardCapability();
        this.needPeopleCount = craft.getNeedPeopleCount();
        this.deadline = customerOrderDto.getDayOfDelivery();
        this.availableManpowerIdList = craft.getAvailableHumanList().parallelStream()
//                .peek(h -> h.setGroupSize(StringUtil.lastDigit(h.getGroupName())))
//                .flatMap(h -> IntStream.range(0, h.getGroupSize()).mapToObj(id -> h.getGroupName() + id))
                .map(Human::getGroupName)
                .collect(Collectors.toList());
        this.availableDeviceTypeIdList = craft.getAvailableEquipmentList().parallelStream()
//                .peek(e -> e.setCount(StringUtil.lastDigit(e.getName())))
//                .flatMap(e -> IntStream.range(0, e.getCount()).mapToObj(id -> e.getName() + id))
                .map(Equipment::getName)
                .collect(Collectors.toList());
    }
}