package la.niub.abcapi.invest.platform.component.client;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 智能投研
 */
@FeignClient(name = "${feign.client.modeling.name}", url = "${feign.client.modeling.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface IModelingClient {

//    @GetMapping(value = "/resapi/mail/list")
//    ModelingResapiResponse<List<MailConfigResponse>> getMailList(@RequestParam("uid") String userId);
//
//    @PostMapping(value = "/resapi/mail/bind")
//    ModelingResapiResponse bindMail(@RequestBody MailBindRequest request);
//
//    @PostMapping(value = "/resapi/mail/delete")
//    ModelingResapiResponse deleteEmailConfig(@RequestBody MailDeleteRequest request);
//
//    @PostMapping(value = "/resapi/mail/update")
//    ModelingResapiResponse updateEmailConfig(@RequestBody MailUpdateRequest request);
//
//    @PostMapping(value = "/resapi/mail/enable")
//    ModelingResapiResponse enableEmailConfig(@RequestBody MailEnableRequest request);
//
//    @PostMapping(value = "/resapi/mail/disable")
//    ModelingResapiResponse disableEmailConfig(@RequestBody MailDisableRequest request);

}
