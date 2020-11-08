package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProgressVo {
    Double totalDueRate;
    List<OrderInOrderProgressVo> orderProgresslist;

    public void page(int pageSize,int pageNum){
        int start = pageSize * (pageNum - 1);
        int end = pageSize * pageNum;
        orderProgresslist=orderProgresslist.subList(start,end);
    }
}