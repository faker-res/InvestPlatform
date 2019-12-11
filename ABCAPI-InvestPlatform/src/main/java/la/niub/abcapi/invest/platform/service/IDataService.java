package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystBO;
import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import la.niub.abcapi.invest.platform.model.bo.data.IndustryBO;

import java.util.List;

/**
 * 数据服务
 */
public interface IDataService {

    /**
     * 获取基金公司
     * @return
     */
    List<CompanyBO> getFund();

    /**
     * 获取券商公司
     * @return
     */
    List<CompanyBO> getBroker();

    /**
     * 获取行业
     * @return
     */
    List<IndustryBO> getIndustry();

    /**
     * 根据公司名称查找公司实体
     * @param companyName
     * @return
     */
    CompanyBO findCompany(String companyName);

    /**
     * 根据公司id和类型查找公司实体
     * @param companyId
     * @param companyType
     * @return
     */
    CompanyBO findCompany(Long companyId, String companyType);
    CompanyBO findCompany(Long companyId);

    /**
     * 查找分析师
     * @param analystName
     * @return
     */
    AnalystBO findAnalyst(String analystName,String companyName);
}
