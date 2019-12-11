package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudObjectClient;
import la.niub.abcapi.invest.platform.model.request.client.KeyWordRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 运营配置,包括：获取预置热词，热门搜索，热门推荐运营位配置接口
 * 数据图运营位配置接口，数据表的运营位接口
 */
@RestController
@RequestMapping(path = "/operate-config")
public class OperateConfigController {

    private static Logger logger = LogManager.getLogger(OperateConfigController.class);

    @Autowired
    IInvestCloudObjectClient investCloudObjectClient;

    /**
     * 获取预置热词，热门搜索，热门推荐运营位配置接口
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/keyword-query")
    Response queryWithKeyWord(KeyWordRequest request) {
        return investCloudObjectClient.queryWithKeyWord(request);
    }

}