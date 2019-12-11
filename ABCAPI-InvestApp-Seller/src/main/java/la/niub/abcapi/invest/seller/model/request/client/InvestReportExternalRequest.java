package la.niub.abcapi.invest.seller.model.request.client;

import lombok.Data;

@Data
public class InvestReportExternalRequest {

    private String userId;

    private String fileUrl;

    private String sourceType = "mail";
}
