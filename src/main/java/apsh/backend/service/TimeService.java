package apsh.backend.service;

import apsh.backend.dto.SystemTime;

public interface TimeService {

    void updateTime(SystemTime systemTime);

    void setTime(SystemTime systemTime);

    SystemTime getTime();

}
