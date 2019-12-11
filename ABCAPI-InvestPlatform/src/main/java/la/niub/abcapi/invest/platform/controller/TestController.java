package la.niub.abcapi.invest.platform.controller;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.component.client.ISsoClient;
import la.niub.abcapi.invest.platform.component.job.RecommendStockJob;
import la.niub.abcapi.invest.platform.component.util.PasswordUtil;
import la.niub.abcapi.invest.platform.config.enums.GenderEnum;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystBO;
import la.niub.abcapi.invest.platform.model.bo.stock.StockRecommendedBO;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetMonthReturnRateRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetPriceByDayRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoApiUserinfoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoUserInfoResponse;
import la.niub.abcapi.invest.platform.service.IDataService;
import la.niub.abcapi.invest.platform.service.IRecommendStockService;
import la.niub.abcapi.invest.platform.service.impl.RecommendStockServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用testest
 */
@RestController
@RequestMapping(path = "/test")
public class TestController {

    private final static Logger logger = LogManager.getLogger(TestController.class);

    @Value("${server.port}")
    private Integer serverPort;

    @Autowired
    private IDataService dataService;

    public static void main(String[] args) throws Exception {
        ;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    String hello() {
        return "this is test "+serverPort;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    String test() {
        return "this is test123321!!!!";
    }

    @Autowired
    private ISsoClient ssoClient;

    @RequestMapping(path = "/ssouserinfo", method = RequestMethod.GET)
    Response ssouserinfo(@RequestParam(value = "userId",defaultValue = "80114336991872214") String userId) {
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userId", userId);
        codeParam.put("mobile", "");
        codeParam.put("email", "");
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        SsoResponse<SsoApiUserinfoResponse> ret = ssoClient.userInfo(realCode,userId,"","");
        return new Response(ret.getData());
    }

    @RequestMapping(path = "/getUserInfoByToken", method = RequestMethod.GET)
    Response getUserInfoByToken(@RequestParam(value = "userId",defaultValue = "80114336991872214") String userId,
                              @RequestParam(value = "token") String token) {
        SsoResponse<SsoUserInfoResponse> ret = ssoClient.getUserInfoByToken(userId,token);
        return new Response(ret.getData());
    }

    @RequestMapping(path = "/addUser", method = RequestMethod.GET)
    Response addUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName","xiaowu");
        jsonObject.put("password","12345678");
        jsonObject.put("mobile","15777963320");
        jsonObject.put("email","15777963320@abcft.com");
        jsonObject.put("inst_name","");
        jsonObject.put("position","");
        jsonObject.put("department","");
        jsonObject.put("gender", GenderEnum.MALE.getGender());

        //需严格按插入顺序排序
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userName", "xiaowu");
        codeParam.put("password", "12345678");
        codeParam.put("mobile", "15777963320");
        codeParam.put("email", "15777963320@abcft.com");
        codeParam.put("inst_name", "");
        codeParam.put("position", "");
        codeParam.put("department", "");
        codeParam.put("gender", GenderEnum.MALE.getGender());
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        jsonObject.put("code",realCode);

        SsoResponse ret = ssoClient.addUser(jsonObject);
        return new Response(ret);
    }

    @RequestMapping(path = "/delUser", method = RequestMethod.GET)
    Response delUser(@RequestParam(value = "userId") String userId) {
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userId", userId);
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        SsoResponse ret = ssoClient.delUser(userId,realCode);
        return new Response(ret);
    }

//    @GetMapping("list")
//    @ResponseBody
//    public Response getMailList(@RequestParam("uid") String userId) {
//        try {
//            userId = trimNonAsc(userId);
//            List<MailConfigResponse> configs = getUserBindings(userId);
//            List<JSONObject> list = new ArrayList<>();
//            for (MailConfigResponse config : configs) {
//                JSONObject o = ((JSONObject) JSON.toJSON(config));
//                o.remove("userId");
//                o.remove("password");
//                o.remove("status");
//                o.remove("lastSyncDate");
//                o.remove("errorMessage");
//
//                list.add(o);
//            }
//
//            return new Response(list);
//        } catch (Exception e) {
//            logger.error("Failed to get mail list.", e);
//            return new ErrorResponse(e.getMessage());
//        }
//    }

//    @Autowired
//    @Qualifier("modelingResMongo")
//    protected MongoTemplate mongoTemplate;
//
//    /**
//     * 获取用户绑定的邮箱
//     *
//     * @param uid 用户
//     * @return
//     */
//    public List<MailConfigResponse> getUserBindings(String uid) {
//        Query query = new Query(Criteria.where("ID").is(uid));
//        Criteria criteria = new Criteria();
//        criteria.orOperator(Criteria.where("deleted").is(false), Criteria.where("deleted").exists(false));
//        query.addCriteria(criteria);
//        query.with(new Sort(Sort.Direction.DESC, "create_time"));
//        return mongoTemplate.find(query, MailConfigResponse.class, "res_mail_config");
//    }

    private static String trimNonAsc(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }

        return StringUtils.strip(str, "\u200B");
    }

    @Autowired
    private IRecommendStockService recommendStockService;

    @Autowired
    private RecommendStockJob recommendStockJob;

    @GetMapping("testemail")
    public Response testemail() throws Exception {
        recommendStockJob.handleEmailAttachment();
        return new Response();
    }

    @Autowired
    private IInvestCloudClient investCloudClient;

    @GetMapping("teststock")
    public Response teststock() {
        List<String> nameOrCode = Arrays.asList("美的集团,招商银行".split(","));
        return new Response(investCloudClient.getStockByNameOrCode(nameOrCode));
    }

    @GetMapping("testindustry")
    public Response testindustry() {
        List<Long> secUniCodes = new ArrayList<Long>(){{add(2860L);add(1520L);}};
        return new Response(investCloudClient.getIndustry(secUniCodes));
    }

    @GetMapping("testprice")
    public Response testprice() {
        StockGetPriceByDayRequest request = new StockGetPriceByDayRequest();
        List<Long> secUniCodes = new ArrayList<Long>(){{add(2860L);add(1520L);}};
        Date day = new Date(1547942400000L);
        request.setSecUniCodes(secUniCodes);
        request.setDay(day);
        return new Response(investCloudClient.getPriceByDay(request));
    }

    @GetMapping("testreturnrate")
    public Response testreturnrate() {
        StockGetMonthReturnRateRequest request = new StockGetMonthReturnRateRequest();
        List<Long> secUniCodes = new ArrayList<Long>(){{add(2860L);add(1520L);}};
        Date day = new Date(1547942400000L);
        request.setSecUniCodes(secUniCodes);
        request.setMonth(day);
        return new Response(investCloudClient.getMonthReturnRate(request));
    }
    
	@GetMapping("helloworld")
	public String helloworld() {
		logger.info("######################helloworld");
		return "hello world!!!";
	}

    @GetMapping("/testanalyst")
    public Response testAnalyst(String name,String companyName) {
        AnalystBO analystBO = dataService.findAnalyst(name,companyName);
        return new Response(analystBO);
    }

    @Autowired
    private RecommendStockServiceImpl recommendStockServiceImpl;

    @RequestMapping("/parseGoldStock")
    public Response parseGoldStock(String file_url) throws Exception {
        Workbook wb = null;
        InputStream is = null;
        try {
            URL url = new URL(file_url);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3 * 60 * 1000);
            is = conn.getInputStream();
        } catch (Exception e) {
            try {
                URL url = new URL(file_url);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3 * 60 * 1000);
                is = conn.getInputStream();
            } catch (Exception e2) {
                throw new Exception("获取在线文件出错");
            }
        }

        wb = WorkbookFactory.create(is);
        List<StockRecommendedBO> list = recommendStockServiceImpl.parseGoldStock(wb,null);
        return new Response(list);
    }
	
}
