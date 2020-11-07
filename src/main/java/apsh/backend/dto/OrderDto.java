package apsh.backend.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import apsh.backend.po.Craft;
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
                .flatMap(h -> IntStream.range(0, h.getGroupSize()).mapToObj(id -> h.getGroupName() + id))
                .collect(Collectors.toList());
        this.availableDeviceTypeIdList = craft.getAvailableEquipmentList().parallelStream()
                .flatMap(e -> IntStream.range(0, e.getCount()).mapToObj(id -> e.getName() + id))
                .collect(Collectors.toList());
    }
}