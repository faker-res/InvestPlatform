package la.niub.abcapi.invest.seller.model.request.client;

import lombok.Data;

/**
 * @author : ppan
 * @description :
 * @date : 2019-04-25 15:56
 */
@Data
public class AnalystReportNumRequest {

    private String analystName;

    private String endDate;

    private String publishName;

    private String sourceType;

    private String startDate;

    private String parentId;
}
