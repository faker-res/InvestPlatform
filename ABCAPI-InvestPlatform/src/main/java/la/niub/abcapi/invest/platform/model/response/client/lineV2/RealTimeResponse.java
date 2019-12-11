package la.niub.abcapi.invest.platform.model.response.client.lineV2;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RealTimeResponse {

    private String market;

    private Long secUnicode;

    private String stockCode;

    private String stockName;

    private String type;

    private Boolean suspended;

    private Double prevClose;

    private Double open;

    private Double now;

    private Double high;

    private Double low;

    private Double limitUp;

    private Double limitDown;

    private BigDecimal volume;

    private BigDecimal amount;

    private Double avg;

    private Double differ;

    private Double differPercent;

    private Double volumeRatio;

    private Double commissionDiff;

    private Double commissionRatio;

    private Double amplitude;

    private Double turnoverRate;

    private Double buyPrice1;

    private Double buyPrice2;

    private Double buyPrice3;

    private Double buyPrice4;

    private Double buyPrice5;

    private Double buyVolume1;

    private Double buyVolume2;

    private Double buyVolume3;

    private Double buyVolume4;

    private Double buyVolume5;

    private Double sellPrice1;

    private Double sellPrice2;

    private Double sellPrice3;

    private Double sellPrice4;

    private Double sellPrice5;

    private Double sellVolume1;

    private Double sellVolume2;

    private Double sellVolume3;

    private Double sellVolume4;

    private Double sellVolume5;

    private BigDecimal liqmv;

    private BigDecimal mv;

    private Double pe;

    private Double pb;

    private Date time;

    private String status;

    private Integer downs;

    private Integer ups;

    private Integer flats;
}
