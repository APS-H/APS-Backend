package apsh.backend.serviceImpl;

import apsh.backend.dto.ResourceDto;
import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.repository.EquipmentRepository;
import apsh.backend.repository.HumanRepository;
import apsh.backend.repository.ScheduleRepository;
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

    private final ScheduleRepository scheduleRepository;
    private final EquipmentRepository equipmentRepository;
    private final HumanRepository humanRepository;

    @Autowired
    public ResourceServiceImpl(ScheduleRepository scheduleRepository, EquipmentRepository equipmentRepository, HumanRepository humanRepository) {
        this.equipmentRepository = equipmentRepository;
        this.humanRepository = humanRepository;
        this.scheduleRepository = scheduleRepository;
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

        //合并两个list
        List<ResourceDto> RUList = new ArrayList<ResourceDto>();
        RUList.addAll(RUList0);
        RUList.addAll(RUList1);


        //获取生产单与生产单资源关系表
        List<ScheduleProductionTableProductionVo> SPTPList = new ArrayList<ScheduleProductionTableProductionVo>();
        List<ScheduleProductionResourceTableProductionVo> SPRTPList = new ArrayList<ScheduleProductionResourceTableProductionVo>();
        // TODO

        //假定接口，根据生产单id查询资源关系，接口调用方法为scheduleRepository.getRelateResource(id);
//        for (ScheduleProductionTableProductionVo sptp : SPTPList) {
//            ScheduleProductionResourceTableProductionVo sprtp=scheduleRepository.getRelateResource(sptp.getid());
//
//
//            for (ResourceUseVo RUV : RUList) {
//                if (RUV.id==sprtp.id){
//                     RUV.addAllTask(sptp);
//                          break;
//                }
//
//            }
//        }
//
        return RUList;
    }
}
