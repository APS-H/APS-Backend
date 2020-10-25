package apsh.backend.serviceImpl;

import apsh.backend.repository.EquipmentRepository;
import apsh.backend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;

public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

}
