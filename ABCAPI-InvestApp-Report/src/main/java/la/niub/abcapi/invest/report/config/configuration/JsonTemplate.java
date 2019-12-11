/**
 * 
 */
package la.niub.abcapi.invest.report.config.configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhairp createDate: 2019-04-17
 */
public abstract class JsonTemplate {
	private Logger log = LoggerFactory.getLogger(getClass());

	public final JSONObject getJsonObj(String jsonPath) {
		log.info("jsonPath:{}", jsonPath);
		String preJson = readJson(jsonPath);
		log.info("preJson:{}", preJson);
		if (!StringUtils.isEmpty(preJson)) {
			String postJson = dealJson(preJson);
			log.info("postJson:{}", postJson);
			return jsonToObj(postJson);
		}
		return JSONObject.parseObject("{}");
	}

	public abstract String dealJson(String json);

	private final String readJson(String jsonPath) {
		URL url = this.getClass().getClassLoader().getResource(jsonPath);
		if (null != url) {
			try {
				return IOUtils.toString(url.openStream(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("readJson异常{}", e);
			}
		}
		return null;
	}

	private final JSONObject jsonToObj(String json) {
		if (StringUtils.isNotEmpty(json)) {
			return JSONObject.parseObject(json);
		}
		return null;
	}

}
