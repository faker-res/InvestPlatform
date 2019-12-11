package la.niub.abcapi.invest.platform.model.request.mystock;

import lombok.Data;

@Data
public class StockPricesRequest {

    private String sec_uni_code = "";

    private String stock_code = "";
}