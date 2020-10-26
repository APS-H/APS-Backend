package apsh.backend.util;

import apsh.backend.vo.TimeVo;
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
    public void infoControllerResponse() {

    }

    @Override
    public void warnControllerResponse() {

    }

    @Override
    public void errorControllerResponse() {

    }
}
