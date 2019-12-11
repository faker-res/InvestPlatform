package la.niub.abcapi.invest.search.component.exception;

import la.niub.abcapi.invest.search.config.code.ICodeConfig;

public class CustomException extends Exception{

    private Integer code;

    private String message;

    public CustomException(ICodeConfig codeEnum){
        super();
        setCode(codeEnum.getCode());
        setMessage(codeEnum.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
