package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description : 指数相关
 */
@RestController
@RequestMapping(path = "/index")
public class IndexController {

    private final static Logger logger = LogManager.getLogger(IndexController.class);

    @Autowired
    private IInvestCloudClient investCloudClient;

    /**
     * 获取指数指定日期的收盘价
     * @param indexUniCode
     * @param day yyyy-MM-dd
     * @return
     */
    @GetMapping("/dayprice")
    public Response getIndexDayPriceByIndexUniCode(@RequestParam("indexUniCode") Long indexUniCode, @RequestParam("day") String day) {
        try{
            return investCloudClient.getIndexDayPriceByIndexUniCode(indexUniCode, day);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

}
