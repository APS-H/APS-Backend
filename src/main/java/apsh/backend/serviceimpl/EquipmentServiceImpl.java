package apsh.backend.serviceimpl;

import apsh.backend.dto.EquipmentDto;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.po.Shift;
import apsh.backend.repository.EquipmentRepository;
import apsh.backend.repository.ShiftRepository;
import apsh.backend.service.EquipmentService;
import apsh.backend.service.LegacySystemService;
import apsh.backend.util.LogFormatter;
import apsh.backend.util.LogFormatterImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ShiftRepository shiftRepository;
    private final LegacySystemService legacySystemService;
    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(EquipmentServiceImpl.class));

    @Autowired
    public EquipmentServiceImpl(
            EquipmentRepository equipmentRepository,
            LegacySystemService legacySystemService,
            ShiftRepository shiftRepository) {
        this.equipmentRepository = equipmentRepository;
        this.legacySystemService = legacySystemService;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public void update(EquipmentDto equipmentDto) {
        logger.infoService("update", equipmentDto);
        Optional<Equipment> equipmentOptional = this.equipmentRepository.findByName(equipmentDto.getName());
        // 设备名称不存在，直接添加设备资源记录，删除标志置为0
        // 设备名称存在且逻辑删除标志为1，设备记录已删除，修改失败
        // 否则，更新该条设备资源记录，删除标志置为0
        if (equipmentOptional.isPresent() && equipmentOptional.get().getIsDeleted() == Equipment.DELETED) {
            logger.errorService("update", equipmentDto, "equipment " + equipmentDto.getName() + " has already been deleted! update fails");
        } else {
            Equipment e = new Equipment(equipmentDto);
            e.setIsDeleted(Equipment.DELETED);
            this.equipmentRepository.saveAndFlush(e);
        }
    }

    @Override
    public void delete(String name) {
        logger.infoService("delete", name);
        // 设备名称不存在，直接添加设备资源记录，删除标志置为1
        // 否则，将设备名称对应的设备资源记录的删除标志置1
        Optional<Equipment> equipmentOptional = this.equipmentRepository.findByName(name);
        Equipment e = equipmentOptional.orElseGet(Equipment::new);
        e.setIsDeleted(Equipment.DELETED);
        this.equipmentRepository.saveAndFlush(e);
    }

    @Override
    public void add(String id, EquipmentDto equipmentDto) {
        logger.infoService("add", equipmentDto);
        // 设备名称不存在，直接添加设备资源记录，删除标志置为0
        // 设备名称存在且逻辑删除标志为1，更新该条设备资源记录，删除标志置为0
        // 否则，该设备已存在，新增失败
        Optional<Equipment> equipmentOptional = this.equipmentRepository.findByName(equipmentDto.getName());
        if (!equipmentOptional.isPresent() || equipmentOptional.get().getIsDeleted() == Human.NOT_DELETED) {
            Equipment e = new Equipment(equipmentDto);
            Optional<Shift> shiftOptional = this.shiftRepository.findByName(equipmentDto.getShift().value());
            shiftOptional.ifPresent(e::setDailySchedule);
            e.setIsDeleted(Human.NOT_DELETED);
            this.equipmentRepository.saveAndFlush(e);
        } else {
            logger.errorService("add", equipmentDto, "equipment " + equipmentDto.getDeviceId() + " exists!");
        }
    }

    @Override
    public List<EquipmentDto> getAll(Integer pageSize, Integer pageNum) {
        List<Equipment> legacySystemAllEquipments = legacySystemService.getAllEquipments();
        List<Equipment> incrementEquipments = this.equipmentRepository.findAll();
        List<EquipmentDto> mergedEquipments = merge(legacySystemAllEquipments, incrementEquipments).parallelStream()
                .map(EquipmentDto::new)
                .sorted(Comparator.comparing(EquipmentDto::getName))
                .collect(Collectors.toList());
        int start = Math.max(0, pageSize * (pageNum - 1));
        int end = Math.min(mergedEquipments.size(), pageSize * pageNum);
        return mergedEquipments.subList(start, end);
    }

    private List<Equipment> merge(List<Equipment> legacySystemAllEquipments, List<Equipment> incrementEquipments) {
        Map<String, Equipment> mergedOrders = legacySystemAllEquipments.parallelStream().collect(Collectors.toMap(Equipment::getName, o -> o));
        incrementEquipments.forEach(o -> {
            if (o.getIsDeleted() == Order.DELETED) {
                mergedOrders.remove(o.getName());
            } else {
                mergedOrders.put(o.getName(), o);
            }
        });
        return mergedOrders.values().parallelStream().collect(Collectors.toList());
    }
}
