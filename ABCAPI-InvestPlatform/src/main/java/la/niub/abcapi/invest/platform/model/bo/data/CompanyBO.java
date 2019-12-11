package la.niub.abcapi.invest.platform.model.bo.data;

import la.niub.abcapi.invest.platform.config.enums.CompanyTypeEnum;
import lombok.Data;

@Data
public class CompanyBO {

    private Long company_id;

    private String sname;

    private String fullname;

    private CompanyTypeEnum type;
}
