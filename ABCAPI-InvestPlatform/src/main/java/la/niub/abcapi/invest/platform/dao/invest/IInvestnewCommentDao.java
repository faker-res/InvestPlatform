package la.niub.abcapi.invest.platform.dao.invest;

import la.niub.abcapi.invest.platform.model.invest.InvestnewCommentModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IInvestnewCommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InvestnewCommentModel record);

    int insertSelective(InvestnewCommentModel record);

    InvestnewCommentModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvestnewCommentModel record);

    int updateByPrimaryKeyWithBLOBs(InvestnewCommentModel record);

    int updateByPrimaryKey(InvestnewCommentModel record);

    List<InvestnewCommentModel> getByObject(@Param("objectId") String objectId,
                                               @Param("objectType") String objectType,
                                               @Param("userId") String userId,
                                               @Param("createTime") Date createTime,
                                               @Param("limit") Integer limit);

    Integer getCount(@Param("objectId") String objectId,
                                               @Param("objectType") String objectType,
                                               @Param("userId") String userId,
                                               @Param("createTime") Date createTime,
                                               @Param("limit") Integer limit);

    List<InvestnewCommentModel> selectByIds(@Param("commentIds") List<Integer> commentIds);

    int insertSelectiveSelectId(InvestnewCommentModel record);
}