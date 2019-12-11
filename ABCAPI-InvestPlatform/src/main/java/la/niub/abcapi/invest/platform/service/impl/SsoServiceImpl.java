package la.niub.abcapi.invest.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import la.niub.abcapi.invest.platform.component.client.ISsoClient;
import la.niub.abcapi.invest.platform.component.util.HttpUtil;
import la.niub.abcapi.invest.platform.component.util.RegexUtil;
import la.niub.abcapi.invest.platform.model.bo.sso.UserTokenBO;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoLoginResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoResponse;
import la.niub.abcapi.invest.platform.service.ISsoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SsoServiceImpl implements ISsoService {

    private String userId;

    private String token;

    @Autowired
    private ISsoClient ssoClient;

    @Override
    public UserTokenBO getUserToken() {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)
                || userLoginStatus(userId, token) == false) {
            SsoLoginResponse ssoLoginResponse = login("test@abcft.com","12345678");
            if (ssoLoginResponse == null){
                return null;
            }
            if (StringUtils.isEmpty(ssoLoginResponse.getUserId()) || StringUtils.isEmpty(ssoLoginResponse.getToken())){
                return null;
            }
            userId = ssoLoginResponse.getUserId();
            token = ssoLoginResponse.getToken();
        }

        UserTokenBO userTokenBO = new UserTokenBO();
        userTokenBO.setUserId(userId);
        userTokenBO.setToken(token);
        return userTokenBO;
    }

    private SsoLoginResponse login(String account, String password) {
        SsoResponse<SsoLoginResponse> ssoResponse = null;
        if (RegexUtil.isMobileNumber(account)){
            ssoResponse = ssoClient.mobileLogin(account,password);
        }else{
            ssoResponse = ssoClient.emailLogin(account,password);
        }
        if (ssoResponse == null || !ssoResponse.getErrorCode().equals(0)){
            return null;
        }
        return ssoResponse.getData();
    }

    /**
     * 调用sso 验证token
     * @param userId
     * @param token
     * @return
     */
    private boolean userLoginStatus(String userId, String token) {
        SsoResponse ssoResponse = ssoClient.verifyToken(userId,token);
        if (ssoResponse.getErrorCode() == 0) {
            return true;
        }
        return false;
    }

    private Map<String,UserTokenBO> userTokenMap = new HashMap<>();

    @Value("${feign.client.sso.url}")
    private String ssoDomain;

    @Override
    public UserTokenBO getUserToken(String env) {
        if (env.equalsIgnoreCase("pre")){
            env = "test";
        }
        String domain = "https://passport.abcfintech.com";
        if (env.equalsIgnoreCase("test")){
            domain = "https://passport-pre.abcfintech.com";
        }else if (env.equalsIgnoreCase("dev")){
            domain = "https://passport-dev.abcfintech.com";
        }else if (env.equalsIgnoreCase("local")){
            domain = ssoDomain;
        }

        UserTokenBO userTokenBO = userTokenMap.get(env);
        if (userTokenBO != null){
            try{
                Map<String, String> params = new HashMap<>();
                params.put("userId",userTokenBO.getUserId());
                params.put("token",userTokenBO.getToken());
                String url = domain+"/sso/verifyToken";
                String result = HttpUtil.post(url,params,null);
                SsoResponse ssoResponse = JSON.parseObject(result,SsoResponse.class);
                if (ssoResponse.getErrorCode() == 0) {
                    return userTokenBO;
                }
            }catch (Exception e){};
        }

        Map<String, String> params = new HashMap<>();
        params.put("email","test@abcft.com");
        params.put("password","12345678");
        String url = domain+"/sso/email/login";
        String result = HttpUtil.post(url,params,null);
        if (StringUtils.isEmpty(result)){
            return null;
        }
        SsoResponse<SsoLoginResponse> ssoResponse = JSON.parseObject(result,new TypeReference<SsoResponse<SsoLoginResponse>>(){});
        if (ssoResponse == null || !ssoResponse.getErrorCode().equals(0)){
            return null;
        }
        SsoLoginResponse ssoLoginResponse = ssoResponse.getData();
        if (ssoLoginResponse == null){
            return null;
        }
        if (StringUtils.isEmpty(ssoLoginResponse.getUserId()) || StringUtils.isEmpty(ssoLoginResponse.getToken())){
            return null;
        }
        userTokenBO = new UserTokenBO();
        userTokenBO.setUserId(ssoLoginResponse.getUserId());
        userTokenBO.setToken(ssoLoginResponse.getToken());
        userTokenMap.put(env,userTokenBO);
        return userTokenBO;
    }
}
