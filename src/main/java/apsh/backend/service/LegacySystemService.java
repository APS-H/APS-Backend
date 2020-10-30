package apsh.backend.service;

import apsh.backend.po.Order;

import java.util.List;

public interface LegacySystemService {
    List<Order> getAllOrders();
}
