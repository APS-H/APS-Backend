package apsh.backend.serviceimpl;

import apsh.backend.po.Shift;
import apsh.backend.repository.ShiftRepository;
import apsh.backend.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift getShift(int id) {
        // TODO : fetch shifts from legacy system
        System.out.println("getShift: id=" + id);
        return shiftRepository.findById(id).get();
    }

}
