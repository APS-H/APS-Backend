package apsh.backend.serviceimpl;

import apsh.backend.dto.HumanDto;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.repository.HumanRepository;
import apsh.backend.service.HumanService;
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
public class HumanServiceImpl implements HumanService {

    private final HumanRepository humanRepository;
    private final LegacySystemService legacySystemService;
    private final LogFormatter logger = new LogFormatterImpl(LoggerFactory.getLogger(HumanServiceImpl.class));

    @Autowired
    public HumanServiceImpl(HumanRepository humanRepository, LegacySystemService legacySystemService) {
        this.humanRepository = humanRepository;
        this.legacySystemService = legacySystemService;
    }

    @Override
    public void update(HumanDto humanDto) {
        logger.infoService("update", humanDto);
        Optional<Human> humanOptional = this.humanRepository.findById(humanDto.getHumanId());
        // 设备名称不存在，直接添加设备资源记录，删除标志置为0
        // 设备名称存在且逻辑删除标志为1，设备记录已删除，修改失败
        // 否则，更新该条设备资源记录，删除标志置为0
        if (humanOptional.isPresent() && humanOptional.get().getIsDeleted() == Human.DELETED) {
            logger.errorService("update", humanDto, "humanresource " + humanDto.getTeamName() + " has already been deleted! update fails");
        } else {
            Human h = new Human(humanDto);
            h.setIsDeleted(Human.DELETED);
            this.humanRepository.saveAndFlush(h);
        }
    }

    @Override
    public void delete(Integer id) {
        logger.infoService("delete", id);
        // 组名不存在，直接添加人力资源记录，删除标志置为1
        // 否则，将组名对应的人力资源记录的删除标志置1
        Optional<Human> humanOptional = this.humanRepository.findById(id);
        Human h = humanOptional.orElseGet(Human::new);
        h.setIsDeleted(Human.DELETED);
        this.humanRepository.saveAndFlush(h);
    }

    @Override
    public void add(Integer id, HumanDto humanDto) {
        logger.infoService("add", humanDto);
        // 组名不存在，直接添加人力资源记录，删除标志置为0
        // 组名存在且逻辑删除标志为1，更新该条人力资源记录，删除标志置为0
        // 否则，该组已存在，新增失败
        Optional<Human> humanOptional = this.humanRepository.findByGroupName(humanDto.getTeamName());
        if (!humanOptional.isPresent() || humanOptional.get().getIsDeleted() == Human.NOT_DELETED) {
            Human h = new Human(humanDto);
            h.setIsDeleted(Human.NOT_DELETED);
            this.humanRepository.saveAndFlush(h);
        } else {
            logger.errorService("add", humanDto, "group " + humanDto.getTeamName() + " exists!");
        }
    }

    @Override
    public List<HumanDto> getAll(Integer pageSize, Integer pageNum) {
        List<Human> legacySystemAllHumans = legacySystemService.getAllHumans();
        List<Human> incrementHumans = this.humanRepository.findAll();
        List<HumanDto> mergedHumans = merge(legacySystemAllHumans, incrementHumans).parallelStream()
                .map(HumanDto::new)
                .sorted(Comparator.comparing(HumanDto::getTeamName))
                .collect(Collectors.toList());
        int start = Math.max(0, pageSize * (pageNum - 1));
        int end = Math.min(mergedHumans.size(), pageSize * pageNum);
        return mergedHumans.subList(start, end);
    }

    private List<Human> merge(List<Human> legacySystemAllHumans, List<Human> incrementHumans) {
        Map<String, Human> mergedOrders = legacySystemAllHumans.parallelStream().collect(Collectors.toMap(Human::getGroupName, o -> o));
        incrementHumans.forEach(o -> {
            if (o.getIsDeleted() == Order.DELETED) {
                mergedOrders.remove(o.getGroupName());
            } else {
                mergedOrders.put(o.getGroupName(), o);
            }
        });
        return mergedOrders.values().parallelStream().collect(Collectors.toList());
    }
}
