/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import com.alibaba.fastjson.JSONObject;
import com.memfactory.pub.http.response.BaseResult;

import la.niub.abcapi.invest.report.model.vo.ReportQuery;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;

/**
 * @author zhairp createDate: 2019-04-22
 */
public interface ReportManageService {

	/**
	 * 公共的研报数据统计服务
	 * 
	 * @author zhairp createDate: 2019-04-22
	 * @param userId
	 * @param dimension
	 * @return
	 */
	JSONObject statisticsReportNums(String userId, String dimension);

	/**
	 * 研报列表
	 * 
	 * @author zhairp createDate: 2019-04-24
	 * @param reportQuery
	 * @return
	 */
	BaseResult<ResearchTaskDomain> reportList(ReportQuery reportQuery);

}
