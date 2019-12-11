package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档标识
 */
@Data
public class MailReportAlgDocumentTagsResponse {

    //文档报告类型。 目前有：研报、公告、其他
    private String documentType;

    //二级研报类型
    private String reportType;

    //二级公告类型
    private String noticeType;

    //行业报告类型
    private String algIndustryType;

    //分析师信息
    private List<MailReportAlgAnalystInfoResponse> algAnalystInfo = new ArrayList<MailReportAlgAnalystInfoResponse>();

    //发布机构
    private String algPublishName;

    //发布日期
    private String algPublishDate;

    //评级
    private String algRating;

    //股票名称,即：公司
    private String algSecurityName;

    //股票代码
    private String algSecurityId;

    //行业,多个行业
    private List<String> industries;

    //公司,多个公司
    private List<MailReportAlgCompanyPartResponse> companies;

    //微信分类。  新闻，观点，无意义谈话
    private String wechatType;

    //文档报告总类型，旧字段，待废弃
    private String algReportTypeZero;

    //一级报告类型,旧字段，待废弃
    private String algReportTypeOne;

    //二级报告类型,旧字段，待废弃
    private String algReportTypeTwo;

    //三级报告类型,旧字段，待废弃
    private String algReportTypeThree;

    //根据数据库信息添加的一级二级三级行业信息字段
    private String algIndustryTypeOne;

    private String algIndustryTypeTwo;

    private String algIndustryTypeThree;

    //标题
    private String algTitle;

    //一致性预期
    private List<MailResponseConsensusPartResponse> consensuses;

    //目标价（低）
    private Double targetPriceLow;

}
