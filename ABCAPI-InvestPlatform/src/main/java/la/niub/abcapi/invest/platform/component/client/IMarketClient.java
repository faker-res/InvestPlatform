package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.KlineResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.RealTimeResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.TimelineResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 行情
 */
@FeignClient(name = "IMarketClient", url = "${feign.client.analyst.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IMarketClient {

    @GetMapping(value = "/api/quotes/rest/v1/kx")
    Response<KlineResponse> getKline(@RequestParam("stockCode") String stockCode,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("period") String period,
                                   @RequestParam("userId") String userId,
                                   @RequestParam("token") String token);

    @GetMapping(value = "/api/quotes/rest/v1/trending")
    Response<TimelineResponse> getTimeline(@RequestParam("stockCode") String stockCode,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("token") String token);

    @GetMapping(value = "/api/quotes/rest/v1/quote")
    Response<RealTimeResponse> getRealTime(@RequestParam("stockCode") String stockCode,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("token") String token);

}
