package la.niub.abcapi.invest.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.platform.component.client.ISsoClient;
import la.niub.abcapi.invest.platform.component.util.PasswordUtil;
import la.niub.abcapi.invest.platform.component.util.RsaUtil;
import la.niub.abcapi.invest.platform.config.code.AccountEnumCodeConfig;
import la.niub.abcapi.invest.platform.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.platform.config.enums.GenderEnum;
import la.niub.abcapi.invest.platform.config.enums.StatusEnum;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewUserDao;
import la.niub.abcapi.invest.platform.model.bo.access.RoleBO;
import la.niub.abcapi.invest.platform.model.bo.account.AccountBO;
import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import la.niub.abcapi.invest.platform.model.bo.sso.SsoInitInfoBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewUserModel;
import la.niub.abcapi.invest.platform.model.request.account.AccountAddRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountCompleteInfoRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountMobileRegisterRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountModifyRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountRegisterRequest;
import la.niub.abcapi.invest.platform.model.response.account.AccountListResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoApiUserinfoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoResponse;
import la.niub.abcapi.invest.platform.model.response.client.sso.SsoUserInfoResponse;
import la.niub.abcapi.invest.platform.service.IAccessService;
import la.niub.abcapi.invest.platform.service.IAccountService;
import la.niub.abcapi.invest.platform.service.IDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements IAccountService {

    private final static Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    private final String ssoPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJLrS7/xW+zabN+LC/HPuNJcmRO1Hf9ffWSyNfQWTF/wHt+e3z+0AKDKFz332JdozI55hOM/BHEPHB12phGqaUUCAwEAAQ==";
    private final String ssoPrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkutLv/Fb7Nps34sL8c+40lyZE7Ud/199ZLI19BZMX/Ae357fP7QAoMoXPffYl2jMjnmE4z8EcQ8cHXamEappRQIDAQABAkARF/E7RtCkMRGjvKDKJJnjV8Szp0nzMV/8fEOMv59pNfCww9AmjrFYW5LlfejxmjPaDD7mFG1uX0zdUb6fzY7BAiEAwkzqGr3t9VsrTs+/dBbZODW3akhSkPa2ESML8axh/HECIQDBkqfYM8l4ybPCDKC5qWGz1JvMTNqKAzyy3Bzh79X0FQIgEJZ2agAK/6Zf5SORSbzloInddcJk4iFd28qtK123I9ECIBvY0TSVHqK1wZpk3qpW56tLJq6ZT8cS+CRy7eTC7/CRAiEAu/3Lx6EGTVQSKpzeTQ1d73y9GNAusnkmXn6fuJe0G8A=";

    @Autowired
    private IDataService dataService;

    @Autowired
    private IAccessService accessService;

    @Autowired
    private ISsoClient ssoClient;

    @Autowired
    private IInvestnewUserDao investnewUserDao;

    @Override
    public InvestnewUserModel syncUser(String mobile, String email, String password) throws Exception {
        return syncUser("",StringUtils.defaultString(mobile,""),StringUtils.defaultString(email,""),password);
    }

    @Override
    public InvestnewUserModel syncUser(String userId,String password) throws Exception {
        if (StringUtils.isEmpty(userId)){
            throw new Exception("userId是空");
        }
        return syncUser(userId,"","",password);
    }

    private InvestnewUserModel syncUser(String userId,String mobile, String email,String password) throws Exception {
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userId", userId);
        codeParam.put("mobile", mobile);
        codeParam.put("email", email);
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        SsoResponse<SsoApiUserinfoResponse> ssoResponse = ssoClient.userInfo(realCode,userId,email,mobile);
        if (!ssoResponse.getErrorCode().equals(0)){
            throw new Exception("后端用户信息获取失败: "+ssoResponse.getMsg());
        }
        SsoApiUserinfoResponse ssoApiUserinfoResponse = ssoResponse.getData();
        if (ssoApiUserinfoResponse == null){
            throw new Exception("后端用户信息获取失败:"+ssoResponse);
        }

        InvestnewUserModel investnewUserModel = new InvestnewUserModel();
        investnewUserModel.setUser_id(ssoApiUserinfoResponse.getUserId());
        investnewUserModel.setUsername(ssoApiUserinfoResponse.getUserName());
        if (StringUtils.isNotEmpty(password)){
            String encryptPassword = RsaUtil.encryptData(password, ssoPublicKey);
            investnewUserModel.setPassword(encryptPassword);
        }
        investnewUserModel.setEmail(ssoApiUserinfoResponse.getEmail());
        investnewUserModel.setMobile(ssoApiUserinfoResponse.getMobile());
        investnewUserModel.setNickname(ssoApiUserinfoResponse.getUserName());
        investnewUserModel.setAvatar(ssoApiUserinfoResponse.getHeadImg());
        investnewUserModel.setDepartment(ssoApiUserinfoResponse.getDepartment());
        investnewUserModel.setPosition(ssoApiUserinfoResponse.getPosition());

        String companyName = ssoApiUserinfoResponse.getInstName();
        if (StringUtils.isNotEmpty(companyName)){
            CompanyBO companyBO = dataService.findCompany(companyName);
            if (companyBO != null){
                investnewUserModel.setCompany_id(companyBO.getCompany_id());
                investnewUserModel.setCompany_type(companyBO.getType().name().toLowerCase());
            }
        }

        Boolean ret = false;
        InvestnewUserModel existsModel = investnewUserDao.selectByUserId(userId);
        if (existsModel == null){
            ret = investnewUserDao.insertSelective(investnewUserModel) > 0;
        }else{
            investnewUserModel.setId(existsModel.getId());
            ret = investnewUserDao.updateByPrimaryKeySelective(investnewUserModel) > 0;
        }

        return ret ? investnewUserModel: null;
    }

    @Override
    public Boolean deleteUser(Integer id) throws Exception {
        InvestnewUserModel investnewUserModel = investnewUserDao.selectByPrimaryKey(id);
        if (investnewUserModel == null){
            throw new Exception(AccountEnumCodeConfig.USER_NO_EXISTS.getMessage());
        }
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userId", investnewUserModel.getUser_id());
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        SsoResponse ret = ssoClient.delUser(investnewUserModel.getUser_id(),realCode);
        if (!ret.getErrorCode().equals(0)){
            throw new Exception("sso删除账户失败");
        }
        return investnewUserDao.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Boolean completeInfo(AccountCompleteInfoRequest param) throws Exception {
        InvestnewUserModel investnewUserModel = userModelByUserId(param.getUserId());
        if (investnewUserModel == null){
            throw new Exception(AccountEnumCodeConfig.USER_NO_EXISTS.getMessage());
        }

        InvestnewUserModel model = new InvestnewUserModel();
        model.setId(investnewUserModel.getId());
        model.setUser_id(investnewUserModel.getUser_id());

        //@todo 同步修改sso的账户信息和公司信息
        if (StringUtils.isNotEmpty(param.getNickname())){
            model.setNickname(param.getNickname());
        }
        if (StringUtils.isNotEmpty(param.getEmail())){
            model.setEmail(param.getEmail());
        }
        model.setCompany_id(param.getCompany_id());
        model.setCompany_type(param.getCompany_type().name().toLowerCase());
        return investnewUserDao.updateByPrimaryKeySelective(model) > 0;
    }

    @Override
    public String mobileRegister(AccountMobileRegisterRequest param) throws Exception {
        String encryptPassword = RsaUtil.encryptData(param.getPassword(), ssoPublicKey);
        SsoResponse<SsoUserInfoResponse> ssoResponse = ssoClient.mobileRegister(param.getMobile(),encryptPassword);
        if (!ssoResponse.getErrorCode().equals(0) || ssoResponse.getData() == null){
            throw new Exception("后端用户注册失败");
        }
        String userId = ssoResponse.getData().getUserId();

        try{
            String md5 = PasswordUtil.md5Password(PasswordUtil.getCurrentTime());
            SsoInitInfoBO ssoInitInfoBO = new SsoInitInfoBO();
            ssoInitInfoBO.setName(param.getNickname());
            ssoInitInfoBO.setEmail(param.getEmail());
            ssoResponse = ssoClient.updateInfo(userId, JSON.toJSONString(ssoInitInfoBO));
            if (!ssoResponse.getErrorCode().equals(0)){
                throw new Exception("初始化用户信息失败: "+ssoResponse.getMsg());
            }
            if (StringUtils.isNotEmpty(param.getEmail())){
                ssoResponse = ssoClient.activateEmail(param.getEmail(),param.getMobile(),md5);
                if (!ssoResponse.getErrorCode().equals(0)){
                    throw new Exception("初始化用户邮箱失败: "+ssoResponse.getMsg());
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        try{
            String md5 = PasswordUtil.md5Password(PasswordUtil.getCurrentTime());
            ssoResponse = ssoClient.enable(null,param.getMobile(),md5);
            if (!ssoResponse.getErrorCode().equals(0)){
                throw new Exception("用户审核失败: "+ssoResponse.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        if (StringUtils.isNotEmpty(userId)){
            InvestnewUserModel investnewUserModel = new InvestnewUserModel();
            investnewUserModel.setUser_id(userId);
            investnewUserModel.setUsername(param.getNickname());
            investnewUserModel.setPassword(encryptPassword);
            investnewUserModel.setEmail(param.getEmail());
            investnewUserModel.setMobile(param.getMobile());
            investnewUserDao.insertSelective(investnewUserModel);
        }

        return userId;
    }

    @Override
    public String register(AccountRegisterRequest param) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",param.getNickname());
        jsonObject.put("password",param.getPassword());
        jsonObject.put("mobile",param.getMobile());
        jsonObject.put("email",param.getEmail());
        jsonObject.put("inst_name","");
        jsonObject.put("position","");
        jsonObject.put("department","");
        jsonObject.put("gender", GenderEnum.MALE.getGender());
        //需严格按插入顺序排序
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userName", jsonObject.getString("userName"));
        codeParam.put("password", jsonObject.getString("password"));
        codeParam.put("mobile", jsonObject.getString("mobile"));
        codeParam.put("email", jsonObject.getString("email"));
        codeParam.put("inst_name", jsonObject.getString("inst_name"));
        codeParam.put("position", jsonObject.getString("position"));
        codeParam.put("department", jsonObject.getString("department"));
        codeParam.put("gender", jsonObject.getString("gender"));
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        jsonObject.put("code",realCode);

        SsoResponse<Map<String, String>> ssoResponse = ssoClient.addUser(jsonObject);
        if (!ssoResponse.getErrorCode().equals(0) || ssoResponse.getData() == null){
            throw new Exception("sso创建用户出错:"+ssoResponse.getMsg());
        }
        String userId = ssoResponse.getData().get("userId");

        if (StringUtils.isNotEmpty(userId)){
            InvestnewUserModel model = new InvestnewUserModel();
            model.setUser_id(userId);
            model.setUsername(param.getNickname());
            String encryptPassword = RsaUtil.encryptData(param.getPassword(), ssoPublicKey);
            model.setPassword(encryptPassword);
            model.setEmail(param.getEmail());
            model.setMobile(param.getMobile());
            model.setNickname(param.getNickname());
            investnewUserDao.insertSelective(model);
        }

        return userId;
    }

    @Override
    public String addUser(AccountAddRequest param) throws Exception {
        InvestnewUserModel currentUser = userModelByUserId(param.getUserId());
        if (currentUser == null){
            throw new Exception("找不到当前用户信息");
        }
        CompanyBO companyBO = dataService.findCompany(currentUser.getCompany_id(),currentUser.getCompany_type());
        if (companyBO == null){
            throw new Exception("找不到当前用户公司信息");
        }

        SsoResponse canRegisterResponse = ssoClient.canRegister(param.getMobile());
        if (!canRegisterResponse.getErrorCode().equals(0)){
            throw new Exception("手机号已使用："+canRegisterResponse.getMsg());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",param.getNickname());
        jsonObject.put("password",param.getPassword());
        jsonObject.put("mobile",param.getMobile());
        jsonObject.put("email",param.getEmail());
        jsonObject.put("inst_name",companyBO.getSname());
        jsonObject.put("position","");
        jsonObject.put("department","");
        jsonObject.put("gender", GenderEnum.MALE.getGender());
        //需严格按插入顺序排序
        Map<String, Object> codeParam = new LinkedHashMap<>();
        codeParam.put("userName", jsonObject.getString("userName"));
        codeParam.put("password", jsonObject.getString("password"));
        codeParam.put("mobile", jsonObject.getString("mobile"));
        codeParam.put("email", jsonObject.getString("email"));
        codeParam.put("inst_name", jsonObject.getString("inst_name"));
        codeParam.put("position", jsonObject.getString("position"));
        codeParam.put("department", jsonObject.getString("department"));
        codeParam.put("gender", jsonObject.getString("gender"));
        String realCode = PasswordUtil.getCodeMd5(codeParam);
        jsonObject.put("code",realCode);

        SsoResponse<Map<String, String>> ssoResponse = ssoClient.addUser(jsonObject);
        if (!ssoResponse.getErrorCode().equals(0) || ssoResponse.getData() == null){
            throw new Exception("sso创建用户出错:"+ssoResponse.getMsg());
        }
        String userId = ssoResponse.getData().get("userId");

        if (StringUtils.isNotEmpty(userId)){
            InvestnewUserModel model = new InvestnewUserModel();
            model.setUser_id(userId);
            model.setCompany_id(currentUser.getCompany_id());
            model.setCompany_type(currentUser.getCompany_type());
            model.setUsername(param.getNickname());
            String encryptPassword = RsaUtil.encryptData(param.getPassword(), ssoPublicKey);
            model.setPassword(encryptPassword);
            model.setEmail(param.getEmail());
            model.setMobile(param.getMobile());
            model.setNickname(param.getNickname());
            model.setStatus(param.getStatus());
            investnewUserDao.insertSelective(model);

            if (StringUtils.isNotEmpty(param.getRole_ids())){
                accessService.grantRole(userId,param.getRole_ids());
            }

            handleUserStatus(model,param.getStatus());
        }

        return userId;
    }

    @Override
    public Boolean modifyUser(AccountModifyRequest param) throws Exception {
        InvestnewUserModel investnewUserModel = investnewUserDao.selectByPrimaryKey(param.getId());
        if (investnewUserModel == null){
            throw new Exception(AccountEnumCodeConfig.USER_NO_EXISTS.getMessage());
        }

        //@todo 同步修改sso的账户信息和密码
        InvestnewUserModel model = new InvestnewUserModel();
        model.setId(investnewUserModel.getId());
        model.setNickname(param.getNickname());
        model.setStatus(param.getStatus());

        if (StringUtils.isNotEmpty(param.getPassword())){
            String encryptPassword = RsaUtil.encryptData(param.getPassword(), ssoPublicKey);
            model.setPassword(encryptPassword);
        }

        Boolean ret = investnewUserDao.updateByPrimaryKeySelective(model) > 0;

        accessService.grantRole(investnewUserModel.getUser_id(),param.getRole_ids());
        if (!investnewUserModel.getStatus().equals(param.getStatus())){
            handleUserStatus(investnewUserModel,param.getStatus());
        }

        return ret;
    }

    @Override
    public Boolean handleUserStatus(InvestnewUserModel user, String status) {
        String md5 = PasswordUtil.md5Password(PasswordUtil.getCurrentTime());
        if (status.equals(StatusEnum.ENABLE.getStatus())){
            SsoResponse ssoResponse = ssoClient.enable(user.getEmail(),user.getMobile(),md5);
            return ssoResponse.getErrorCode().equals(0);
        }else if (status.equals(StatusEnum.DISABLE.getStatus())){
            SsoResponse ssoResponse = ssoClient.disable(user.getEmail(),user.getMobile(),md5);
            return ssoResponse.getErrorCode().equals(0);
        }
        return false;
    }

    @Override
    public InvestnewUserModel userModelByUserId(String userId) {
        return investnewUserDao.selectByUserId(userId);
    }

    @Override
    public AccountListResponse companyUserList(Long companyId, String keyword, Integer page, Integer limit) {
        AccountListResponse accountListResponse = new AccountListResponse();

        Integer offset = Math.max((page - 1) * limit,0);
        Integer total = investnewUserDao.getCount(companyId,null,keyword);
        accountListResponse.setTotal(total);
        if (total.equals(0)){
            return accountListResponse;
        }

        List<AccountBO> accountBOList = new ArrayList<>();
        List<InvestnewUserModel> investnewUserModelList = investnewUserDao.getByPage(companyId,null,keyword,offset,limit);
        List<String> userIds = new ArrayList<>();
        investnewUserModelList.forEach((item)->{
            userIds.add(item.getUser_id());
        });
        Map<String, List<RoleBO>> roleBOMap = accessService.getUsersRole(userIds);

        for (InvestnewUserModel item : investnewUserModelList){
            AccountBO accountBO = new AccountBO();
            accountBO.setId(item.getId());
            accountBO.setUser_id(item.getUser_id());
            accountBO.setUsername(item.getUsername());
            accountBO.setEmail(item.getEmail());
            accountBO.setMobile(item.getMobile());
            accountBO.setNickname(item.getNickname());
            accountBO.setAvatar(item.getAvatar());
            accountBO.setStatus(item.getStatus());
            accountBO.setRoles(roleBOMap.get(item.getUser_id()));
            accountBO.setCreate_time(item.getCreate_time());
            accountBO.setDepartment(item.getDepartment());
            accountBO.setPosition(item.getPosition());
            accountBOList.add(accountBO);
        }

        accountListResponse.setList(accountBOList);
        return accountListResponse;
    }

    @Override
    public List<AccountBO> companyUserList(Long companyId, String companyType) {
        List<AccountBO> accountBOList = new ArrayList<>();
        List<InvestnewUserModel> investnewUserModelList = investnewUserDao.getByPage(companyId,companyType.toLowerCase(),null,0,10000);
        List<String> userIds = new ArrayList<>();
        investnewUserModelList.forEach((item)->{
            userIds.add(item.getUser_id());
        });
        Map<String, List<RoleBO>> roleBOMap = accessService.getUsersRole(userIds);

        for (InvestnewUserModel item : investnewUserModelList){
            AccountBO accountBO = new AccountBO();
            accountBO.setId(item.getId());
            accountBO.setUser_id(item.getUser_id());
            accountBO.setUsername(item.getUsername());
            accountBO.setEmail(item.getEmail());
            accountBO.setMobile(item.getMobile());
            accountBO.setNickname(item.getNickname());
            accountBO.setAvatar(item.getAvatar());
            accountBO.setStatus(item.getStatus());
            accountBO.setRoles(roleBOMap.get(item.getUser_id()));
            accountBO.setCreate_time(item.getCreate_time());
            accountBO.setDepartment(item.getDepartment());
            accountBO.setPosition(item.getPosition());
            accountBOList.add(accountBO);
        }

        return accountBOList;
    }

    @Override
    public AccountBO userInfoByUserId(String userId) throws Exception {
        InvestnewUserModel item = investnewUserDao.selectByUserId(userId);
        if (item == null){
            return null;
        }

        CompanyBO companyBO = dataService.findCompany(item.getCompany_id(),item.getCompany_type());
        List<RoleBO> roleBOList = accessService.getUserRole(item.getUser_id());

        AccountBO accountBO = new AccountBO();
        accountBO.setId(item.getId());
        accountBO.setUser_id(item.getUser_id());
        accountBO.setCompany(companyBO);
        accountBO.setUsername(item.getUsername());
        accountBO.setEmail(item.getEmail());
        accountBO.setMobile(item.getMobile());
        accountBO.setNickname(item.getNickname());
        accountBO.setAvatar(item.getAvatar());
        accountBO.setStatus(item.getStatus());
        accountBO.setRoles(roleBOList);
        accountBO.setCreate_time(item.getCreate_time());
        accountBO.setDepartment(item.getDepartment());
        accountBO.setPosition(item.getPosition());

        return accountBO;
    }

    @Override
    public Map<String,AccountBO> userInfoByUserIds(List<String> userIds) {
        Map<String,AccountBO> accountBOMap = new HashMap<>();

        List<InvestnewUserModel> investnewUserModelList = investnewUserDao.selectByUserIds(userIds);
        if (investnewUserModelList.isEmpty()){
            return accountBOMap;
        }

        for (InvestnewUserModel item : investnewUserModelList){
            AccountBO accountBO = new AccountBO();
            accountBO.setId(item.getId());
            accountBO.setUser_id(item.getUser_id());
//            accountBO.setCompany(companyBO);
            accountBO.setUsername(item.getUsername());
            accountBO.setEmail(item.getEmail());
            accountBO.setMobile(item.getMobile());
            accountBO.setNickname(item.getNickname());
            accountBO.setAvatar(item.getAvatar());
            accountBO.setStatus(item.getStatus());
//            accountBO.setRoles(roleBOList);
            accountBO.setCreate_time(item.getCreate_time());
            accountBO.setDepartment(item.getDepartment());
            accountBO.setPosition(item.getPosition());
            accountBOMap.put(item.getUser_id(),accountBO);
        }
        return accountBOMap;
    }

    @Override
    public Long getUserCompanyId(String userId) {
        InvestnewUserModel investnewUserModel = userModelByUserId(userId);
        if (investnewUserModel == null){
            return null;
        }
        if (investnewUserModel.getCompany_id() == null || investnewUserModel.getCompany_id().equals(0)){
            return null;
        }
        return investnewUserModel.getCompany_id();
    }

    @Override
    public List<CompanyBO> getFundUserCompanyList() {
        List<CompanyBO> companyBOList = new ArrayList<>();
        List<Long> companyIds = investnewUserDao.getFundCompanyIds();
        companyIds.forEach((item)->{
            CompanyBO companyBO = dataService.findCompany(item, CompanyTypeEnum.FUND.name().toLowerCase());
            if (companyBO != null){
                companyBOList.add(companyBO);
            }
        });
        return companyBOList;
    }

    @Override
    public List<CompanyBO> getBrokerUserCompanyList() {
        List<CompanyBO> companyBOList = new ArrayList<>();
        List<Long> companyIds = investnewUserDao.getFundCompanyIds();
        companyIds.forEach((item)->{
            CompanyBO companyBO = dataService.findCompany(item, CompanyTypeEnum.BROKER.name().toLowerCase());
            if (companyBO != null){
                companyBOList.add(companyBO);
            }
        });
        return companyBOList;
    }
}
