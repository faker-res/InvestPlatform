package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkTaskModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraderMarkTaskMapper {
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
    int insertSelective(TraderMarkTaskModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkTaskModel 
     */
    TraderMarkTaskModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkTaskModel record);

    /**
     * 根据idList查询信息
     * @param taskIdList
     * @return
     */
    List<TraderMarkTaskModel> selectByTaskIdList(@Param("list") List<Long> taskIdList);

    /**
     * 获取最近一次的券商评分任务
     * @param parentId
     * @return
     */
    Long getRecentFinishedTaskIdByParentId(@Param("parentId") String parentId);

    /**
     * 获取最近7个月评分任务
     * @param parentId
     * @return
     */
    List<TraderMarkTaskModel> getRecentSevenTaskIdByParentId(@Param("parentId") String parentId);
}