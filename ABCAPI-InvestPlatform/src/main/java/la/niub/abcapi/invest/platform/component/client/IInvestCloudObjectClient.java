package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.config.configuration.FeignObjectConfiguration;
import la.niub.abcapi.invest.platform.model.bo.stock.StockMonthReturnRateBO;
import la.niub.abcapi.invest.platform.model.reporter.SecBasicInfoModel;
import la.niub.abcapi.invest.platform.model.reporter.SecIndustryNewModel;
import la.niub.abcapi.invest.platform.model.request.client.KeyWordRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetMonthReturnRateRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetPriceByDayRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.modeling.MailReportChartsResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.MailReportResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.MailReportSearchResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.MailReportTablesResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.MailReportTextResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 智能投研
 */
@FeignClient(name = "IInvestCloudObjectClient", url = "${feign.client.investCloud.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
        , configuration = FeignObjectConfiguration.class
)
public interface IInvestCloudObjectClient {

    @GetMapping(path = "/operate-config/keyword-query")
    Response queryWithKeyWord(@PathVariable(value = "default") KeyWordRequest request);
}
