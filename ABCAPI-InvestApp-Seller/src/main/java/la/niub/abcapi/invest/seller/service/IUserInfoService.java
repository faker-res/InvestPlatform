package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.seller.model.response.client.AccountInfoResponse;
import la.niub.abcapi.invest.seller.model.response.client.CompanyResponse;
import la.niub.abcapi.invest.seller.model.vo.UserInfoVo;

import java.util.List;
import java.util.Map;

public interface IUserInfoService {

    CompanyResponse getUserCompanyInfo(String userId) throws Exception;

    String getUserParentId(String userId) throws Exception;

    List<UserInfoVo> getUserInfoList(String companyId, CompanyTypeEnum companyType) throws Exception;

    List<Map<String, String>> getFundCompanyList(CompanyTypeEnum companyType) throws Exception;

    AccountInfoResponse getUserInfo(String userId) throws Exception;
}
