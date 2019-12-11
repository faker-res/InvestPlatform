package la.niub.abcapi.invest.platform.model.bo.analyst;

import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import lombok.Data;

@Data
public class AnalystReportCountBO {

    private Long count;

    private String name;

    private AnalystBO analyst;
}
