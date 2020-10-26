package apsh.backend.util;

public interface LogFormatter {

    void infoControllerRequest(String method, String url, Object vo);

    void warnControllerRequest(String method, String url, Object vo, String warnMsg);

    void errorControllerRequest(String method, String url, Object vo, String errMsg);

    void infoControllerResponse();

    void warnControllerResponse();

    void errorControllerResponse();

}
