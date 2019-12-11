package la.niub.abcapi.invest.platform.model.request.access;

import la.niub.abcapi.invest.platform.config.enums.StatusEnum;
import lombok.Data;

@Data
public class AccessAddRoleRequest {

    private String userId;

    private String name;

    private String description;

    private String access_ids;

    private String status = StatusEnum.ENABLE.getStatus();
}
