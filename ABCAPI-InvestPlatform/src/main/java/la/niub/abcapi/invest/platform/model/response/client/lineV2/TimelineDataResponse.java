package la.niub.abcapi.invest.platform.model.response.client.lineV2;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TimelineDataResponse {

    private Double now;

    private BigDecimal volume;

    private BigDecimal amount;

    private Double avg;

    private Double differ;

    private Double differPercent;

    private Double turnoverRate;

    private Date timestamp;
}
