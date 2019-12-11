package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.TraderMarkConfigObjectModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TraderMarkConfigObjectMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     * @param record
     * @return int 
     */
    int insertSelective(TraderMarkConfigObjectModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkConfigObjectModel 
     */
    TraderMarkConfigObjectModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkConfigObjectModel record);

    /**
     * 查询parentId的记录数
     * @param parentId
     * @return
     */
    int selectCountByParentId(String parentId);

    /**
     * 根据parentId删除记录
     * @param parentId
     * @return
     */
    int deleteByParentId(String parentId);

    /**
     * 批量插入
     * @param configObjectModelList
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkConfigObjectModel> configObjectModelList);

    /**
     * 根据parentId和keyword查询券商名称
     * @param parentId
     * @param keyword
     * @param limit
     * @return
     */
    List<Map<String, Object>> selectBrokerListByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 根据parentId和券商名称查询记录
     * @param parentId
     * @param brokerList
     * @return
     */
    List<TraderMarkConfigObjectModel> selectByParentIdAndBrokerList(@Param("parentId") String parentId, @Param("brokerList") List<String> brokerList);

    /**
     * 查询所有parentId
     * @return
     */
    List<String> selectDistinctParentId();

    /**
     * 根据parentId和券商名称获取行业
     * @param buyerCompanyId
     * @param broker
     * @return
     */
    List<String> getBrokerIndustryList(@Param("parentId") String buyerCompanyId, @Param("broker") String broker);
}