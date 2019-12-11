package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

import java.util.List;

/**
 * @author : ppan
 * @description :
 * @date : 2019-04-25 16:01
 */
@Data
public class AnalystReportResponse {
    private List<AnalystReportItemResponse> data;

    private Integer numFound;
}
