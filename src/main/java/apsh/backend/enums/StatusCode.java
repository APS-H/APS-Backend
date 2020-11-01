package apsh.backend.enums;

import apsh.backend.vo.Response;

/**
 * Created by Peihong Chen, 2020/02/26 16:12
 * <p>
 * HTTP响应状态码，{@link Response}
 */
public enum StatusCode {
    // 正常
    OK_200(200),
    // 客户端错误
    CLIENT_ERROR_400(400),
    // 服务端错误
    SERVER_ERROR_500(500);

    private final int value;

    private StatusCode(int value) {
        this.value = value;
    }

    public static StatusCode valueOf(int value) {
        switch (value) {
            case 200:
                return OK_200;
            case 400:
                return CLIENT_ERROR_400;
            case 500:
                return SERVER_ERROR_500;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
