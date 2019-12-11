package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.model.request.client.ConfigBindRequest;
import la.niub.abcapi.invest.platform.model.request.client.ConfigUpdateRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.MailConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 智能投研
 */
@FeignClient(name = "service-invest-data-email"
//        , url = "http://localhost:9851"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IInvestDataEmailClient {

    @PostMapping(path = "/config/bind")
    Response configBind(@RequestBody ConfigBindRequest request);

    @PostMapping(path = "/config/update")
    Response configUpdate(@RequestBody ConfigUpdateRequest request);

    @GetMapping(path = "/config/unbind")
    Response configUnbind(@RequestParam("config_id") Integer config_id);

    @GetMapping(path = "/config/enable")
    Response configEnable(@RequestParam("config_id") Integer config_id);

    @GetMapping(path = "/config/disable")
    Response configDisable(@RequestParam("config_id") Integer config_id);

    @GetMapping(path = "/config/list")
    Response<List<MailConfig>> configList(@RequestParam("userId") String userId);
}
