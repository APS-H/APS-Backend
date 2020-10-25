package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentVo {
    String deviceId;
    String name;
    Integer shift;
    List<Integer> workDay;
}