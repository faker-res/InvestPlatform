package la.niub.abcapi.invest.platform.model.request.log;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import lombok.Data;

@Data
public class HistoryAddRequest {

    private String userId;

    private String title;

    private String object_id;

    private ObjectTypeEnum object_type;

    private String author;

    private String stock_code;

    private String data;
}
