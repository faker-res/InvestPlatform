package la.niub.abcapi.invest.seller.component.client;

import la.niub.abcapi.invest.seller.model.request.client.AnalystReportNumRequest;
import la.niub.abcapi.invest.seller.model.request.client.AnalystReportRequest;
import la.niub.abcapi.invest.seller.model.request.client.InvestReportExternalRequest;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.AnalystReportResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 研报管理
 */
@FeignClient(name = "${feign.client.report.name}", url = "${feign.client.report.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IInvestReportClient {

    @PostMapping(value = "/api/external")
    Response reportExternal(@RequestBody InvestReportExternalRequest request);

    @PostMapping(value = "/api/batchExternal")
    Response reportBatchExternal(@RequestBody List<InvestReportExternalRequest> request);

    @PostMapping(value = "/api/relatedAnalystReportNum")
    Response<Integer> relatedAnalystReportNum(@RequestBody AnalystReportNumRequest request);

    @PostMapping(value = "/api/relatedAnalystReport")
    Response<AnalystReportResponse> relatedAnalystReport(@RequestBody AnalystReportRequest request);
}
