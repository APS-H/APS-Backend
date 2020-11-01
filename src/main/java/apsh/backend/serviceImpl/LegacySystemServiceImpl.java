package apsh.backend.serviceimpl;

import apsh.backend.po.Equipment;
import apsh.backend.po.Human;
import apsh.backend.po.Order;
import apsh.backend.service.LegacySystemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegacySystemServiceImpl implements LegacySystemService {

    @Override
    public List<Order> getAllOrders() {
        // TODO
        return null;
    }

    @Override
    public List<Human> getAllHumans() {
        return null;
    }

    @Override
    public List<Equipment> getAllEquipments() {
        return null;
    }

}
