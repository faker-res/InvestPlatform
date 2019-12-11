/**
 * 
 */
package la.niub.abcapi.invest.seller.model;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-14
 */
@Data
public class ReportFilter {
	private String userId;
	// 研报类型
	private String[] reportTypes;
	// 行业
	private String[] reportIndustryTypes;
	/**
	 * 分页
	 */
	private Integer pageIndex;
	private Integer pageSize;

	/**
	 * @author zhairp createDate: 2019-04-18
	 * @return
	 */
	@Override
	public String toString() {
		return "ReportFilter [userId=" + userId + ", reportTypes=" + reportTypes + ", reportIndustryTypes=" + reportIndustryTypes + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + "]";
	}

}
