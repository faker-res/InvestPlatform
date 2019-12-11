package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RecommendedStockModel;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockBrokerRateVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RecommendedStockMapper {
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
    int insertSelective(RecommendedStockModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RecommendedStockModel 
     */
    RecommendedStockModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RecommendedStockModel record);

    /**
     * 根据parentId和时间统计不同的股票数量
     * @param parentId
     * @param date
     * @return
     * @throws Exception
     */
    Integer getCountByParentIdAndDate(@Param("parent_id") String parentId,
                                      @Param("push_month") String date);

    /**
     * 根据parentId和时间统计每个行业的股票数
     * @param parentId
     * @param date
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getIndustryStockCountByParentIdAndDate(@Param("parent_id") String parentId,
                                                                     @Param("push_month") String date);

    /**
     * 根据parentId、时间以及输入的关键字获取股票名称和代码
     * @param parentId
     * @param date
     * @param keyword
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getStockListByParentIdAndDateAndKeyword(@Param("parent_id") String parentId,
                                                                      @Param("push_month") String date,
                                                                      @Param("keyword") String keyword);

    /**
     * 根据parentId、时间以及股票名称或代码统计不同股票数量
     * @param parentId
     * @param date
     * @param keywordList
     * @return
     * @throws Exception
     */
    Integer getDistinctStockCountByParentIdAndDateAndKeyword(@Param("parent_id") String parentId,
                                                             @Param("push_month") String date,
                                                             @Param("keywordList") List<String> keywordList);

    /**
     * 根据parentId、时间以及股票名称或代码获取相关信息
     * @param parentId
     * @param date
     * @param keywordList
     * @param goldRateMonth
     * @param offset
     * @param limit
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getDistinctStockListByParentIdAndDateAndKeyword(@Param("parent_id") String parentId,
                                                                              @Param("push_month") String date,
                                                                              @Param("keywordList") List<String> keywordList,
                                                                              @Param("goldRateMonth") String goldRateMonth,
                                                                              @Param("offset") Integer offset,
                                                                              @Param("limit") Integer limit);

    /**
     * 根据parentId、时间以及股票代码获取推荐理由
     * @param parentId
     * @param date
     * @param stockCode
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getRecommendedReasonByParentIdAndDateAndStockCode(@Param("parent_id") String parentId,
                                                                                @Param("push_month") String date,
                                                                                @Param("stock_code") String stockCode);

    /**
     * 根据parentId、时间以及关键字获取券商名称
     * @param parentId
     * @param date
     * @param keyword
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getBrokerListByParentIdAndDateAndKeyword(@Param("parent_id") String parentId,
                                                                       @Param("push_month") String date,
                                                                       @Param("keyword") String keyword);

    /**
     * 根据parentId、时间以及推荐券商名称获取推荐股票的详细信息
     * @param parentId
     * @param date
     * @param broker
     * @param goldRateMonth
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getAllStockListByParentIdAndDateAndKeyword(@Param("parent_id") String parentId,
                                                                         @Param("push_month") String date,
                                                                         @Param("broker") String broker,
                                                                         @Param("goldRateMonth") String goldRateMonth);

    List<Map<String, Object>> countBrokerRateV2(RecommendedStockBrokerRateVo input);

    /**
     * 根据parentId和broker和时间查询记录
     * @param parentId
     * @param broker
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getByParentIdAndBrokerAndDate(@Param("parentId") String parentId,
                                                            @Param("broker") String broker,
                                                            @Param("starTime") Date startTime,
                                                            @Param("endTime") Date endTime);

    /**
     * 获取券商最近推荐的7个月份
     * @param parentId
     * @param broker
     * @return
     */
    List<String> getSevenPushMonthByParentIdAndBroker(@Param("parentId") String parentId, @Param("broker") String broker, @Param("maxPushMonth") String maxPushMonth);

    /**
     * 根据parentId、broker、pushMonthList获取股票和收益率
     * @param parentId
     * @param broker
     * @param pushMonthList
     * @return
     */
    List<Map<String, Object>> getByParentIdAndBrokerAndPushMonthList(@Param("parentId") String parentId,
                                                                     @Param("broker") String broker,
                                                                     @Param("pushMonthList") List<String> pushMonthList);
}