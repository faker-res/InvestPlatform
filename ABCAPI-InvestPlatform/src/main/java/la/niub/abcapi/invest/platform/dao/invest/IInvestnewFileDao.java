package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewFileModel;

public interface IInvestnewFileDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvestnewFileModel record);

    int insertSelective(InvestnewFileModel record);

    InvestnewFileModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvestnewFileModel record);

    int updateByPrimaryKey(InvestnewFileModel record);
}