/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import com.memfactory.pub.http.request.BaseQuery;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-25
 */
@Data
public class RelatedAnalystInput extends BaseQuery {
	private String parentId;
	/**
	 * 机构
	 */
	private String publishName;
	/**
	 * 研究员组
	 */
	private String analystName;
	/**
	 * 开始时间,格式如下：2019-04-22
	 */
	private String startDate;
	/**
	 * 结束时间,格式如下：2019-04-23
	 */
	private String endDate;
	/**
	 * 来源类型
	 */
	private String sourceType;

	/**
	 * @author zhairp createDate: 2019-04-25
	 * @return
	 */
	@Override
	public String toString() {
		return "publishName=" + publishName + ", analystName=" + analystName + ", startDate=" + startDate + ", endDate=" + endDate + ", sourceType=" + sourceType + ",pageIndex=" + this.getPageIndex() + ",pageSize=" + this.getPageSize();
	}

}
