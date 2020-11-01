package apsh.backend.service;

import apsh.backend.vo.OrderProgressVo;
import java.util.Date;
import apsh.backend.dto.CustomerOrderDto;
import java.util.List;

public interface OrderService {

    List<CustomerOrderDto> getAll(Integer pageSize, Integer pageNum);

    void add(CustomerOrderDto order);

    void update(CustomerOrderDto order);

    void delete(String id);

    OrderProgressVo getOrderProgress(Date date);
}
