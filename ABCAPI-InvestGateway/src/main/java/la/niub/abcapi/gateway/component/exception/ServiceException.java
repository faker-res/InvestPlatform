package la.niub.abcapi.gateway.component.exception;

import la.niub.abcapi.gateway.config.code.BaseCodeEnum;
import la.niub.abcapi.gateway.config.code.ICodeConfig;

public class ServiceException extends Exception {
    private int code = BaseCodeEnum.ERROR_DEFAULT.getCode();

    private String message = BaseCodeEnum.ERROR_DEFAULT.getMessage();

    public ServiceException() {
        super();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ICodeConfig object) {
        super(object.getMessage());
        this.code = object.getCode();
    }

    public int getCode() {
        return code;
    }
}
