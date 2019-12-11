package la.niub.abcapi.invest.platform.model.bo.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockMonthReturnRateBO {

    private Long secUniCode;

    private Date month;

    private BigDecimal goldRate;

    private BigDecimal industryRate;

    private BigDecimal monthBeginPrice;

    private BigDecimal monthEndPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectBegin;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectEnd;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date actualBegin;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date actualEnd;
}
