package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

/**
 * 分析师信息
 */
@Data
public class MailReportAlgAnalystInfoResponse {
    //姓名
    private String analystName;

    //电话
    private String tel;

    //电子邮件
    private String email;

    //分析师执业证书编号
    private String certId;
}
