package la.niub.abcapi.invest.platform.model.bo.line;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TimelineBO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date trade_date;

    private BigDecimal turn;

    private BigDecimal differ_range;

    private BigDecimal amount;

    private String amount_unit;

    private BigDecimal avg_price;

    private String  avg_price_unit;

    private BigDecimal differ;

    private String differ_unit;

    private BigDecimal open;

    private String open_unit;

    private BigDecimal volume;

    private String volume_unit;
}
