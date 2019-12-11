package la.niub.abcapi.invest.platform.model.request.log;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import lombok.Data;

@Data
public class HistoryListRequest {

    private String userId;

    private ObjectTypeEnum object_type;

    private String stock_code;

    private Long start_time;

    private Long end_time;

    private Boolean group_by_id = false;

    private Integer offset = 0;

    private Integer page = 1;

    private Integer limit = 10;
}
