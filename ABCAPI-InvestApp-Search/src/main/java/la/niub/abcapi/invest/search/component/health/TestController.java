package la.niub.abcapi.invest.search.component.health;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import la.niub.abcapi.invest.search.component.api.PersonalReportApi;
import la.niub.abcapi.invest.search.component.api.UcServiceApi;
import la.niub.abcapi.invest.search.component.api.UcServiceClient;
import la.niub.abcapi.invest.search.dao.invest.BrokerAnalystMapper;
import la.niub.abcapi.invest.search.dao.reportor.IndustryMapper;
import la.niub.abcapi.invest.search.model.response.CompanyResponse;
import la.niub.abcapi.invest.search.model.response.Response;
import la.niub.abcapi.invest.search.model.response.SolrResponse;
import la.niub.abcapi.invest.search.model.vo.ReportVo;
import la.niub.abcapi.invest.search.service.TestService;

/**
 * 
 * 测试模块【主要测试：多数据库,定时任务,全局异常处理,RPC】
 * 
 * @author zhairp createDate: 2019-02-16
 */
@RestController
public class TestController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private BrokerAnalystMapper brokerAnalystMapper;

	@Autowired
	private IndustryMapper industryMapper;

	@Autowired
	private TestService testService;

	@Autowired
	private UcServiceApi ucServiceApi;

	@Autowired
	private UcServiceClient ucServiceClient;

	@Autowired
	private PersonalReportApi personalReportApi;

	@GetMapping("hello")
	public Response hello(@RequestParam(defaultValue = "Lee") String name) {
		log.info("####################hello invoked!");
		return new Response("hi," + name);
	}

	@GetMapping("getAllAnalysts")
	public Response getAllAnalysts() {
		return new Response(brokerAnalystMapper.getAllAnalysts());
	}

	@GetMapping("getAllIndustries")
	public Response getAllIndustries() {
		return new Response(industryMapper.getAllIndustries());
	}

	@GetMapping("getAllAnalystsByCache")
	public Response getAllAnalystsByCache() {
		return new Response(testService.getAllAnalysts());
	}

	@GetMapping("dealException")
	public Response dealException() {
		throw new RuntimeException("dealException throws");
	}

	@GetMapping("getUserInfo")
	public Response getUserInfo(@NotBlank(message = "参数[userId]不能为空") String userId) {
		return new Response(ucServiceApi.getUserInfo(userId));
	}

	@GetMapping("getCompanyInfo")
	public Response<CompanyResponse> getCompanyInfo(String userId) {
		return ucServiceClient.getCompanyInfo(userId);
	}

	@GetMapping("solrQuery")
	public Response solrQuery(String q, String fl, Integer start, Integer rows, String sort) {
		log.info("solrQuery q:{},fl:{},start:{},rows:{},sort:{}", q, fl, start, rows, sort);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("q", q);
		params.put("fl", fl);
		params.put("start", start);
		params.put("rows", rows);
		params.put("sort", sort);
		SolrResponse solrResponse = personalReportApi.query(params);
		List<ReportVo> data = JSON.parseArray(JSON.toJSONString(solrResponse.getDocs()), ReportVo.class);
		solrResponse.setDocs(data);
		return new Response(solrResponse);
	}

}
