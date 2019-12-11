package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewUserModel;
import la.niub.abcapi.invest.platform.model.bo.account.AccountBO;
import la.niub.abcapi.invest.platform.model.request.account.AccountAddRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountMobileRegisterRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountModifyRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountRegisterRequest;
import la.niub.abcapi.invest.platform.model.request.account.AccountCompleteInfoRequest;
import la.niub.abcapi.invest.platform.model.response.account.AccountListResponse;

import java.util.List;
import java.util.Map;

/**
 * 账户服务
 */
public interface IAccountService {

    /**
     * 同步账户基础信息
     * @return
     */
    InvestnewUserModel syncUser(String mobile,String email,String password) throws Exception;
    InvestnewUserModel syncUser(String userId,String password) throws Exception;

    /**
     * 删除账户
     */
    Boolean deleteUser(Integer id) throws Exception;

    /**
     * 完善个人信息
     * @param param
     */
    Boolean completeInfo(AccountCompleteInfoRequest param) throws Exception;

    /**
     * 注册手机账户
     * @return userId
     */
    String mobileRegister(AccountMobileRegisterRequest param) throws Exception;

    /**
     * 无验证直接注册新账户
     * @return userId
     */
    String register(AccountRegisterRequest param) throws Exception;

    /**
     * 添加账户
     * @param param
     * @return
     */
    String addUser(AccountAddRequest param) throws Exception;

    /**
     * 修改账户
     * @param param
     * @return
     */
    Boolean modifyUser(AccountModifyRequest param) throws Exception;

    /**
     * 处理用户状态
     * @param user
     * @param status
     * @return
     */
    Boolean handleUserStatus(InvestnewUserModel user,String status);

    /**
     * 根据userid获取账户数据库模型
     * @param userId
     * @return
     */
    InvestnewUserModel userModelByUserId(String userId);

    /**
     * 公司下账号列表
     * @param companyId
     * @param keyword
     * @param page
     * @param limit
     * @return
     */
    AccountListResponse companyUserList(Long companyId, String keyword, Integer page, Integer limit);
    List<AccountBO> companyUserList(Long companyId, String companyType);

    /**
     * 根据userid获取账户信息
     * @param userId
     * @return
     */
    AccountBO userInfoByUserId(String userId) throws Exception;

    /**
     * 根据userid批量获取账户信息
     * @param userIds
     * @return
     */
    Map<String,AccountBO> userInfoByUserIds(List<String> userIds);

    /**
     * 获取用户的公司id
     * @param userId
     * @return
     */
    Long getUserCompanyId(String userId);

    /**
     * 获取已注册的基金公司
     * @return
     */
    List<CompanyBO> getFundUserCompanyList();

    /**
     * 获取已注册的券商公司
     * @return
     */
    List<CompanyBO> getBrokerUserCompanyList();
}
