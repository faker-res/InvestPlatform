package la.niub.abcapi.invest.seller.controller;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.CompanyResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 处理公网数据或者可共用的请求
 * @date : 2019-01-18 09:34
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    /**
     * 根据关键字获取公网数据的券商列表
     * @param keyword
     * @return
     * @throws Exception
     */
    @GetMapping("/suggest/broker")
    public Response getBrokerList(String keyword) throws Exception {
        Response<List<CompanyResponse>> response = apiPlatFormClient.broker();
        if (response == null ||response.getCode() != 200 || response.getData() == null || response.getData().isEmpty()) {
            throw new RuntimeException("从平台获取券商列表失败,返回值：" + JSONObject.toJSONString(response));
        } else {
            List<CompanyResponse> data = response.getData();
            List<Map<String, String>> result = new ArrayList<>();
            data.forEach(companyResponse -> {
                if (StringUtil.isEmpty(keyword) || companyResponse.getSname().contains(keyword)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("broker", companyResponse.getSname());

                    result.add(map);
                }
            });

            return new Response(result);
        }
    }

    /**
     * 搜索建议
     * @param category
     * @param keyword
     * @return
     * @throws Exception
     */
    @GetMapping("/suggest/company")
    public Response getCompanyList(String category, String keyword) throws Exception {
        return apiPlatFormClient.suggestEntity(keyword, category);
    }
}
