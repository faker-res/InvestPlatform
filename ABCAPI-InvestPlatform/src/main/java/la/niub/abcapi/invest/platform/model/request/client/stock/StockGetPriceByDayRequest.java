package la.niub.abcapi.invest.platform.model.request.client.stock;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StockGetPriceByDayRequest {

    private List<Long> secUniCodes;

    private Long secUniCode;

    private Date day;
}
