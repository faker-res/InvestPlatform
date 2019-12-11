package la.niub.abcapi.invest.platform.model.response.client.sso;

import lombok.Data;

@Data
public class SsoLoginResponse {

    private String ticket;

    private Boolean isTrialOn;

    private String userId;

    private Integer status;

    private String token;
}
