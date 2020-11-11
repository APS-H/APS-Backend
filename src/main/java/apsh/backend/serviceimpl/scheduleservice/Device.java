package apsh.backend.serviceimpl.scheduleservice;

import apsh.backend.dto.DeviceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @NonNull
    private String id;
    @NonNull
    private String deviceTypeId;

    public static Device fromDto(DeviceDto dto) {
        return new Device(dto.getId(), dto.getDeviceTypeId());
    }

    @Override
    public String toString() {
        return "{ " + id.toString() + ", " + deviceTypeId.toString() + " }";
    }
}