package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.model.bo.stock.StockMonthReturnRateBO;
import la.niub.abcapi.invest.platform.model.reporter.SecBasicInfoModel;
import la.niub.abcapi.invest.platform.model.reporter.SecIndustryNewModel;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetMonthReturnRateRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetPriceByDayRequest;
import la.niub.abcapi.invest.platform.model.request.mystock.StockPricesRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.modeling.*;
import la.niub.abcapi.invest.platform.model.response.client.stock.SecPlateInfoResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 智能投研
 */
@FeignClient(name = "${feign.client.investCloud.name}", url = "${feign.client.investCloud.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IInvestCloudClient {

    @PostMapping(value = "/stock/getStockByNameOrCode")
    Response<List<SecBasicInfoModel>> getStockByNameOrCode(@RequestBody List<String> nameOrCode);

    @PostMapping(path = "/stock/getIndustry")
    Response<Map<Long, SecIndustryNewModel>> getIndustry(@RequestBody List<Long> secUniCodes);

    @PostMapping(path = "/stock/getPriceByDay")
    Response getPriceByDay(@RequestBody StockGetPriceByDayRequest request);

    @PostMapping(path = "/stock/getMonthReturnRate")
    Response<Map<Long, StockMonthReturnRateBO>> getMonthReturnRate(@RequestBody StockGetMonthReturnRateRequest request);

    @GetMapping(path = "/analyst/innerreport/count")
    Response<Map<String,Long>> innerReportCount(@RequestParam(value = "name") String name,
                                                @RequestParam(value = "start_time") String startTime,
                                                @RequestParam(value = "end_time") String endTime);

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
    Response getAnalystTargetReportList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit);

    @GetMapping(path = "/stock/getTradeDateByPrice")
    Response getTradeDateByPrice(@RequestParam("secUniCode") Long secUniCode, @RequestParam("startTime") String startTime, @RequestParam("targetPrice") BigDecimal targetPrice, @RequestParam("currentPrice") BigDecimal currentPrice);

    @GetMapping(path = "/stock/getSecUniCodeByMarketCode")
    Response getSecUniCodeByMarketCode(@RequestParam("marketId") Integer marketId);

    @GetMapping(path = "/stock/getSecIndexPlateInfo")
    Response<List<SecPlateInfoResponse>> getSecIndexPlateInfoByPlateCode(@RequestParam("plateCode") String plateCode);

    @GetMapping(value = "/suggestion/mystock")
    Response searchStockGroup(@RequestParam("keyword") String keyword, @RequestParam("marketCode") String marketCode, @RequestParam("limit") Integer limit);

    @GetMapping(value = "/suggestion/mystock/v2")
    Response searchStockV2(@RequestParam("keyword") String keyword, @RequestParam("category") String category, @RequestParam("limit") Integer limit);

    @GetMapping(value = "/market/categories")
    Response getMarketCategory(@RequestParam("level") Integer level);

    @GetMapping( value = "/market/categories/items")
    Response getMarketCategoryItemList(@RequestParam("plateCode") String plateCode);

    @GetMapping(path = "/stock/getByCodeName")
    Response getByCodeName(@RequestParam("stockCodes") String stockCodes, @RequestParam("stockNames") String stockNames);

    @PostMapping(value = "/stock/stock-stat-card")
    Response getPricesByStocks(@RequestBody StockPricesRequest stockPricesRequest);

    @GetMapping(value = "/stock/stock-card-base")
    Response stockCardBase(@RequestParam("stockCode") String stockCode, @RequestParam("secUniCode") String secUniCode);

    @GetMapping(value = "/stock/industry")
    Response getSWFirstIndustryInfo(@RequestParam("secUniCode") Long secUniCode);

    @GetMapping(value = "/index/dayprice")
    Response getIndexDayPriceByIndexUniCode(@RequestParam("indexUniCode") Long indexUniCode, @RequestParam("day") String day);
}
