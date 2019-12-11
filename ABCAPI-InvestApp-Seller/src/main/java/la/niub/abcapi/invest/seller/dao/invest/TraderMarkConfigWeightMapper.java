package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkConfigWeightModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraderMarkConfigWeightMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     * @param record
     * @return int 
     */
    int insertSelective(TraderMarkConfigWeightModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkConfigWeightModel 
     */
    TraderMarkConfigWeightModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkConfigWeightModel record);

    /**
     * 根据parentId获取记录
     * @param parentId
     * @return
     */
    List<TraderMarkConfigWeightModel> selectByParentId(String parentId);

    /**
     * 根据parentId和userId查询权重
     * @param parentId
     * @param userId
     * @return
     */
    TraderMarkConfigWeightModel selectByParentIdAndUserId(@Param("parentId") String parentId, @Param("userId") String userId, @Param("id") Long id);

    /**
     * 查询parentId以及对应模式的权重或阈值是否都设置完成
     * @param parentId
     * @param modeId
     * @return
     */
    Integer selectCountByParentIdAndModeId(@Param("parentId") String parentId, @Param("modeId") Long modeId);
}