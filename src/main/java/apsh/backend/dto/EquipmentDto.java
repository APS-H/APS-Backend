package apsh.backend.dto;

import apsh.backend.enums.ShiftType;
import apsh.backend.po.Equipment;
import apsh.backend.vo.EquipmentVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EquipmentDto {

    Integer deviceId;
    String name;
    Integer count;
    ShiftType shift;
    List<Integer> workDay;

    public EquipmentDto(EquipmentVo vo) {
        this.deviceId = Integer.valueOf(vo.getDeviceId());
        this.name = vo.getName();
        this.count = vo.getCount();
        this.shift = ShiftType.valueOf(vo.getShift());
        this.workDay = new ArrayList<>(vo.getWorkDay());
    }

    public EquipmentDto(Equipment equipment) {
        this.deviceId = equipment.getId();
        this.name = equipment.getName();
        this.count = equipment.getCount();
        this.shift = ShiftType.valueOf(equipment.getDailySchedule().getName());
        this.workDay = Arrays.stream(equipment.getWeeklySchedule().split(","))
                .map(Integer::valueOf).collect(Collectors.toList());

    }
}
