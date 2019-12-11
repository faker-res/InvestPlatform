/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.List;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-13
 */
@Data
public class InvestDocTagData {
	private String[] alg_topic;
	private String alg_target_price_low;
	private String alg_security_name;
	private List<InvestDocTagAnalyst> alg_analyst_info;
	private String alg_publish_name;
	private String alg_summary;
	private Integer alg_page_count;
	private Double alg_target_price_high;
	private String alg_title;
	private String alg_security_id;
	private String alg_rating;
	private String alg_file_type;
	private String alg_industry_type;
	private String alg_report_type_two;
}
