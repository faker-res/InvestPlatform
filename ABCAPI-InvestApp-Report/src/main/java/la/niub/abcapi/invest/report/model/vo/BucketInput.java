/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-08
 */
@Data
public class BucketInput {
	private String userId;
	private String sourceType;
	// 分组字段
	private String hotItem;

	private String startDate;
	private String endDate;
	// 行业
	private String industryType;
	// 公司
	private String company;
	// 文件类型
	private String reportFileType;
}
