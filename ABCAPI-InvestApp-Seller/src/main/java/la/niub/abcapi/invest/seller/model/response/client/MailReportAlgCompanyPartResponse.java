package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

import java.util.List;

@Data
public class MailReportAlgCompanyPartResponse {
    //股票代码
    private List<String> codes;

    //公司名称
    private String name;
}
