package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.ISsoClient;
import la.niub.abcapi.invest.platform.component.util.RegexUtil;
import la.niub.abcapi.invest.platform.config.code.AccountEnumCodeConfig;
import la.niub.abcapi.invest.platform.config.enums.CompanyTypeEnum;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 公司服务
 */
@RestController
@RequestMapping(path = "/company")
public class CompanyController {

    private final static Logger logger = LogManager.getLogger(CompanyController.class);

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IDataService dataService;

    @Autowired
    private ISsoClient ssoClient;

    /**
     * 获取已注册的公司
     * @return
     */
    @GetMapping(path = "")
    Response list(CompanyTypeEnum company_type) {
        try{
            List<CompanyBO> companyBOList = new ArrayList<>();
            if (company_type.equals(CompanyTypeEnum.FUND)){
                companyBOList = accountService.getFundUserCompanyList();
            }else if (company_type.equals(CompanyTypeEnum.BROKER)){
                companyBOList = accountService.getBrokerUserCompanyList();
            }
            return new Response(companyBOList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取公司下的人员
     * @return
     */
    @GetMapping(path = "/user")
    Response user(Long company_id,CompanyTypeEnum company_type) {
        try{
            List<AccountBO> accountBOList =  accountService.companyUserList(company_id,company_type.name());
            return new Response(accountBOList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
}
