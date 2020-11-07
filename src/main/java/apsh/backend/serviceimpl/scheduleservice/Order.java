package apsh.backend.serviceimpl.scheduleservice;

import java.util.Date;
import java.util.List;

import apsh.backend.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @NonNull
    private String id;
    @NonNull
    private Boolean urgent;
    @NonNull
    private Integer needTimeInHour;
    @NonNull
    private Integer needPeopleCount;
    @NonNull
    private Date deadline;
    @NonNull
    private List<String> availableManpowerIdList;
    @NonNull
    private List<String> availableDeviceTypeIdList;

    public static Order fromDto(OrderDto dto) {
        return new Order(dto.getId(), dto.getUrgent(), dto.getNeedTimeInHour(), dto.getNeedPeopleCount(),
                dto.getDeadline(), dto.getAvailableManpowerIdList(), dto.getAvailableDeviceTypeIdList());
    }
}