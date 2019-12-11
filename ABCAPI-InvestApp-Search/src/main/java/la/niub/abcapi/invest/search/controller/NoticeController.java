/**
 * 
 */
package la.niub.abcapi.invest.search.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.memfactory.utils.HttpUtil;

import la.niub.abcapi.invest.search.model.vo.NoticeDetailInputVo;
import la.niub.abcapi.invest.search.model.vo.NoticeSearchInputVo;

/**
 * 
 * 公告模块
 * 
 * @author zhairp createDate: 2019-02-20
 */
@RestController
@RequestMapping("notice")
public class NoticeController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${api.notice.noticeSearch}")
	private String noticeSearch;

	@Value("${api.notice.noticeDetail}")
	private String noticeDetail;

	/**
	 * 公告搜索
	 * 
	 * @author zhairp createDate: 2019-02-20
	 * @param noticeSearchInputVo
	 * @return
	 */
	@GetMapping("noticeSearch")
	public Object noticeSearch(@Valid NoticeSearchInputVo noticeSearchInputVo) {
		log.info("公告搜索入参:{}", noticeSearchInputVo.toString());
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(noticeSearchInputVo), Map.class);
		String result = HttpUtil.doGet(noticeSearch, params);
		log.info("公告搜索返回结果:{}", result);
		return JSON.parseObject(result);
	}

	/**
	 * 公告详情
	 * 
	 * @author zhairp createDate: 2019-02-20
	 * @param noticeDetailInputVo
	 * @return
	 */
	@GetMapping("noticeDetail")
	public Object noticeDetail(@Valid NoticeDetailInputVo noticeDetailInputVo) {
		log.info("公告详情入参:{}", noticeDetailInputVo.toString());
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(noticeDetailInputVo), Map.class);
		String result = HttpUtil.doGet(noticeDetail, params);
		log.info("公告详情返回结果:{}", result);
		return JSON.parseObject(result);
	}

}
