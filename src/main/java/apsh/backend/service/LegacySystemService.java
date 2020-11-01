package apsh.backend.service;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;

import java.util.List;

public interface LegacySystemService {

    List<Order> getAllOrders();

    List<Human> getAllHumans();

    List<Equipment> getAllEquipments();

}
