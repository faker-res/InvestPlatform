package la.niub.abcapi.invest.platform.model.request;

import lombok.Data;

@Data
public class TimelineRequest {

    private String start_time;

    private String stock_code;
}
