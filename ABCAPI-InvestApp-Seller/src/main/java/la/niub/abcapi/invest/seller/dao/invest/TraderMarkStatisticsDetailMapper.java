package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkObjectiveDataBrokerModel;
import la.niub.abcapi.invest.seller.model.TraderMarkStatisticsDetailModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TraderMarkStatisticsDetailMapper {
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
    int insertSelective(TraderMarkStatisticsDetailModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkStatisticsDetailModel 
     */
    TraderMarkStatisticsDetailModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkStatisticsDetailModel record);

    /**
     * 批量插入
     * @param statisticsDetailModelList
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkStatisticsDetailModel> statisticsDetailModelList);

    /**
     * 获取券商最近7次的综合得分
     * @param parentId
     * @param broker
     * @return
     */
    List<Map<String, Object>> getBrokerSevenTotalScoreByBroker(@Param("parentId") String parentId, @Param("broker") String broker);

    /**
     * 获取券商排名
     * @param taskId
     * @return
     */
    List<Map<String, Object>> getBrokerRankByTaskId(Long taskId);

    /**
     * 获取评分任务券商的各维度得分
     * @param taskId
     * @param broker
     * @param calculate  true:只查询加入综合得分计算  false:全查询
     * @return
     */
    List<Map<String, Object>> getDimensionScoreByTaskIdAndBroker(@Param("taskId") Long taskId,
                                                                 @Param("broker") String broker,
                                                                 @Param("calculate") Boolean calculate);

    /**
     * 获取评分任务下某个券商的行业研究员数量
     * @param taskId
     * @param broker
     * @return
     */
    List<Map<String, Object>> getBrokerIndustryAnalystCount(@Param("taskId") Long taskId, @Param("broker") String broker);

    /**
     * 获取评分任务某个券商研究员得分详情
     * @param taskId
     * @param broker
     * @param industry
     * @param analystList
     * @return
     */
    List<TraderMarkStatisticsDetailModel> getBrokerDetail(@Param("taskId") Long taskId,
                                                          @Param("broker") String broker,
                                                          @Param("industry") String industry,
                                                          @Param("analystList") List<String> analystList);

    /**
     * 获取评分任务某个券商研究员列表
     * @param taskId
     * @param broker
     * @param industry
     * @param keyword
     * @return
     */
    List<Map<String, Object>> getBrokerAnalyst(@Param("taskId") Long taskId,
                                               @Param("broker") String broker,
                                               @Param("industry") String industry,
                                               @Param("keyword") String keyword);

    /**
     * 根据taskId获取券商的综合得分
     * @param taskId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getBrokerScoreByTaskId(Long taskId);

    /**
     * 根据taskIdList和broker获取券商的综合得分
     * @param taskIdList
     * @param broker
     * @return
     */
    List<Map<String, Object>> getBrokerScoreByTaskIdListAndBroker(@Param("taskIdList") List<Long> taskIdList, @Param("broker") String broker);

    /**
     * 根据taskIdList获取平均分
     * @param taskIdList
     * @return
     */
    List<Map<String, Object>> getTaskAverageScoreByTaskIdList(@Param("taskIdList") List<Long> taskIdList);
}