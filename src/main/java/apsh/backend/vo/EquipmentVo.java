package apsh.backend.vo;

import java.util.ArrayList;
import java.util.List;

import apsh.backend.dto.EquipmentDto;
import apsh.backend.enums.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EquipmentVo {
    String deviceId;
    String name;
    Integer count;
    Integer shift;
    List<Integer> workDay;

    public EquipmentVo(EquipmentDto equipmentDto) {
        this.deviceId = equipmentDto.getDeviceId();
        this.name = equipmentDto.getName();
        this.count = equipmentDto.getCount();
        this.shift = equipmentDto.getShift().intValue();
        this.workDay = new ArrayList<>(equipmentDto.getWorkDay());
    }
}