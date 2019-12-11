/**
 * 
 */
package la.niub.abcapi.invest.report.component.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.memfactory.utils.HttpUtil;

import la.niub.abcapi.invest.report.model.so.CompanySo;

/**
 * @author zhairp createDate: 2019-04-09
 */
@Component
public class PlatformService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${api.platformUrl}")
	private String platformUrl;

	public CompanySo getCompany(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String url = platformUrl + "account/company";
		log.info("url:{},input:{},", url, JSON.toJSONString(params));
		String result = HttpUtil.doGet(url, params);
		log.info("output:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			if (null != jo.getJSONObject("data")) {
				return JSON.parseObject(jo.getJSONObject("data").toJSONString(), CompanySo.class);
			}
		}
		return null;
	}

}
