package apsh.backend.serviceimpl;

import apsh.backend.dto.ResourceDto;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.OrderProduction;
import apsh.backend.po.SuborderProduction;
import apsh.backend.repository.EquipmentRepository;
import apsh.backend.repository.HumanRepository;
import apsh.backend.repository.OrderProductionRepository;
import apsh.backend.repository.OrderRepository;
import apsh.backend.service.ResourceService;
import apsh.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final OrderProductionRepository orderProductionRepository;
    private final EquipmentRepository equipmentRepository;
    private final HumanRepository humanRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ResourceServiceImpl(OrderProductionRepository orderProductionRepository, EquipmentRepository equipmentRepository, HumanRepository humanRepository, OrderRepository orderRepository) {
        this.equipmentRepository = equipmentRepository;
        this.humanRepository = humanRepository;
        this.orderProductionRepository = orderProductionRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ResourceLoadVo getResourceLoad(Date date, Integer pageSize, Integer pageNum) {
        List<ResourceDto> RUList = getAllResourceUse(date);
        List<ResourceInResourceLoadVo> resourceLoadlist = RUList.stream().map(ResourceDto::getResourceLoad).collect(Collectors.toList());
        int EquipmentAmount = equipmentRepository.findAll().size();
        Double deviceLoad = 0.0;
        Double manpowerLoad = 0.0;
        for (int i = 0; i < EquipmentAmount; i++) {
            deviceLoad = deviceLoad + RUList.get(i).getLoad() / EquipmentAmount;
        }
        for (int i = EquipmentAmount; i < RUList.size(); i++) {
            manpowerLoad = manpowerLoad + RUList.get(i).getLoad() / (RUList.size() - EquipmentAmount);
        }

        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;

        ResourceLoadVo result = new ResourceLoadVo(deviceLoad, manpowerLoad, resourceLoadlist.subList(0,EquipmentAmount),resourceLoadlist.subList(EquipmentAmount+1,RUList.size()));
        return result;
    }

    @Override
    public List<ResourceDto> getResourceUse(Date date, Integer pageSize, Integer pageNum) {
        List<ResourceDto> RUList=getAllResourceUse(date);
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        return RUList.subList(start, end);
    }


    private List<ResourceDto> getAllResourceUse(Date date) {
        List<Equipment> EList = equipmentRepository.findAll();
        List<Human> HList = humanRepository.findAll();


        //转换为resource类型
        List<ResourceDto> RUList0 = EList.stream().map(o -> {
            ResourceDto resourceUseDto = new ResourceDto(o);
            return resourceUseDto;
        }).sorted(((o1, o2) -> Integer.parseInt(o1.getResourceId()) < Integer.parseInt(o2.getResourceId()) ? 1 : 0)).collect(Collectors.toList());
        List<ResourceDto> RUList1 = HList.stream().map(o -> {
            ResourceDto resourceUseDto = new ResourceDto(o);
            return resourceUseDto;
        }).sorted(((o1, o2) -> Integer.parseInt(o1.getResourceId())< Integer.parseInt(o2.getResourceId())  ? 1 : 0)).collect(Collectors.toList());




        //获取生产单与生产单资源关系表
        List<ScheduleProductionTableProductionVo> SPTPList = new ArrayList<ScheduleProductionTableProductionVo>();
        List<ScheduleProductionResourceTableProductionVo> SPRTPList = new ArrayList<ScheduleProductionResourceTableProductionVo>();
        // TODO
        List<OrderProduction> orderProductions=orderProductionRepository.findAll();

        //假定接口，根据生产单id查询资源关系，接口调用方法为scheduleRepository.getRelateResource(id);
        for (OrderProduction OP : orderProductions) {
            List<SuborderProduction> SOPs=OP.getSuborderProductionsByDate(date);
            int stock_id=orderRepository.findById(Integer.parseInt(OP.getOrderId())).get().getProductId();
            if(!SOPs.isEmpty()){
                for(SuborderProduction SOP:SOPs) {
                    for (ResourceDto equipment : RUList0) {
                        if (equipment.getResourceId().equals( SOP.getDeviceId())){
                            equipment.addUsedTime(SOP,stock_id);
                            break;
                        }
                    }

                    List<String> manPowerIds=new ArrayList<>(SOP.getManpowerIds());
                    for(String id:manPowerIds){

                        for (ResourceDto human : RUList1) {
                            if (human.getResourceId() .equals(id)){
                                human.addUsedTime(SOP,stock_id);
                                break;
                            }
                        }


                    }



                }

            }
        }

        //合并两个list
        List<ResourceDto> RUList = new ArrayList<ResourceDto>();
        RUList.addAll(RUList0);
        RUList.addAll(RUList1);
        return RUList;
    }
}