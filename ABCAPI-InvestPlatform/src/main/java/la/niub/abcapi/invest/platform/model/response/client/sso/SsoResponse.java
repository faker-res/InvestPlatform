package la.niub.abcapi.invest.platform.model.response.client.sso;

import lombok.Data;

@Data
public class SsoResponse<T> {

    private Integer errorCode;

    private String msg;

    private T data;
}
