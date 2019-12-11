/**
 * 
 */
package la.niub.abcapi.invest.seller.component.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memfactory.utils.HttpUtil;

/**
 * 研报代理
 * 
 * @author zhairp createDate: 2019-04-13
 */
@Component
public class ReportApi {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${api.reportUrl}")
	private String reportUrl;

	/**
	 * 主要行业和报告类型NEW
	 * 
	 * @author zhairp createDate: 2019-04-15
	 * @param filter
	 * @param userId
	 * @param sourceType
	 * @return
	 */
	public JSONObject getItems(String filter, String userId, String sourceType, String reportFileType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filter", filter);
		params.put("userId", userId);
		if (!StringUtils.isEmpty(sourceType)) {
			params.put("sourceType", sourceType);
		}
		if (!StringUtils.isEmpty(reportFileType)) {
			params.put("reportFileType", reportFileType);
		}
		String getItemsUrl = reportUrl + "business/getItems";
		logger.info("getItemsUrl:{},input:{}", getItemsUrl, JSON.toJSONString(params));
		String result = HttpUtil.doGet(getItemsUrl, params);
		logger.info("result:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			return jo.getJSONObject("data");
		}
		return null;
	}

	/**
	 * 我的热门公司或者行业NEW
	 * 
	 * @author zhairp createDate: 2019-04-15
	 * @param hotItem
	 * @param sourceType
	 * @param userId
	 * @param latelyDays
	 * @return
	 */
	public JSONArray getHotItem(String hotItem, String sourceType, String userId, Integer latelyDays, String reportFileType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hotItem", hotItem);
		params.put("userId", userId);
		params.put("latelyDays", latelyDays);
		if (!StringUtils.isEmpty(sourceType)) {
			params.put("sourceType", sourceType);
		}
		if (!StringUtils.isEmpty(reportFileType)) {
			params.put("reportFileType", reportFileType);
		}
		String getHotItem = reportUrl + "business/getHotItem";
		logger.info("getHotItem:{},input:{}", getHotItem, JSON.toJSONString(params));
		String result = HttpUtil.doGet(getHotItem, params);
		logger.info("result:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			return jo.getJSONArray("data");
		}
		return null;
	}

	/**
	 * 查询研报搜索结果NEW
	 * 
	 * @author zhairp createDate: 2019-04-15
	 * @param reportTypes
	 * @param reportIndustryTypes
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @param sort
	 * @return
	 */
	public JSONObject queryReport(String[] reportTypes, String[] reportIndustryTypes, String[] reportFileTypes, String sourceType, String userId, Integer pageIndex, Integer pageSize, String sort) {
		JSONObject params = JSONObject.parseObject("{}");
		if (null != reportTypes && reportTypes.length > 0) {
			params.put("reportTypes", reportTypes);
		}
		if (null != reportIndustryTypes && reportIndustryTypes.length > 0) {
			params.put("reportIndustryTypes", reportIndustryTypes);
		}
		if (null != reportFileTypes && reportFileTypes.length > 0) {
			params.put("reportFileTypes", reportFileTypes);
		}
		if (!StringUtils.isEmpty(sourceType)) {
			params.put("sourceType", sourceType);
		}
		params.put("userId", userId);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("sort", sort);
		String queryReport = reportUrl + "search/queryReport";
		logger.info("queryReport:{},input:{}", queryReport, params.toJSONString());
		String result = HttpUtil.doPost(queryReport, (Object) params);
		logger.info("result:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			return jo.getJSONObject("data");
		}
		return null;
	}

	/**
	 * 查询研报详情NEW
	 * 
	 * @author zhairp createDate: 2019-04-15
	 * @param id
	 * @return
	 */
	public JSONObject getReportDetail(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String getReportDetail = reportUrl + "search/getReportDetail";
		logger.info("getReportDetail:{},input:{}", getReportDetail, JSON.toJSONString(params));
		String result = HttpUtil.doGet(getReportDetail, params);
		logger.info("result:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			return jo.getJSONObject("data");
		}
		return null;
	}

}
