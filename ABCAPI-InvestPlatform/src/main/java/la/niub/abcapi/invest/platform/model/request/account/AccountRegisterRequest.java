package la.niub.abcapi.invest.platform.model.request.account;

import la.niub.abcapi.invest.platform.config.CommonConfig;
import lombok.Data;

@Data
public class AccountRegisterRequest {

    private String nickname;

    private String email;

    private String mobile;

    private String password =  CommonConfig.DEFAULT_PASSWORD;
}
