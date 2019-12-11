/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-08
 */
@Data
public class HotQueryInput {
	private String userId;
	private String sourceType;
	private String reportFileType;
	// 分组字段:report_industry_type,report_security_name
	private String hotItem;
	private int latelyDays = 7;
}
