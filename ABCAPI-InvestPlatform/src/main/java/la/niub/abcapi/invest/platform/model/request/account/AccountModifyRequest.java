package la.niub.abcapi.invest.platform.model.request.account;

import lombok.Data;

@Data
public class AccountModifyRequest {

    private Integer id;

    private String nickname;

    private String password;

    private String role_ids;

    private String status;
}
