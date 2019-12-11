package la.niub.abcapi.invest.search.component.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.memfactory.utils.HttpUtil;

import la.niub.abcapi.invest.search.model.response.MdUcUserParentIdResponse;
import la.niub.abcapi.invest.search.model.vo.UserInfoVo;

/**
 * @author zhairp createDate: 2018-11-26
 */
@Component
public class UcServiceApi {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${api.uc.getUserInfo}")
	private String getUserInfo;

	/**
	 * 查询用户信息
	 * 
	 * @author zhairp createDate: 2018-12-03
	 * @param userId
	 * @return
	 */
	public UserInfoVo getUserInfo(String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("code", "CEb1QsbHJXcBaHN4R");
		param.put("userId", userId);
		String response = HttpUtil.doGet(getUserInfo, param, null);
		if (StringUtils.isEmpty(response)) {
			log.error("调用md-uc服务出现错误 userId:{},response:{}", userId, response);
			throw new RuntimeException("调用md-uc服务出现错误:" + response);
		}
		MdUcUserParentIdResponse responseJson = JSON.parseObject(response, MdUcUserParentIdResponse.class);
		if (responseJson.getCode() == null || responseJson.getCode() == 1 || responseJson.getData() == null) {
			log.error("调用md-uc服务出现错误 userId:{},response:{}", userId, response);
			throw new RuntimeException("调用md-uc服务出现错误:" + response);
		}
		UserInfoVo userInfo = JSON.parseObject(JSON.toJSONString(responseJson.getData()), UserInfoVo.class);
		userInfo.setUserId(userId);
		return userInfo;
	}

}
