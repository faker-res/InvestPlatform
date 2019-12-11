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

import la.niub.abcapi.invest.search.model.vo.NewsDetailInputVo;
import la.niub.abcapi.invest.search.model.vo.NewsSearchInputVo;

/**
 * 资讯模块
 * 
 * @author zhairp createDate: 2019-02-16
 */
@RestController
@RequestMapping("news")
public class NewsController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${api.news.newsSearch}")
	private String newsSearch;

	@Value("${api.news.newsDetail}")
	private String newsDetail;

	/**
	 * 资讯搜索
	 * 
	 * @author zhairp createDate: 2019-02-19
	 * @param newsSearchInputVo
	 * @return
	 */
	@GetMapping("newsSearch")
	public Object newsSearch(@Valid NewsSearchInputVo newsSearchInputVo) {
		log.info("资讯搜索入参:{}", newsSearchInputVo.toString());
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(newsSearchInputVo), Map.class);
		String result = HttpUtil.doGet(newsSearch, params);
		log.info("资讯搜索返回结果:{}", result);
		return JSON.parseObject(result);
	}

	/**
	 * 资讯详情
	 * 
	 * @author zhairp createDate: 2019-02-19
	 * @param newsDetailInputVo
	 * @return
	 */
	@GetMapping("newsDetail")
	public Object newsDetail(@Valid NewsDetailInputVo newsDetailInputVo) {
		log.info("资讯详情入参:{}", newsDetailInputVo.toString());
		Map<String, Object> params = JSON.parseObject(JSON.toJSONString(newsDetailInputVo), Map.class);
		String result = HttpUtil.doGet(newsDetail, params);
		log.info("资讯详情返回结果:{}", result);
		return JSON.parseObject(result);
	}

}
