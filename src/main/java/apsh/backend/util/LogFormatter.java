package apsh.backend.util;

import apsh.backend.dto.SystemTime;

import java.io.IOException;

public interface LogFormatter {

    void infoControllerRequest(String method, String url, Object vo);

    void warnControllerRequest(String method, String url, Object vo, String warnMsg);

    void errorControllerRequest(String method, String url, Object vo, String errMsg);

    void infoControllerResponse();

    void warnControllerResponse();

    void errorControllerResponse();

    void errorService(String method, Object parameters, String errMsg);

}
