package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkConfigModeModel;

import java.util.List;

public interface TraderMarkConfigModeMapper {
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
    int insertSelective(TraderMarkConfigModeModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkConfigModeModel 
     */
    TraderMarkConfigModeModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkConfigModeModel record);

    /**
     * 根据parentId获取评价模式配置信息
     * @param parentId
     * @return
     * @throws Exception
     */
    TraderMarkConfigModeModel selectByParentId(String parentId);

    /**
     * 查询出所有的parentId
     * @return
     * @throws Exception
     */
    List<String> selectDistinctParentId();
}