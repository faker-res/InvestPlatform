package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkConfigDimensionModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraderMarkConfigDimensionMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(TraderMarkConfigDimensionModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkConfigDimensionModel 
     */
    TraderMarkConfigDimensionModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkConfigDimensionModel record);

    /**
     * 根据parentId获取维度集合
     * @param parentId
     * @return
     */
    List<TraderMarkConfigDimensionModel> selectByParentId(String parentId);

    /**
     * 根据parentId和dimension查询记录
     * @param parentId
     * @param dimension
     * @return
     */
    TraderMarkConfigDimensionModel selectByParentIdAndDimension(@Param("parentId") String parentId, @Param("dimension") String dimension, @Param("id") Long id);

    /**
     * 查询所有的parentId
     * @return
     */
    List<String> selectDistinctParentId();
}