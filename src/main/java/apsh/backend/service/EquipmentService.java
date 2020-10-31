package apsh.backend.service;

import apsh.backend.dto.EquipmentDto;

import java.util.List;

public interface EquipmentService {

    void update(EquipmentDto equipmentDto);

    void delete(String id);

    void add(String id, EquipmentDto equipmentDto);

    List<EquipmentDto> getAll(Integer pageSize, Integer pageNum);
}
