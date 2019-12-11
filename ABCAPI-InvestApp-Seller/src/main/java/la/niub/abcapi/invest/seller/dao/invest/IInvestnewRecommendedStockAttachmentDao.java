package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.InvestnewRecommendedStockAttachmentModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInvestnewRecommendedStockAttachmentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InvestnewRecommendedStockAttachmentModel record);

    int insertSelective(InvestnewRecommendedStockAttachmentModel record);

    InvestnewRecommendedStockAttachmentModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvestnewRecommendedStockAttachmentModel record);

    int updateByPrimaryKey(InvestnewRecommendedStockAttachmentModel record);

    int insertMulti(@Param("records") List<InvestnewRecommendedStockAttachmentModel> records);

    List<InvestnewRecommendedStockAttachmentModel> selectNotHandled();
}