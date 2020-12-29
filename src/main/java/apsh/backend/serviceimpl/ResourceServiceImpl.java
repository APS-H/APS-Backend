package apsh.backend.serviceimpl;

import apsh.backend.dto.EquipmentDto;
import apsh.backend.dto.HumanDto;
import apsh.backend.dto.ResourceDto;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.OrderProduction;
import apsh.backend.po.SuborderProduction;
import apsh.backend.repository.EquipmentRepository;
import apsh.backend.repository.HumanRepository;
import apsh.backend.repository.OrderProductionRepository;
import apsh.backend.repository.OrderRepository;
import apsh.backend.service.EquipmentService;
import apsh.backend.service.HumanService;
import apsh.backend.service.LegacySystemService;
import apsh.backend.service.ResourceService;
import apsh.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final OrderProductionRepository orderProductionRepository;
    private final EquipmentService equipmentService;
    private final HumanService humanService;
    private final OrderRepository orderRepository;
    private final LegacySystemService legacySystemService;


    private int len = 0;

    @Autowired
    public ResourceServiceImpl(
            OrderProductionRepository orderProductionRepository,
            EquipmentService equipmentService,
            HumanService humanService,
            OrderRepository orderRepository,
            LegacySystemService legacySystemService) {
        this.orderProductionRepository = orderProductionRepository;
        this.equipmentService = equipmentService;
        this.humanService = humanService;
        this.orderRepository = orderRepository;
        this.legacySystemService = legacySystemService;
    }

    @Override
    public ResourceLoadVo getResourceLoad(Date date, Integer pageSize, Integer pageNum) {
        List<ResourceDto> RUList = getAllResourceUse(date);
        List<ResourceInResourceLoadVo> resourceLoadlist = RUList.stream().map(ResourceDto::getResourceLoad).collect(Collectors.toList());
        int EquipmentAmount = len;
        double deviceLoad = 0.0;
        double manpowerLoad = 0.0;
        for (int i = 0; i < EquipmentAmount; i++) {
            deviceLoad = deviceLoad + RUList.get(i).getLoad() / EquipmentAmount;
        }
        for (int i = EquipmentAmount; i < RUList.size(); i++) {
            manpowerLoad = manpowerLoad + RUList.get(i).getLoad() / (RUList.size() - EquipmentAmount);
        }

        return new ResourceLoadVo(deviceLoad, manpowerLoad, resourceLoadlist.subList(0, EquipmentAmount), resourceLoadlist.subList(EquipmentAmount + 1, RUList.size()));
    }

    @Override
    public List<ResourceDto> getResourceUse(Date date, Integer pageSize, Integer pageNum) {
        List<ResourceDto> RUList = getAllResourceUse(date);
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        start = Math.max(start, 0);
        end = Math.min(end, RUList.size());
        return RUList.subList(start, end);
    }

    private List<ResourceDto> getAllResourceUse(Date date) {
        List<ResourceDto> equipmentResourceList = equipmentService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                .flatMap(e -> IntStream.range(0, e.getCount()).mapToObj(c -> {
                    ResourceDto s = new ResourceDto(e);
                    s.setResourceId(s.getResourceName() + "-" + c);
                    return s;
                }))
                .collect(Collectors.toList());
        List<ResourceDto> humanResourceList = humanService.getAll(Integer.MAX_VALUE, 1).parallelStream()
                .map(h -> {
                    ResourceDto s = new ResourceDto(h);
                    s.setResourceId(s.getResourceName());
                    return s;
                })
                .collect(Collectors.toList());

        len = equipmentResourceList.size();

        List<OrderProduction> orderProductions = orderProductionRepository.findAll();
        //假定接口，根据生产单id查询资源关系，接口调用方法为scheduleRepository.getRelateResource(id);
        for (OrderProduction OP : orderProductions) {
            List<SuborderProduction> SOPs = OP.getSuborderProductionsByDate(date);
            int stock_id = Integer.parseInt(OP.getOrderId());
            if (!SOPs.isEmpty()) {
                for (SuborderProduction SOP : SOPs) {
                    for (ResourceDto equipment : equipmentResourceList) {
                        if (equipment.getResourceName().equals(SOP.getDeviceId())) {
                            equipment.addUsedTime(SOP, stock_id);
                            break;
                        }
                    }
                    List<String> manPowerIds = new ArrayList<>(SOP.getManpowerIds());
                    for (String id : manPowerIds) {
                        for (ResourceDto human : humanResourceList) {
                            if (human.getResourceName().equals(id)) {
                                human.addUsedTime(SOP, stock_id);
                                break;
                            }
                        }
                    }
                }
            }
        }

        //合并两个list
        List<ResourceDto> RUList = new ArrayList<>();
        RUList.addAll(equipmentResourceList);
        RUList.addAll(humanResourceList);
        return RUList;
    }
}
