package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkModeModel;

import java.util.List;

public interface TraderMarkModeMapper {
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
    int insertSelective(TraderMarkModeModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkModeModel 
     */
    TraderMarkModeModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkModeModel record);

    /**
     * 查询所有模式
     * @return
     */
    List<TraderMarkModeModel> selectAll();
}