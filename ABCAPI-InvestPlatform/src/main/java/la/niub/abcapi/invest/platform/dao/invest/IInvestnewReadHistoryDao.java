package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewReadHistoryModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IInvestnewReadHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvestnewReadHistoryModel record);

    int insertSelective(InvestnewReadHistoryModel record);

    InvestnewReadHistoryModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvestnewReadHistoryModel record);

    int updateByPrimaryKeyWithBLOBs(InvestnewReadHistoryModel record);

    int updateByPrimaryKey(InvestnewReadHistoryModel record);

    List<InvestnewReadHistoryModel> search(@Param("userId") String userId,
                                           @Param("objectType") String objectType,
                                           @Param("stockCode") String stockCode,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime,
                                           @Param("offset") Integer offset,
                                           @Param("limit") Integer limit);

    List<InvestnewReadHistoryModel> searchGroupById(@Param("userId") String userId,
                                                    @Param("objectType") String objectType,
                                                    @Param("stockCode") String stockCode,
                                                    @Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime,
                                                    @Param("offset") Integer offset,
                                                    @Param("limit") Integer limit);
}