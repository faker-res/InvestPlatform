/**
 * 
 */
package la.niub.abcapi.invest.report.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memfactory.pub.http.response.BaseResult;
import com.memfactory.utils.HttpUtil;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.config.configuration.JsonTemplate;
import la.niub.abcapi.invest.report.config.configuration.JsonTemplateFactory;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.ReportQuery;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportManageService;

/**
 * @author zhairp createDate: 2019-04-22
 */
@Component
public class ReportManageServiceImpl implements ReportManageService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${spring.data.solr.host}")
	private String solrHost;

	@Autowired
	private PlatformService platformService;

	/**
	 * @author zhairp createDate: 2019-04-22
	 * @param userId
	 * @param dimension
	 * @return
	 */
	@Override
	public JSONObject statisticsReportNums(String userId, String dimension) {
		CompanySo company = platformService.getCompany(userId);
		if (null == company || StringUtils.isEmpty(company.getParentId())) {
			throw new RuntimeException("请维护账户" + userId + "的父账号");
		}
		JSONObject result = JSONObject.parseObject("{}");
		result.put("count", 0);
		result.put("buckets", JSONArray.parseArray("[]"));
		JSONObject params = JsonTemplateFactory.createJSONObj(new JsonTemplate() {
			@Override
			public String dealJson(String json) {
				log.info("parentId:{},dimension:{}", company.getParentId(), dimension);
				return json.replace("$dimension", dimension).replace("$parentId", company.getParentId());
			}
		}, "json/countReportNum.json");
		String jsonUrl = solrHost + "/invest_report_solr/query";
		String response = HttpUtil.doPost(jsonUrl, (Object) params);
		if (!StringUtils.isEmpty(response)) {
			JSONObject jo = JSONObject.parseObject(response);
			JSONObject facets = jo.getJSONObject("facets");
			Integer count = facets.getInteger("count");
			if (null != count && count.intValue() > 0) {
				JSONObject hotItem = facets.getJSONObject("hotItem");
				if (null != hotItem) {
					JSONArray buckets = hotItem.getJSONArray("buckets");
					int numCount = 0;
					if (null != buckets && buckets.size() > 0) {
						for (int j = 0; j < buckets.size(); j++) {
							numCount += buckets.getJSONObject(j).getInteger("count").intValue();
						}
					}
					result.put("count", numCount);
					result.put("buckets", buckets);
				}
			}
		}
		return result;
	}

	/**
	 * @author zhairp createDate: 2019-04-24
	 * @param reportQuery
	 * @return
	 */
	@Override
	public BaseResult<ResearchTaskDomain> reportList(ReportQuery reportQuery) {
		// FIXME
		Long total = null;
		List<ResearchTaskDomain> rows = null;
		return new BaseResult<ResearchTaskDomain>(total, rows);
	}

}
