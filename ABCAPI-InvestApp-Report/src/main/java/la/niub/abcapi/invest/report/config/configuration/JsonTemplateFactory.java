/**
 * 
 */
package la.niub.abcapi.invest.report.config.configuration;

import com.alibaba.fastjson.JSONObject;

/**
 * JSONTemplate工厂
 * 
 * @author zhairp createDate: 2019-04-20
 */
public final class JsonTemplateFactory {
	private JsonTemplateFactory() {
	}

	public static JSONObject createJSONObj(JsonTemplate jsonTemplate, String jsonPath) {
		return jsonTemplate.getJsonObj(jsonPath);
	}

}
