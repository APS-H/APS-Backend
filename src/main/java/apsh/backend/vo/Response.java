package apsh.backend.vo;

import apsh.backend.enums.StatusCode;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Response {
    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class);

    // 状态码，200-正常，400-客户端错误，500-服务端错误
    private int statusCode;
    // 返回内容，成功时为数据，失败时为失败原因
    private Object content;

    private Response() {
    }


    public static Response succeed(Object content) {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK_200.value());
        response.setContent(content);
        return response;
    }

    public static Response fail(StatusCode statusCode, String msg) {
        Response response = new Response();
        response.setStatusCode(statusCode.value());
        LOGGER.info("返回错误信息:" + msg);
        response.setContent(msg);
        return response;
    }
}
