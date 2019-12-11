package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewRecommendedStockAttachmentModel;

import java.util.List;

public interface IInvestnewRecommendedStockAttachmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InvestnewRecommendedStockAttachmentModel record);

    int insertSelective(InvestnewRecommendedStockAttachmentModel record);

    InvestnewRecommendedStockAttachmentModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvestnewRecommendedStockAttachmentModel record);

    int updateByPrimaryKey(InvestnewRecommendedStockAttachmentModel record);

    List<InvestnewRecommendedStockAttachmentModel> selectNotHandled();
}