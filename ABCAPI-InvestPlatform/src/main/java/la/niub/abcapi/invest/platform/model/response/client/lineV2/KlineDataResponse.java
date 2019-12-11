package la.niub.abcapi.invest.platform.model.response.client.lineV2;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class KlineDataResponse {

    private Double open;

    private Double high;

    private Double low;

    private BigDecimal volume;

    private BigDecimal amount;

    private BigDecimal avg;

    private Double differ;

    private Double differPercent;

    private Double turnoverRate;

    private Double amplitude;

    private Double close;

    private Date timestamp;
}
