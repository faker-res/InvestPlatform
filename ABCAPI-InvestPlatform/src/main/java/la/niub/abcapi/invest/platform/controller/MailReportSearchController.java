package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.model.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : ppan
 * @description : 邮件研报搜索
 * @date : 2019-02-26 09:31
 */
@RestController
@RequestMapping("/mail/report")
public class MailReportSearchController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IInvestCloudClient investCloudClient;

    @PostMapping(path = "/search")
    public Response search(@RequestBody String param) throws Exception {
        return investCloudClient.mailReportSearch(param);
    }

    @GetMapping(path = "/facet")
    public Response searchFacet(@RequestParam("type") String type, @RequestParam("userId") String userId) throws Exception {
        return investCloudClient.mailReportSearchFacet(type, userId);
    }

    @GetMapping(path = "/hot/industry")
    public Response getHotIndustry(@RequestParam("type") String type, @RequestParam("userId") String userId) throws Exception {
        return investCloudClient.getMailReportHotIndustry(type, userId);
    }

    @GetMapping(path = "/hot/company")
    public Response getHotCompany(@RequestParam("type") String type, @RequestParam("userId") String userId) throws Exception {
        return investCloudClient.getMailReportHotCompany(type, userId);
    }

    @GetMapping(path = "/detail")
    public Response getReportDetail(@RequestParam("reportId") String reportId) throws Exception {
        return investCloudClient.getMailReportDetail(reportId);
    }

    @GetMapping(path = "/chart")
    public Response getReportChart(@RequestParam("reportId") String reportId) throws Exception {
        return investCloudClient.getMailReportChart(reportId);
    }

    @GetMapping(path = "/table")
    public Response getReportTable(@RequestParam("reportId") String reportId) throws Exception {
        return investCloudClient.getMailReportTable(reportId);
    }

    @GetMapping(path = "/text")
    public Response getReportText(@RequestParam("reportId") String reportId) throws Exception {
        return investCloudClient.getMailReportText(reportId);
    }

    @GetMapping(path = "/analyst/target/count")
    Response<Long> getAnalystTargetReportCount(@RequestParam("startTime") String startTime,
                                               @RequestParam("endTime") String endTime) throws Exception {
        return investCloudClient.getAnalystTargetReportCount(startTime, endTime);
    }

    @GetMapping("/analyst/target/list")
    Response getAnalystTargetReportList(@RequestParam("startTime") String startTime,
                                        @RequestParam("endTime") String endTime,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit) throws Exception {
        return investCloudClient.getAnalystTargetReportList(startTime, endTime, page, limit);
    }
}
