package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.model.request.wechat.WechatGroupsRequest;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingWechatBindedResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingWechatGroupResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * modeling
 */
@FeignClient(name = "IWechatReadingClient", url = "${feign.client.modeling.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IWechatReadingClient {

    @GetMapping(value = "/wechatapi/sync/wechat/get/binded/wechats?developer=true")
    ModelingResponse<ModelingWechatBindedResponse> binded(@RequestParam("userId") String userId);

    @PostMapping(value = "/wechatapi/sync/wechat/get/wechat/groups?developer=true")
    ModelingResponse<ModelingWechatGroupResponse> groups(@RequestBody WechatGroupsRequest request);

}
