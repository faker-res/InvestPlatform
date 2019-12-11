package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.AccountInfoResponse;
import la.niub.abcapi.invest.seller.model.response.client.CompanyResponse;
import la.niub.abcapi.invest.seller.model.vo.UserInfoVo;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 获取用户信息service
 * @date : 2019-01-24 10:24
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;


    @Override
    public CompanyResponse getUserCompanyInfo(String userId) throws Exception {
        Response<CompanyResponse> response = apiPlatFormClient.getAccountCompany(userId);
        logger.info("从平台获取用户公司信息返回值:" + JSONObject.toJSONString(response));
        if (response == null || response.getCode() != 200 ||
                response.getData() == null || StringUtil.isEmpty(response.getData().getCompany_id())) {
            throw new RuntimeException("从平台获取用户公司信息失败,返回值：" + JSONObject.toJSONString(response));
        }
        return response.getData();
    }

    @Override
    public String getUserParentId(String userId) throws Exception {
        CompanyResponse userCompanyInfo = getUserCompanyInfo(userId);
        return userCompanyInfo.getCompany_id().toString();
    }

    @Override
    public List<UserInfoVo> getUserInfoList(String companyId, CompanyTypeEnum companyType) throws Exception {
        Response<List<AccountInfoResponse>> response = apiPlatFormClient.getCompanyUserList(Long.parseLong(companyId), companyType);
        logger.info("从平台获取公司下用户信息返回值:" + JSONObject.toJSONString(response));
        if (response == null || response.getCode() != 200 ||
                response.getData() == null || StringUtil.isEmpty(response.getData())) {
            throw new RuntimeException("从平台获取公司下用户信息失败,返回值：" + JSONObject.toJSONString(response));
        }
        List<AccountInfoResponse> dataList = response.getData();
        List<UserInfoVo> result = new ArrayList<>();

        for (AccountInfoResponse item : dataList) {
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserId(item.getUser_id());
            userInfoVo.setUsername(item.getUsername());

            CompanyResponse userCompanyInfo = getUserCompanyInfo(item.getUser_id());
            userInfoVo.setParentId(userCompanyInfo.getCompany_id().toString());
            userInfoVo.setParentName(userCompanyInfo.getSname());

            result.add(userInfoVo);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getFundCompanyList(CompanyTypeEnum companyType) throws Exception {
        Response<List<CompanyResponse>> response = apiPlatFormClient.getCompanyListByCompanyType(companyType);
        logger.info("从平台获取注册的基金公司信息返回值:" + JSONObject.toJSONString(response));
        if (response == null || response.getCode() != 200 ||
                response.getData() == null || StringUtil.isEmpty(response.getData())) {
            throw new RuntimeException("从平台获取注册的基金公司信息失败,返回值：" + JSONObject.toJSONString(response));
        }

        List<Map<String, String>> result = new ArrayList<>();
        List<CompanyResponse> dataList = response.getData();
        dataList.forEach(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("userId", item.getCompany_id().toString());
            map.put("username", item.getSname());

            result.add(map);
        });
        return result;
    }

    @Override
    public AccountInfoResponse getUserInfo(String userId) throws Exception {
        Response<AccountInfoResponse> response = apiPlatFormClient.getAccountInfo(userId);
        logger.info("从平台获取用户信息返回值:" + JSONObject.toJSONString(response));
        if (response == null || response.getCode() != 200 || response.getData() == null) {
            throw new RuntimeException("从平台获取用户信息失败,返回值：" + JSONObject.toJSONString(response));
        }
        return response.getData();
    }
}
