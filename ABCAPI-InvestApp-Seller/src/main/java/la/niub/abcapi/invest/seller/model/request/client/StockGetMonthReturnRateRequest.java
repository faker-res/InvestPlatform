package la.niub.abcapi.invest.seller.model.request.client;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StockGetMonthReturnRateRequest {

    private List<Long> secUniCodes;

    private Long secUniCode;

    private Date month;
}
