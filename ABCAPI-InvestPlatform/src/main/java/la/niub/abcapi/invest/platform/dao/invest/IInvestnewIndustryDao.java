package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewIndustryModel;

import java.util.List;

public interface IInvestnewIndustryDao {
    int deleteByPrimaryKey(Long index_uni_code);

    int insert(InvestnewIndustryModel record);

    int insertSelective(InvestnewIndustryModel record);

    InvestnewIndustryModel selectByPrimaryKey(Long index_uni_code);

    int updateByPrimaryKeySelective(InvestnewIndustryModel record);

    int updateByPrimaryKey(InvestnewIndustryModel record);

    List<InvestnewIndustryModel> getAll();
}