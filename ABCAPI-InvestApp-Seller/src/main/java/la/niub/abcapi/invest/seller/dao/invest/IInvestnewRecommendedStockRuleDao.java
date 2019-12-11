package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.InvestnewRecommendedStockRuleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInvestnewRecommendedStockRuleDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvestnewRecommendedStockRuleModel record);

    int insertSelective(InvestnewRecommendedStockRuleModel record);

    InvestnewRecommendedStockRuleModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvestnewRecommendedStockRuleModel record);

    int updateByPrimaryKey(InvestnewRecommendedStockRuleModel record);

    List<InvestnewRecommendedStockRuleModel> selectByCompanyIdAndBroker(@Param("companyId") Long companyId, @Param("broker") String broker);
}