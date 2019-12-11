package la.niub.abcapi.invest.platform.model.response.client.lineV2;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Data
public class TimelineResponse {

    private Long secUnicode;

    private String stockCode;

    private String stockName;

    private String secType;

    private Date startTime;

    private Date endTime;

    private TimeZone timezone;

    private Double prevClose;

    private Double limitUp;

    private Double high;

    private Double low;

    private Double limitDown;

    private List<TimelineDataResponse> trending;
}
