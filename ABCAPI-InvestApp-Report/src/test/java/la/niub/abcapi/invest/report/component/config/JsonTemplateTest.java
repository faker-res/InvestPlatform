/**
 * 
 */
package la.niub.abcapi.invest.report.component.config;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import la.niub.abcapi.invest.report.config.configuration.JsonTemplate;

/**
 * @author zhairp createDate: 2019-04-17
 */
public class JsonTemplateTest {

	@Test
	public void getJsonObjTest() {
		JSONObject jo = new JsonTemplate() {
			@Override
			public String dealJson(String json) {
				return json.replace("$dimension", "report_type").replace("$parentId", "461368");
			}
		}.getJsonObj("json/countReportNum.json");
		System.out.println("result:" + jo.toJSONString());
	}

}
