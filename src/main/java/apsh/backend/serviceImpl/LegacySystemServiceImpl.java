package apsh.backend.serviceImpl;

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

}
