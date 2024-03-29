package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewAnalystModel;

import java.util.List;

public interface IInvestnewAnalystDao {
    int deleteByPrimaryKey(String peo_uni_code);

    int insert(InvestnewAnalystModel record);

    int insertSelective(InvestnewAnalystModel record);

    InvestnewAnalystModel selectByPrimaryKey(String peo_uni_code);

    int updateByPrimaryKeySelective(InvestnewAnalystModel record);

    int updateByPrimaryKey(InvestnewAnalystModel record);

    List<InvestnewAnalystModel> selectByName(String analystName);
}