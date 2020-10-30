package apsh.backend.dto;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.vo.ProductInResourceUseVo;
import apsh.backend.vo.ResourceUseVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResourceUseDto {
    private String resourceId;
    private String resourceName;
    private Integer resourceType;
    private List<ProductInResourceUseVo> usedTimeList;

    public ResourceUseDto(Human po) {
        this.resourceId = po.getId();
        this.resourceName = po.getGroupName();
        this.resourceType = 0;
        this.usedTimeList = new ArrayList<ProductInResourceUseVo>();
    }

    public ResourceUseDto(Equipment po) {
        this.resourceId = po.getId();
        this.resourceName = po.getName();
        this.resourceType = 1;
        this.usedTimeList = new ArrayList<ProductInResourceUseVo>();
    }

    public void  addUsedTime(ProductInResourceUseVo usedTime){
        usedTimeList.add(usedTime);
    }

    public ResourceUseVo getResourceUseVo(){
        ResourceUseVo  data=new ResourceUseVo(resourceId,resourceName,resourceType,usedTimeList);
        return data;
    }
}
