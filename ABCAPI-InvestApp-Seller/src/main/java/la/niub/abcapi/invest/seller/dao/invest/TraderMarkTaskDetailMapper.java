package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkTaskDetailModel;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveItemVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TraderMarkTaskDetailMapper {
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
    int insertSelective(TraderMarkTaskDetailModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkTaskDetailModel 
     */
    TraderMarkTaskDetailModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkTaskDetailModel record);

    /**
     * 批量插入
     * @param taskDetailModelList
     * @param taskId
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkTaskDetailModel> taskDetailModelList, @Param("taskId") Long taskId);

    /**
     * 获取任务的总进度
     * @param taskId
     * @return
     */
    BigDecimal selectProgressRate(Long taskId);

    /**
     * 查询某用户是否未评分项数
     * @param taskId
     * @param userId
     * @return
     */
    Integer selectScoreNullCountByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") String userId);

    /**
     * 查询某用户的券商是否评分
     * @param taskId
     * @param userId
     * @return
     */
    List<Map<String, Object>> getBrokerStatusByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") String userId);

    /**
     * 获取券商的各维度得分
     * @param taskId
     * @param userId
     * @param broker
     * @return
     */
    List<Map<String, Object>> getDimensionScoreByTaskIdAndUserIdAndBroker(@Param("taskId") Long taskId, @Param("userId") String userId, @Param("broker") String broker);

    /**
     * 获取各行业下的研究员数量
     * @param userId
     * @param taskId
     * @param broker
     * @return
     */
    List<Map<String, Object>> getIndustryAnalystCount(@Param("userId") String userId, @Param("taskId") Long taskId, @Param("broker") String broker);

    /**
     * 获取已完成的行业
     * @param userId
     * @param taskId
     * @param broker
     * @return
     */
    List<Map<String, Object>> getFinishedIndustry(@Param("userId") String userId, @Param("taskId") Long taskId, @Param("broker") String broker);

    /**
     * 获取研究员客观数据和主观数据
     * @param userId
     * @param taskId
     * @param broker
     * @param industry
     * @return
     */
    List<Map<String,Object>> getAnalystObjectiveAndSubjectiveData(@Param("userId") String userId,
                                                                  @Param("taskId") Long taskId,
                                                                  @Param("broker") String broker,
                                                                  @Param("industry") String industry);

    /**
     * 获取评价维度
     * @param userId
     * @param taskId
     * @param broker
     * @return
     */
    List<String> getDistinctDimensionList(@Param("userId") String userId, @Param("taskId") Long taskId, @Param("broker") String broker);

    /**
     * 获取得分之和
     * @param userId
     * @param taskId
     * @return
     */
    Integer getTotalScore(@Param("userId") String userId, @Param("taskId") Long taskId);

    /**
     * 根据userId、taskId、broker、industry获取得分之和
     * @param userId
     * @param taskId
     * @param broker
     * @param industry
     * @return
     */
    Integer getTotalScoreByBrokerAndIndustry(@Param("userId") String userId,
                                             @Param("taskId") Long taskId,
                                             @Param("broker") String broker,
                                             @Param("industry") String industry);

    /**
     * 查询还未评分的id
     * @param taskId
     * @return
     */
    List<Long> getScoreNullIdList(Long taskId);

    /**
     * 根据taskId获取打分详情
     * @param taskId
     * @return
     */
    List<TraderMarkTaskDetailModel> getByTaskId(Long taskId);

    /**
     * 评分入库
     * @param scoreList
     * @return
     */
    int updateScoreByIdList(@Param("list") List<TraderMarkTaskSaveItemVo> scoreList);

    /**
     * 根据userId和taskId查询记录
     * @param userId
     * @param taskId
     * @return
     */
    List<TraderMarkTaskDetailModel> selectByUserIdAndTaskId(@Param("userId") String userId, @Param("taskId") Long taskId);
}