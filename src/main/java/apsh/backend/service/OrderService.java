package apsh.backend.service;

import apsh.backend.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAll(Integer pageSize, Integer pageNum);

    void add(OrderDto order);

    void update(OrderDto order);

    void delete(String id);
}
