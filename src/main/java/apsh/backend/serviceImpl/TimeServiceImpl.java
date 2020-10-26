package apsh.backend.serviceImpl;

import apsh.backend.service.TimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

    @Value("${time-server.start-time}")
    private String startTimePath;

    @Value("${time-server.time-speed}")
    private String timeSpeedPath;



}
