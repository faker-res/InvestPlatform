package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewStockIndexModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInvestnewStockIndexDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvestnewStockIndexModel record);

    int insertSelective(InvestnewStockIndexModel record);

    InvestnewStockIndexModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvestnewStockIndexModel record);

    int updateByPrimaryKey(InvestnewStockIndexModel record);

    List<Long> findCodesDataNotEmpty(@Param("secUniCodes") List<Long> secUniCodes, @Param("pushMonth") String pushMonth);

    int insertMulti(@Param("records") List<InvestnewStockIndexModel> records);

    int updateData(InvestnewStockIndexModel record);

    List<InvestnewStockIndexModel> selectDataEmpty(@Param("limit") Integer limit);
}