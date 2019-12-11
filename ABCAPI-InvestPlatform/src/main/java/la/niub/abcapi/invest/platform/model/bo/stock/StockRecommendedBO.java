package la.niub.abcapi.invest.platform.model.bo.stock;

import lombok.Data;

@Data
public class StockRecommendedBO {

    private Long secUniCode;

    private String stockName;

    private String stockCode;

    private String userId;

    private String parentId;

    private String reason;

    private String broker;

    private Long secType;

    private Long secSmallType;
}
