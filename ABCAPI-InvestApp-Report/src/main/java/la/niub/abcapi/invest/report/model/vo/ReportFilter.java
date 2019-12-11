/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.List;

import la.niub.abcapi.invest.report.constant.SortType;
import lombok.Data;

/**
 * 通用的研报查询入参
 * 
 * @author zhairp createDate: 2019-03-14
 */
@Data
public class ReportFilter {
	// *****************************高级过滤start****************************//
	// 子账号
	private String userId;
	// 父账号
	private String parentId;
	// 研报类型
	private List<String> reportTypes;
	// 主题
	private List<String> reportTopics;
	// 公司(股票名称)
	private List<String> reportSecurityNames;
	// 股票编码
	private List<String> reportSecurityIds;
	// 所属机构
	private List<String> reportPublishNames;
	// 分析师
	private List<String> reportAnalystNames;
	// 文件类型
	private List<String> reportFileTypes;
	// 评级
	private List<String> reportRatings;
	// 行业
	private List<String> reportIndustryTypes;
	// 来源类型
	private String sourceType;
	// 开始时间,格式如下：2019-04-22
	private String startDate;
	// 结束时间,格式如下：2019-04-23
	private String endDate;
	// 最低价格
	private Double reportTargetPriceLow;
	// 已选择的筛选项,格式如下：字段名称1,字段值1;字段名称2,字段值2;字段名称3,字段值3
	private String selected;
	// *****************************高级过滤end****************************//

	// *****************************分页start****************************//
	// 页面索引,从1开始
	private Integer pageIndex;
	// 页面大小
	private Integer pageSize;
	// *****************************分页end****************************//

	// *****************************排序start****************************//
	// compositive-综合排序 timeDesc-时间降序 timeAsc-时间升序
	private SortType sort;
	// *****************************排序end****************************//

	// *****************************关键字检索start****************************//
	// 关键词
	private String keyword;
	// *****************************关键字检索end****************************//

}
