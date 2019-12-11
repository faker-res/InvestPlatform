package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.IAnalystTargetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : ppan
 * @description : 研究员达成天数历史数据同步
 * @date : 2019-03-01 09:57
 */
@RestController
@RequestMapping("/analyst/target")
@Validated
public class AnalystTargetController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IAnalystTargetService analystTargetService;

    /**
     * 同步研究员达成天数历史数据
     * @param parentId 公司id
     * @param startTime 开始时间(包含) yyyy-MM-dd
     * @param endTime 结束时间(包含) yyyy-MM-dd
     * @return
     */
    @GetMapping("/history")
    public Response synchronizeHistoricalData(String parentId, String startTime, String endTime) throws Exception {
        analystTargetService.synchronizeHistoricalData(parentId, startTime, endTime);
        return new Response();
    }

    /**
     * 同步研究员达成天数历史某页数据(当同步某页失败时调用)
     * @param parentId 公司id
     * @param startTime 开始时间(包含) yyyy-MM-dd
     * @param endTime 结束时间(包含) yyyy-MM-dd
     * @param page 页数(从0开始)
     * @return
     */
    @GetMapping("/history/page")
    public Response synchronizeHistoricalDataByPage(String parentId, String startTime, String endTime, Integer page) throws Exception {
        analystTargetService.synchronizeHistoricalDataByPage(parentId, startTime, endTime, page);
        return new Response();
    }

    @GetMapping("/cache/remove")
    public Response removeCache() throws Exception {
        analystTargetService.removeCache();
        return new Response();
    }

}
