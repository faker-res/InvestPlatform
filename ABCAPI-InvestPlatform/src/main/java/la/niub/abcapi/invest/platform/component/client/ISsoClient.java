package la.niub.abcapi.invest.platform.component.client;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoApiUserinfoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoLoginResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoUserInfoResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * sso
 */
@FeignClient(name = "${feign.client.sso.name}", url = "${feign.client.sso.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface ISsoClient {

    @PostMapping(value = "/sso/mobile/login")
    SsoResponse<SsoLoginResponse> mobileLogin(@RequestParam("mobile") String mobile, @RequestParam("password") String password);

    @PostMapping(value = "/sso/email/login")
    SsoResponse<SsoLoginResponse> emailLogin(@RequestParam("email") String email, @RequestParam("password") String password);

    @PostMapping(value = "/sso/verifyToken")
    SsoResponse verifyToken(@RequestParam("userId") String userId, @RequestParam("token") String token);

    @GetMapping(value = "/sso/api/user/info")
    SsoResponse<SsoApiUserinfoResponse> userInfo(@RequestParam("code") String code, @RequestParam("userId") String userId,
                                                 @RequestParam("email") String email, @RequestParam("mobile") String mobile);

    @PostMapping(value = "/sso/getUserInfoByToken")
    SsoResponse<SsoUserInfoResponse> getUserInfoByToken(@RequestParam("userId") String userId, @RequestParam("token") String token);

    @GetMapping(value = "/sso/mobile/canRegister")
    SsoResponse canRegister(@RequestParam("mobile") String mobile);

    @PostMapping(value = "/sso/mobile/sendMsgCode")
    SsoResponse sendMsgCode(@RequestParam("mobile") String mobile);

    @PostMapping(value = "/sso/mobile/register/validateMsgCode")
    SsoResponse registerValidateMsgCode(@RequestParam("mobile") String mobile, @RequestParam("msgCode") String msgCode);

    @PostMapping(value = "/sso/mobile/register")
    SsoResponse<SsoUserInfoResponse> mobileRegister(@RequestParam("mobile") String mobile, @RequestParam("password") String password);

    @PostMapping(value = "/sso/initInfo")
    SsoResponse updateInfo(@RequestParam("userId") String userId, @RequestParam("userInfoStr") String userInfoStr);

    @GetMapping(value = "/sso/backStage/passExamine")
    SsoResponse enable(@RequestParam("email") String email,
                           @RequestParam("mobile") String mobile, @RequestParam("md5") String md5);

    @GetMapping(value = "/sso/backStage/activateEmail")
    SsoResponse activateEmail(@RequestParam("email") String email,
                            @RequestParam("mobile") String mobile, @RequestParam("md5") String md5);

    @PostMapping(value = "/sso/private/delUser")
    SsoResponse delUser(@RequestParam("userId") String userId, @RequestParam("code") String code);

    @PostMapping(value = "/sso/private/addUser")
    SsoResponse<Map<String, String>> addUser(@RequestBody JSONObject requestJson);

    @GetMapping(value = "/sso/backStage/disable")
    SsoResponse disable(@RequestParam("email") String email,
                            @RequestParam("mobile") String mobile, @RequestParam("md5") String md5);
}
