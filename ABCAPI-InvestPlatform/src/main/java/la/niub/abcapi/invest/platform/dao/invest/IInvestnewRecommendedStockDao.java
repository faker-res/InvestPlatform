package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewRecommendedStockModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInvestnewRecommendedStockDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvestnewRecommendedStockModel record);

    int insertSelective(InvestnewRecommendedStockModel record);

    InvestnewRecommendedStockModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvestnewRecommendedStockModel record);

    int updateByPrimaryKeyWithBLOBs(InvestnewRecommendedStockModel record);

    int updateByPrimaryKey(InvestnewRecommendedStockModel record);

    int insertMulti(@Param("records") List<InvestnewRecommendedStockModel> records);
}