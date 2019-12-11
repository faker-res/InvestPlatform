package la.niub.abcapi.invest.platform.model.response.client.lineV2;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Data
public class KlineResponse {

    private Long secUnicode;

    private String stockCode;

    private String stockName;

    private String secType;

    private String period;

    private Date startTime;

    private TimeZone timezone;

    private List<KlineDataResponse> data;
}
