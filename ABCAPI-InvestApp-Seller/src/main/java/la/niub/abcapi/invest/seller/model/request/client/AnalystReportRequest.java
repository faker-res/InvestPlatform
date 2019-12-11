package la.niub.abcapi.invest.seller.model.request.client;

import lombok.Data;

/**
 * @author : ppan
 * @description :
 * @date : 2019-04-25 15:58
 */
@Data
public class AnalystReportRequest {

    private String endDate;

    private Integer pageIndex;

    private Integer pageSize;

    private String parentId;

    private String sourceType;

    private String startDate;
}
