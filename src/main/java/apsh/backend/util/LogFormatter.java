package apsh.backend.util;

public interface LogFormatter {

    void infoControllerRequest(String method, String url, Object vo);

    void warnControllerRequest(String method, String url, Object vo, String warnMsg);

    void errorControllerRequest(String method, String url, Object vo, String errMsg);

    void infoControllerResponse(String method, String url, Object vo);

    void warnControllerResponse();

    void errorControllerResponse();

    void errorService(String method, Object parameters, String errMsg);

    void infoService(String method, Object parameters);
}
