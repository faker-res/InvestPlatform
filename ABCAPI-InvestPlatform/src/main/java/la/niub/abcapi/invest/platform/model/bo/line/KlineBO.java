package la.niub.abcapi.invest.platform.model.bo.line;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class KlineBO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date trade_date;

    private BigDecimal turn;

    private BigDecimal differ_range;

    private BigDecimal amount;

    private String amount_unit = "元";

    private BigDecimal close_price;

    private String close_price_unit = "元";

    private BigDecimal differ;

    private String differ_unit = "元";

    private BigDecimal high;

    private String high_unit = "元";

    private BigDecimal low;

    private String low_unit = "元";

    private BigDecimal open;

    private String open_unit = "元";

    private BigDecimal volume;

    private String volume_unit = "股";

    private BigDecimal amplitude;
}
