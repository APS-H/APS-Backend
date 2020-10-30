package apsh.backend.serviceImpl;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.repository.EquipmentRepository;
import apsh.backend.repository.HumanRepository;
import apsh.backend.service.ResourceService;
import apsh.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
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
    public ResourceLoadVo getResourceLoad(Date date) {
        List<ResourceUseVo> RUList=getResourceUse(date);
        List<ResourceInResourceLoadVo> resourceLoadlist=RUList.stream().map(ResourceUseVo::getLoad).collect(Collectors.toList());
        int EquipmentAmount=equipmentRepository.findAll().size();
        Double deviceLoad=0.0;
        Double manpowerLoad=0.0;
        for(int i=0;i<EquipmentAmount;i++){
            deviceLoad=deviceLoad+RUList.get(i).getLoad()/EquipmentAmount;
        }
        for(int i=EquipmentAmount;i<RUList.size();i++){
            manpowerLoad=manpowerLoad+RUList.get(i).getLoad()/(RUList.size()-EquipmentAmount);
        }
        ResourceLoadVo result=new ResourceLoadVo(deviceLoad,manpowerLoad,resourceLoadlist);
        return result;
    }

    @Override
    public List<ResourceUseVo> getResourceUse(Date date) {
        List<Equipment> EList = equipmentRepository.findAll();
        List<Human> HList = humanRepository.findAll();


        //转换为resource类型
        List<ResourceUseVo> RUList0 = EList.stream().map(Equipment::toResourceUseVo).collect(Collectors.toList());
        List<ResourceUseVo> RUList1 = HList.stream().map(Human::toResourceUseVo).collect(Collectors.toList());

        //合并两个list
        List<ResourceUseVo> RUList = new ArrayList<ResourceUseVo>();
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
