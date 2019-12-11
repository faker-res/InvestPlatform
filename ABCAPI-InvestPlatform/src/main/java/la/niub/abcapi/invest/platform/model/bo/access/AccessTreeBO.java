package la.niub.abcapi.invest.platform.model.bo.access;

import lombok.Data;

import java.util.List;

@Data
public class AccessTreeBO {

    private String id;

    private String name;

    private String description;

    private List<AccessTreeBO> child;
}
