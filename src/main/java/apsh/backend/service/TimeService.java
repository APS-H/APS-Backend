package apsh.backend.service;

import apsh.backend.dto.SystemTime;

import java.time.LocalDateTime;

public interface TimeService {

    void updateTime(SystemTime systemTime);

    void setTime(SystemTime systemTime);

    SystemTime getTime();

    LocalDateTime now();

}
