package apsh.backend.serviceImpl;

import apsh.backend.repository.HumanRepository;
import apsh.backend.service.HumanService;
import org.springframework.beans.factory.annotation.Autowired;

public class HumanServiceImpl implements HumanService {

    private final HumanRepository humanRepository;

    @Autowired
    public HumanServiceImpl(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

}
