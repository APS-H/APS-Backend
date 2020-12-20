package apsh.backend.dto;

import apsh.backend.po.Shift;
import apsh.backend.po.SuborderProduction;
import apsh.backend.vo.ProductInResourceUseVo;
import apsh.backend.vo.ResourceInResourceLoadVo;
import apsh.backend.vo.ResourceUseVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResourceDto {
    private String resourceId;
    private String resourceName;
    private Integer resourceType;
    private Shift shift;
    private List<ProductInResourceUseVo> usedTimeList;

    public ResourceDto(HumanDto po) {
        this.resourceId = String.valueOf(po.getHumanId());
        this.resourceName = po.getTeamName();
        this.resourceType = 0;
        this.shift = po.getShift().getShift();
        this.usedTimeList = new ArrayList<>();

    }

    public ResourceDto(EquipmentDto po) {
        this.resourceId = String.valueOf(po.getDeviceId());
        this.resourceName = po.getName();
        this.resourceType = 1;
        this.shift = po.getShift().getShift();
        this.usedTimeList = new ArrayList<>();

    }

    public void addUsedTime(SuborderProduction SOP, int stock_id) {
        Date date1 = new Date(SOP.getStartTime().getTime());
        Date date2 = new Date(SOP.getEndTime().getTime());
        ProductInResourceUseVo newTime = new ProductInResourceUseVo(stock_id, date1, date2, false);
        usedTimeList.add(newTime);
    }

    public ResourceUseVo getResourceUseVo() {
        ResourceUseVo data = new ResourceUseVo(resourceId, resourceName, resourceType, usedTimeList);
        return data;
    }

    public ResourceInResourceLoadVo getResourceLoad() {
        ResourceInResourceLoadVo resourceInResourceLoadVo = new ResourceInResourceLoadVo(resourceName, getLoad());

        return resourceInResourceLoadVo;
    }

    public double getLoad() {
        long usedTime = 0;
        for (ProductInResourceUseVo i : usedTimeList) {
            System.out.println(i.getEndTime().getTime() - i.getStartTime().getTime());
            usedTime = (usedTime + i.getEndTime().getTime() - i.getStartTime().getTime());
        }
        long workTime = (long)Math.abs (shift.getEndTime().getTime() - shift.getStartTime().getTime());
        double load = ((double)usedTime) / workTime;
        return load;
    }

}
