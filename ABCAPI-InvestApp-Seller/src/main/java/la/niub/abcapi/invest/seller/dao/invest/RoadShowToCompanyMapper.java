package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RoadShowToCompanyModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowCompanyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoadShowToCompanyMapper {
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
    int insertSelective(RoadShowToCompanyModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RoadShowToCompanyModel 
     */
    RoadShowToCompanyModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RoadShowToCompanyModel record);

    /**
     * 批量插入路演公司
     * @param roadShowId
     * @param companys
     * @return
     */
    int insertBatch(@Param("roadShowId") Long roadShowId, @Param("companys") List<RoadShowCompanyVo> companys);

    /**
     * 查询路演公司
     * @param roadShowId
     * @return
     */
    List<RoadShowToCompanyModel> getCompanyListByRoadShowId(Long roadShowId);

    /**
     * 根据roadShowId删除记录
     * @param roadShowId
     * @return
     */
    int deleteByRoadShowId(Long roadShowId);
}