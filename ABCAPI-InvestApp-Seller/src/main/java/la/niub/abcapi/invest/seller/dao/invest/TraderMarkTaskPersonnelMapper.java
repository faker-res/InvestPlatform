package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkTaskPersonnelModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraderMarkTaskPersonnelMapper {

    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(TraderMarkTaskPersonnelModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkTaskPersonnelModel 
     */
    TraderMarkTaskPersonnelModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkTaskPersonnelModel record);

    /**
     * 批量插入
     * @param taskPersonnelModelList
     * @param taskId
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkTaskPersonnelModel> taskPersonnelModelList, @Param("taskId") Long taskId);

    /**
     * 根据userId获取taskIdList
     * @param userId
     * @return
     */
    List<Long> getTaskIdListByUserId(String userId);

    /**
     * 根据userId和taskId
     * @param taskId
     * @param userId
     * @return
     */
    TraderMarkTaskPersonnelModel getByUserIdAndTaskId(@Param("taskId") Long taskId, @Param("userId") String userId);

    /**
     * 根据taskId获取记录
     * @param taskId
     * @return
     */
    List<TraderMarkTaskPersonnelModel> getByTaskId(Long taskId);
}