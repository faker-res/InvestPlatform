package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.ISsoClient;
import la.niub.abcapi.invest.platform.component.util.RegexUtil;
import la.niub.abcapi.invest.platform.config.code.AccountEnumCodeConfig;
import la.niub.abcapi.invest.platform.model.bo.account.AccountBO;
import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewUserModel;
import la.niub.abcapi.invest.platform.model.request.account.AccountAddRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountCompleteInfoRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountMobileRegisterRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountModifyRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountRegisterRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.account.AccountListResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoLoginResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoResponse;
import la.niub.abcapi.invest.platform.service.IAccountService;
import la.niub.abcapi.invest.platform.service.IDataService;
import la.niub.abcapi.invest.platform.service.ISsoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户服务
 */
@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final static Logger logger = LogManager.getLogger(AccountController.class);

    @Value("${feign.client.sso.url}")
    private String ssoUrl;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IDataService dataService;

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private ISsoClient ssoClient;

    @GetMapping(path = "/ssotoken")
    Response ssoToken(@RequestParam(value = "env",required = false) String env){
        if (StringUtils.isNotEmpty(env)){
            return new Response(ssoService.getUserToken(env));
        }
        return new Response(ssoService.getUserToken());
    }

    /**
     * 获取账户信息
     * @param userId
     * @return
     */
    @GetMapping(path = "/info")
    Response info(String userId){
        try{
            AccountBO accountBO = accountService.userInfoByUserId(userId);
            if (accountBO == null){
                accountService.syncUser(userId,null);
                accountBO = accountService.userInfoByUserId(userId);
            }
            return new Response(accountBO);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取公司id
     * @param userId
     * @return
     */
    @GetMapping(path = "/company")
    Response company(String userId){
        InvestnewUserModel investnewUserModel = accountService.userModelByUserId(userId);
        if (investnewUserModel == null || investnewUserModel.getCompany_id() == null){
            return new Response();
        }

        CompanyBO companyBO = dataService.findCompany(investnewUserModel.getCompany_id(),investnewUserModel.getCompany_type());
        return new Response(companyBO);
    }

    /**
     * 同公司账号列表
     * @return
     */
    @GetMapping(path = "/list")
    Response list(String userId, String keyword, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Long companyId = accountService.getUserCompanyId(userId);
        if (companyId == null){
            return new ErrorResponse(AccountEnumCodeConfig.MISS_COMPANY);
        }

        AccountListResponse accountListResponse =  accountService.companyUserList(companyId,keyword,page,limit);

        return new Response(accountListResponse);
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    @GetMapping(path = "/login")
    Response login(String account,String password) {
        try{
            SsoResponse<SsoLoginResponse> ssoResponse = null;
            if (RegexUtil.isMobileNumber(account)){
                ssoResponse = ssoClient.mobileLogin(account,password);
            }else{
                ssoResponse = ssoClient.emailLogin(account,password);
            }
            if (ssoResponse == null || !ssoResponse.getErrorCode().equals(0)){
                return new ErrorResponse(AccountEnumCodeConfig.BACK_LOGIN_FAIL);
            }
            SsoLoginResponse ssoLoginResponse = ssoResponse.getData();
            if (ssoLoginResponse != null){
                accountService.syncUser(ssoLoginResponse.getUserId(),password);
            }
            return new Response(ssoLoginResponse);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 发送手机验证码
     * @return
     */
    @Deprecated
    @GetMapping(path = "/mobileregister/sendcode")
    Response sendCode(String mobile) {
        SsoResponse ssoResponse = ssoClient.canRegister(mobile);
        if (!ssoResponse.getErrorCode().equals(0)){
            return new ErrorResponse(ssoResponse.getMsg());
        }
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile",mobile);
//        String ret = HttpUtil.post(ssoUrl+"/sso/mobile/sendMsgCode",params,null);
//        return new Response(ret);
        ssoResponse = ssoClient.sendMsgCode(mobile);
        return ssoResponse.getErrorCode().equals(0) ? new Response(true) : new ErrorResponse();
    }

    /**
     * 手机注册
     * @return
     */
    @Deprecated
    @RequestMapping(path = "/mobileregister")
    Response mobileRegister(AccountMobileRegisterRequest param) {
        try{
            SsoResponse ssoResponse = ssoClient.registerValidateMsgCode(param.getMobile(),param.getMobile_code());
            if (!ssoResponse.getErrorCode().equals(0)){
                return new ErrorResponse(ssoResponse.getMsg());
            }

            String userId = accountService.mobileRegister(param);
            if (userId == null){
                return new ErrorResponse("用户注册失败");
            }
            return new Response((Object)userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 无手机/邮箱验证直接注册新账户
     * @param param
     * @return
     */
    @RequestMapping(path = "/register")
    Response register(AccountRegisterRequest param) {
        try {
            String userId = accountService.register(param);
            if (userId == null){
                return new ErrorResponse("用户注册失败");
            }
            return new Response((Object)userId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 添加账户
     * @param param
     * @return
     */
    @PostMapping(path = "/add")
    Response add(@RequestBody AccountAddRequest param) {
        logger.info("account/add param:"+param);
        if (StringUtils.isEmpty(param.getUserId())){
            return new ErrorResponse("管理员userId是空");
        }

        try{
            String userId = accountService.addUser(param);
            if (userId == null){
                return new ErrorResponse("添加账户失败");
            }
            return new Response((Object)userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 修改账户
     * @param param
     * @return
     */
    @PostMapping(path = "/modify")
    Response modify(@RequestBody AccountModifyRequest param) {
        if (param.getId() == null){
            return new ErrorResponse("账户id是空");
        }

        try {
            Boolean ret = accountService.modifyUser(param);
            return new Response(ret);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 删除账户
     * @param id
     * @return
     */
    @GetMapping(path = "/delete")
    Response delete(Integer id) {
        try {
            Boolean ret = accountService.deleteUser(id);
            return new Response(ret);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 完善个人信息
     * @param param
     * @return
     */
    @PostMapping(path = "/completeinfo")
    Response completeInfo(@RequestBody AccountCompleteInfoRequest param) {
        if (param.getCompany_id() == null || param.getCompany_type() == null){
            return new ErrorResponse(AccountEnumCodeConfig.MISS_COMPANY);
        }

        try{
            Boolean ret = accountService.completeInfo(param);
            return new Response(ret);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
}
