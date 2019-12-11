package la.niub.abcapi.invest.platform.model.bo.analyst;

import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import lombok.Data;

@Data
public class AnalystBO {

    private String peo_uni_code;

    private String name;

    private String analyst_code;

    private CompanyBO company;
}
