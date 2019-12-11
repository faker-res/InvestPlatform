package la.niub.abcapi.invest.platform.model.request;

import la.niub.abcapi.invest.platform.config.enums.LineTypeEnum;
import lombok.Data;

@Data
public class KlineRequest {

    private String start_time;

    private String stock_code;

    private LineTypeEnum line_type;
}
