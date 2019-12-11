package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RoadShowBrokerAnalystModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoadShowBrokerAnalystMapper {

    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(RoadShowBrokerAnalystModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RoadShowBrokerAnalystModel 
     */
    RoadShowBrokerAnalystModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RoadShowBrokerAnalystModel record);

    /**
     * 批量插入
     * @param brokerAnalystModelList
     * @return
     */
    int insertBatch(@Param("list") List<RoadShowBrokerAnalystModel> brokerAnalystModelList);

    /**
     * 根据parentId和source删除
     * @param parentId
     * @param source
     * @return
     */
    int deleteByParentIdAndSource(@Param("parentId") String parentId, @Param("source") int source);

    /**
     * 根据parentId、broker、industry、analyst查询记录
     * @param parentId
     * @param broker
     * @param industry
     * @param analyst
     * @return
     */
    RoadShowBrokerAnalystModel getByParentIdAndBroker(@Param("parentId") String parentId,
                                                      @Param("broker") String broker,
                                                      @Param("industry") String industry,
                                                      @Param("analyst") String analyst);

    /**
     * 根据parentId和broker和keyword筛选行业
     * @param parentId
     * @param broker
     * @param keyword
     * @param limit
     * @return
     */
    List<String> getIndustryByParentIdAndBrokerAndKeyword(@Param("parentId") String parentId,
                                                          @Param("broker") String broker,
                                                          @Param("keyword") String keyword,
                                                          @Param("limit") Integer limit);

    /**
     * 根据parentId和broker和keyword筛选研究员
     * @param parentId
     * @param broker
     * @param keyword
     * @return
     */
    List<Map<String, Object>> getAnalystList(@Param("parentId") String parentId,
                                             @Param("broker") String broker,
                                             @Param("industry") String industry,
                                             @Param("keyword") String keyword);

    List<Map<String, Object>> getBrokerIndustryByParentIdAndSource(@Param("parentId") String parentId,
                                                                   @Param("source") Integer source);
}