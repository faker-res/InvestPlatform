package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RoadShowToSellerModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowSellerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoadShowToSellerMapper {
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
    int insertSelective(RoadShowToSellerModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RoadShowToSellerModel
     */
    RoadShowToSellerModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RoadShowToSellerModel record);

    /**
     * 批量插入
     * @param roadShowId
     * @param sellers
     * @param sellerCompanyId
     * @param sellerCompanyName
     * @return
     */
    int insertBatch(@Param("roadShowId") Long roadShowId,
                    @Param("list") List<RoadShowSellerVo> sellers,
                    @Param("sellerCompanyId") String sellerCompanyId,
                    @Param("sellerCompanyName") String sellerCompanyName);

    /**
     * 根据roadShowId查询卖方研究员信息
     * @param roadShowId
     * @return
     */
    List<RoadShowToSellerModel> getSellerListByRoadShowId(@Param("roadShowId") Long roadShowId);

    /**
     * 删除路演卖方研究员信息
     * @param roadShowId
     * @return
     */
    int deleteByRoadShowId(@Param("roadShowId") Long roadShowId);
}