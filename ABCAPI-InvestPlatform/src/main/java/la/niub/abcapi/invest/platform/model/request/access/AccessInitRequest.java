package la.niub.abcapi.invest.platform.model.request.access;

import la.niub.abcapi.invest.platform.config.enums.StatusEnum;
import la.niub.abcapi.invest.platform.model.bo.access.AccessTreeBO;
import lombok.Data;

import java.util.List;

@Data
public class AccessInitRequest {

    private String company_name;

    private String manager_user_id;

    private List<AccessTreeBO> auth_tree;
}
