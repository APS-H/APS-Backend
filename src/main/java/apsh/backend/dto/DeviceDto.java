package apsh.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {
    /**
     * 单个设备的id
     */
    private String id;
    /**
     * 设备类型id
     */
    private String deviceTypeId;

    public DeviceDto(Integer id, EquipmentDto equipmentDto) {
        this.id = equipmentDto.getName() + id;
        this.deviceTypeId = equipmentDto.getName();
    }
}