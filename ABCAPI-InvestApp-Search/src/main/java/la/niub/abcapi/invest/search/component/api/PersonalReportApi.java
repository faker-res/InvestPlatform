/**
 * 
 */
package la.niub.abcapi.invest.search.component.api;

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

import la.niub.abcapi.invest.search.model.response.SolrResponse;

/**
 * @author zhairp createDate: 2019-02-19
 */
@Component
public class PersonalReportApi {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${api.solr.personal_report.url}")
	private String personalReportSolrUrl;

	public SolrResponse query(Map<String, Object> params) {
		params.put("indent", "on");
		params.put("wt", "json");
		log.info("params:{}", JSON.toJSONString(params));
		String result = HttpUtil.doGet(personalReportSolrUrl, params);
		if (StringUtils.isEmpty(result)) {
			throw new RuntimeException("操作solr库异常[" + personalReportSolrUrl + "]");
		}
		log.info("result:{}", result);
		JSONObject jb = JSON.parseObject(result);
		JSONObject response = jb.getJSONObject("response");
		Long numFound = response.getLong("numFound");
		JSONArray docs = response.getJSONArray("docs");
		return new SolrResponse(numFound, docs);
	}

}
