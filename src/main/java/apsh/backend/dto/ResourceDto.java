package apsh.backend.dto;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Shift;
import apsh.backend.po.SuborderProduction;
import apsh.backend.vo.ProductInResourceUseVo;
import apsh.backend.vo.ResourceInResourceLoadVo;
import apsh.backend.vo.ResourceLoadVo;
import apsh.backend.vo.ResourceUseVo;
import liquibase.pro.packaged.D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.text.SimpleDateFormat;
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

    public ResourceDto(Human po) {
        this.resourceId = String.valueOf(po.getId());
        this.resourceName = po.getGroupName();
        this.resourceType = 0;
        this.shift = po.getDailySchedule();
        this.usedTimeList = new ArrayList<ProductInResourceUseVo>();

    }

    public ResourceDto(Equipment po) {
        this.resourceId = String.valueOf(po.getId());
        this.resourceName = po.getName();
        this.resourceType = 1;
        this.shift = po.getDailySchedule();
        this.usedTimeList = new ArrayList<ProductInResourceUseVo>();

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
        int usedTime = 0;
        for (ProductInResourceUseVo i : usedTimeList) {
            usedTime = (int) (usedTime + i.getEndTime().getTime() - i.getStartTime().getTime());
        }
        int workTime = (int) (shift.getEndTime().getTime() - shift.getStartTime().getTime());
        double load = usedTime / workTime;
        return load;
    }

}
