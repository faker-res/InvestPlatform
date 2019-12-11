package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RoadShowToBuyerModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowBuyerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoadShowToBuyerMapper {
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
    int insertSelective(RoadShowToBuyerModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RoadShowToBuyerModel 
     */
    RoadShowToBuyerModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RoadShowToBuyerModel record);

    /**
     * 批量插入路演买方关系
     * @param roadShowId
     * @param buyers
     * @return
     */
    int insertBatch(@Param("roadShowId") Long roadShowId, @Param("list") List<RoadShowBuyerVo> buyers);

    /**
     * 根据路演id查询关联的买方研究员
     * @param roadShowId
     * @return
     */
    List<RoadShowToBuyerModel> getBuyerListByRoadShowId(Long roadShowId);

    /**
     * 根据roadShowId和userId删除记录
     * @param roadShowId
     * @param userId
     * @return
     */
    int deleteByRoadShowIdAndUserId(@Param("roadShowId") Long roadShowId, @Param("userId") String userId);

    /**
     * 根据roadShowId删除id
     * @param roadShowId
     * @return
     */
    int deleteByRoadShowId(Long roadShowId);
}