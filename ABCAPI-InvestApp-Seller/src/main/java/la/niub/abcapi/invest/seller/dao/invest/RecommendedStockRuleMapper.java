package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecommendedStockRuleMapper {
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
    int insertSelective(RecommendedStockRuleModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RecommendedStockRuleModel 
     */
    RecommendedStockRuleModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RecommendedStockRuleModel record);

    /**
     * 根据parentId和keyword获取券商名称列表
     * @param parentId
     * @param keyword
     * @param limit
     * @return
     */
    List<Map<String, String>> getBrokerListByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 根据parentId和券商名称集合获规则列表
     * @param parentId
     * @param brokerList
     * @return
     */
    List<RecommendedStockRuleModel> getRuleListByParentIdAndBrokerList(@Param("parentId") String parentId, @Param("brokerList") List<String> brokerList);

    /**
     * 根据parentId和broker和sheetName查询记录
     * @param parentId
     * @param broker
     * @param sheetName
     * @return
     */
    RecommendedStockRuleModel getByParentIdAndBrokerAndSheetName(@Param("parentId") String parentId, @Param("broker") String broker, @Param("sheetName") String sheetName);

    /**
     * 根据parentId获取券商名称
     * @param parentId
     * @return
     */
    List<String> getBrokerByParentId(@Param("parentId") String parentId);
}