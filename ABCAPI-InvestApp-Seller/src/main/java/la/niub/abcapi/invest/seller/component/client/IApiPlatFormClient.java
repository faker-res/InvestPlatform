package la.niub.abcapi.invest.seller.component.client;

import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.seller.model.bo.StockMonthReturnRateBO;
import la.niub.abcapi.invest.seller.model.reporter.SecBasicInfoModel;
import la.niub.abcapi.invest.seller.model.reporter.SecIndustryNewModel;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 公网数据
 */
@FeignClient(name = "${feign.client.platform.name}", url = "${feign.client.platform.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IApiPlatFormClient {
    @GetMapping(value = "/account/company")
    Response<CompanyResponse> getAccountCompany(@RequestParam("userId") String userId);

    @GetMapping(value = "/data/broker")
    Response<List<CompanyResponse>> broker();

    @GetMapping(value = "/account/list")
    Response<AccountListResponse> getAccountListByKeyword(@RequestParam("userId") String userId,
                                                          @RequestParam("keyword") String keyword,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "limit" ,defaultValue = "10") Integer limit);

    @GetMapping(path = "/stock/getMonthReturnRate")
    Response<Map<Long, StockMonthReturnRateBO>> getMonthReturnRate(@RequestParam("sec_uni_codes") String sec_uni_codes,
                                                                   @RequestParam(value = "month",required = false) String month);

    @GetMapping(path = "/stock/getPriceByDay")
    Response<Map<Long, KlineResponse>> getPriceByDay(@RequestParam("sec_uni_codes") String sec_uni_codes, @RequestParam(value = "day",required = false) String day);

    @GetMapping(path = "/stock/getByNameOrCode")
    Response<List<SecBasicInfoResponse>> getStockByNameOrCode(@RequestParam("names_or_codes") String names_or_codes);

    @PostMapping(path = "/stock/getByNameOrCode")
    Response<List<SecBasicInfoModel>> getStockByNameOrCode(@RequestBody List<String> names_or_codes);

    @GetMapping(path = "/stock/getIndustry")
    Response<Map<Long, SecIndustryNewModel>> getIndustry(@RequestParam("sec_uni_codes") String sec_uni_codes);

    @GetMapping(path = "/stock/getKline")
    Response<Map<String, KlineResponse>> getKline(@RequestParam(value = "start_time",required = false) String startTime,
                                                  @RequestParam(value = "stock_code") String stockCode,
                                                  @RequestParam(value = "line_type") String lineType);

    @GetMapping(path = "/analyst/innerreport/count")
    Response<List<AnalystReportCountResponse>> innerReportCount(@RequestParam(value = "company_name") String company_name,
                                                                @RequestParam(value = "name") String name,
                                                                @RequestParam(value = "start_time",required = false) String startTime,
                                                                @RequestParam(value = "end_time",required = false) String endTime);

    @GetMapping(path = "/account/info")
    Response<AccountInfoResponse> getAccountInfo(@RequestParam(value = "userId") String userId);

    @GetMapping(path = "/company/user")
    Response<List<AccountInfoResponse>> getCompanyUserList(@RequestParam(value = "company_id") Long company_id, @RequestParam(value = "company_type") CompanyTypeEnum company_type);

    @GetMapping(path = "/company")
    Response<List<CompanyResponse>> getCompanyListByCompanyType(@RequestParam(value = "company_type") CompanyTypeEnum company_type);

    @GetMapping(path = "/data/industry")
    Response<List<IndustryResponse>> industry();

    @GetMapping(path = "/suggestion/entity")
    Response suggestEntity(@RequestParam("keyword") String keyword, @RequestParam("category") String category);

    @PostMapping(path = "/mail/report/search")
    Response<MailReportSearchResponse> mailReportSearch(@RequestBody String param);

    @GetMapping(path = "/mail/report/facet")
    Response<Map<String, List<String>>> mailReportSearchFacet(@RequestParam("type") String type, @RequestParam("userId") String userId);

    @GetMapping(path = "/mail/report/hot/industry")
    Response<List<Map<String, Object>>> getMailReportHotIndustry(@RequestParam("type") String type, @RequestParam("userId") String userId);

    @GetMapping(path = "/mail/report/hot/company")
    Response<List<Map<String, Object>>> getMailReportHotCompany(@RequestParam("type") String type, @RequestParam("userId") String userId);

    @GetMapping(path = "/mail/report/detail")
    Response<MailReportResponse> getMailReportDetail(@RequestParam("reportId") String reportId);

    @GetMapping(path = "/mail/report/chart")
    Response<List<MailReportChartsResponse>> getMailReportChart(@RequestParam("reportId") String reportId);

    @GetMapping(path = "/mail/report/table")
    Response<List<MailReportTablesResponse>> getMailReportTable(@RequestParam("reportId") String reportId);

    @GetMapping(path = "/mail/report/text")
    Response<MailReportTextResponse> getMailReportText(@RequestParam("reportId") String reportId);

    @GetMapping(path = "/mail/report/analyst/target/count")
    Response<Long> getAnalystTargetReportCount(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);

    @GetMapping("/mail/report/analyst/target/list")
    Response<List<MailReportTextResponse>> getAnalystTargetReportList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit);

    @GetMapping(path = "/stock/getTradeDateByPrice")
    Response<KlineResponse> getTradeDateByPrice(@RequestParam("secUniCode") Long secUniCode, @RequestParam("startTime") String startTime, @RequestParam("targetPrice") BigDecimal targetPrice, @RequestParam("currentPrice") BigDecimal currentPrice);


    @GetMapping(path = "/operate-config/keyword-query")
    Response getOperateConfigKeywordQuery(@RequestParam("type") Integer type,
                                          @RequestParam("module") Integer module,
                                          @RequestParam("cate") Integer cate,
                                          @RequestParam("terminal") Integer terminal,
                                          @RequestParam("lan") Integer lan,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit);
}
