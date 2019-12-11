package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkAnalystTargetModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TraderMarkAnalystTargetMapper {
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
    int insertSelective(TraderMarkAnalystTargetModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkAnalystTargetModel 
     */
    TraderMarkAnalystTargetModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkAnalystTargetModel record);

    /**
     * 批量插入
     * @param analystTargetModelList
     * @return
     */
    int insertBatch(@Param("list") Collection<TraderMarkAnalystTargetModel> analystTargetModelList);

    /**
     * 查询未达到目标值的记录
     * @return
     */
    List<TraderMarkAnalystTargetModel> selectByNotComp(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 根据id批量更新
     * @param updateList
     * @return
     */
    Integer updateBatch(@Param("list") List<TraderMarkAnalystTargetModel> updateList);

    TraderMarkAnalystTargetModel selectByCondition(@Param("parentId") String parentId, @Param("broker") String broker,
                                                   @Param("analyst") String analyst, @Param("secUniCode") Long secUniCode,
                                                   @Param("stockCode") String stockCode, @Param("stockName") String stockName,
                                                   @Param("targetValue") BigDecimal targetValue, @Param("targetDate") Date targetDate);

    /**
     * 获取达成天数
     * @param parentId
     * @param broker
     * @param analyst
     * @param endTime
     * @return
     */
    BigDecimal getAnalystAttainDays(@Param("parentId") String parentId, @Param("broker") String broker, @Param("analyst") String analyst, @Param("endTime") Date endTime);

    /**
     * 获取达成率
     * @param parentId
     * @param broker
     * @param analyst
     * @param endTime
     * @return
     */
    BigDecimal getAnalystAttainProbability(@Param("parentId") String parentId, @Param("broker") String broker, @Param("analyst") String analyst, @Param("endTime") Date endTime);

    /**
     * 查询未达成的数量
     * @return
     */
    Integer selectCountByNotComp();
}