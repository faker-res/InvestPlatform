package la.niub.abcapi.invest.platform.model.request.account;

import la.niub.abcapi.invest.platform.config.enums.CompanyTypeEnum;
import lombok.Data;

@Data
public class AccountCompleteInfoRequest {

    private String userId;

    private String nickname;

    private String email;

    private Long company_id;

    private CompanyTypeEnum company_type;
}
