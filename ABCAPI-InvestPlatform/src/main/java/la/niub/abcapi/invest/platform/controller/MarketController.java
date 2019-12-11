package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.model.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : ppan
 * @description :
 * @date : 2019-03-13 11:37
 */
@RestController
@RequestMapping(path = "/market")
public class MarketController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IInvestCloudClient investCloudClient;

    @GetMapping(value = "/categories")
    public Response getMarketCategory(@RequestParam("level") Integer level) throws Exception {
        return investCloudClient.getMarketCategory(level);
    }

    @GetMapping( value = "/categories/items")
    public Response getMarketCategoryItemList(@RequestParam("plateCode") String plateCode) throws Exception {
        return investCloudClient.getMarketCategoryItemList(plateCode);
    }
}
