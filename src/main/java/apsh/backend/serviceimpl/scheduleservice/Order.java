package apsh.backend.serviceimpl.scheduleservice;

import java.util.Date;
import java.util.HashSet;

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
    private HashSet<String> availableManpowerIdSet;
    @NonNull
    private HashSet<String> availableDeviceTypeIdSet;

    public static Order fromDto(OrderDto dto) {
        return new Order(dto.getId(), dto.getUrgent(), 12 /* TODO: dto.getNeedTimeInHour() */, dto.getNeedPeopleCount(),
                dto.getDeadline(), new HashSet<>(dto.getAvailableManpowerIdList()),
                new HashSet<>(dto.getAvailableDeviceTypeIdList()));
    }
}