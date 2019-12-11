package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : ppan
 * @description :
 * @date : 2019-04-25 16:02
 */
@Data
public class AnalystReportItemResponse {

    private String reportAnalystName;

    private String reportParentId;

    private String reportPublishDate;

    private String reportPublishName;

    private String reportSecurityId;

    private String reportSecurityName;

    private BigDecimal reportTargetPriceHigh;

    private BigDecimal reportTargetPriceLow;

    private String reportUserId;
}
