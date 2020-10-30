package apsh.backend.util;

import org.slf4j.Logger;

public class LogFormatterImpl implements LogFormatter {

    private final Logger logger;

    public LogFormatterImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void infoControllerRequest(String method, String url, Object vo) {
        logger.info("request: method=" + method + ", url=" + url + ", parameters=" + vo);
    }

    @Override
    public void warnControllerRequest(String method, String url, Object vo, String warnMsg) {
        logger.info("request: method=" + method + ", url=" + url + ", parameters=" + vo + "; warn: " + warnMsg);
    }

    @Override
    public void errorControllerRequest(String method, String url, Object vo, String errMsg) {
        logger.info("request: method=" + method + ", url=" + url + ", parameters=" + vo + "; error: " + errMsg);
    }

    @Override
    public void infoControllerResponse(String method, String url, Object vo) {
        logger.info("response: method=" + method + ", url=" + url + ", results=" + vo);
    }

    @Override
    public void warnControllerResponse() {

    }

    @Override
    public void errorControllerResponse() {

    }

    @Override
    public void errorService(String method, Object parameters, String errMsg) {
        logger.error("method=" + method + ", parameters=" + parameters + ", errMsg: " + errMsg);
    }

    @Override
    public void infoService(String method, Object parameters) {
        logger.info("method=" + method + ", parameters=" + parameters);
    }
}
