/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-13
 */
@Data
public class InvestSolrDocOutput {
	@Field("id")
	private String id;

	@Field("report_topic")
	private String[] reportTopic;

	@Field("report_title")
	private String reportTitle;

	@Field("report_publish_name")
	private String reportPublishName;

	@Field("report_analyst_name")
	private String reportAnalystName;

	@Field("report_tel")
	private String reportTel;

	@Field("report_email")
	private String reportEmail;

	@Field("report_cert_id")
	private String reportCertId;

	@Field("report_page_count")
	private Integer reportPageCount;

	@Field("report_file_type")
	private String reportFileType;

	@Field("report_security_id")
	private String reportSecurityId;

	@Field("report_security_name")
	private String reportSecurityName;

	@Field("report_target_price_low")
	private Double reportTargetPriceLow;

	@Field("report_target_price_high")
	private Double reportTargetPriceHigh;

	@Field("report_rating")
	private String reportRating;

	@Field("report_summary")
	private String reportSummary;

	@Field("report_user_id")
	private String reportUserId;

	@Field("report_parent_id")
	private String reportParentId;

	@Field("report_source_type")
	private String reportSourceType;

	@Field("report_file_url")
	private String reportFileUrl;

	@Field("report_publish_time")
	private Date reportPublishTime;

	@Field("report_industry_type")
	private String reportIndustryType;

	@Field("report_type")
	private String reportType;

	@Field("report_publish_date")
	private String reportPublishDate;

}
