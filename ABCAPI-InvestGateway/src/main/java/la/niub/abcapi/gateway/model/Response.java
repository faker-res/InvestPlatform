package la.niub.abcapi.gateway.model;

import la.niub.abcapi.gateway.config.code.BaseCodeEnum;

public class Response {
    private int code = BaseCodeEnum.SUCCESS.getCode();

    private String message = BaseCodeEnum.SUCCESS.getMessage();

    private Object data = null;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(Object data) {
        this.data = data;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
