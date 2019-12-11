package la.niub.abcapi.invest.platform.model.bo.access;

import lombok.Data;

@Data
public class AccessForChooseBO {

    private String access_id;

    private String name;

    private String description;

    private Boolean chosen;
}
