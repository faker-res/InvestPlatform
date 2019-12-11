package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkObjectiveDataBrokerModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TraderMarkObjectiveDataBrokerMapper {
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
    int insertSelective(TraderMarkObjectiveDataBrokerModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkObjectiveDataBrokerModel 
     */
    TraderMarkObjectiveDataBrokerModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkObjectiveDataBrokerModel record);

    /**
     * 批量插入
     * @param objectiveDataBrokerModelList
     * @param taskId
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerModelList, @Param("taskId") Long taskId);

    /**
     * 根据taskId和broker查询记录
     * @param taskId
     * @param broker
     * @return
     */
    TraderMarkObjectiveDataBrokerModel selectByTaskIdAndBroker(@Param("taskId") Long taskId, @Param("broker") String broker);

    /**
     * 获取某个公司上传所有券商
     * @param parentId
     * @param keyword
     * @return
     */
    List<String> getBrokerListByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword);

    /**
     * 获取最近一个季度的券商路演量
     * @param taskId
     * @return
     */
    List<TraderMarkObjectiveDataBrokerModel> getBrokerInfoListByTaskId(Long taskId);

    /**
     * 根据taskIdList和broker获取券商信息
     * @param taskIdList
     * @param broker
     * @return
     */
    List<TraderMarkObjectiveDataBrokerModel> getBrokerInfoListByTaskIdListAndBroker(@Param("taskIdList") List<Long> taskIdList, @Param("broker") String broker);

    /**
     * 获取历史所有上传券商最近一个季度的达成率和达成天数
     * @param parentId
     * @return
     */
    List<Map<String, Object>> getBrokerRecentAttainDaysInfoByParentId(@Param("parentId") String parentId);
}