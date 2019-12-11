package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.RoadShowModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RoadShowMapper {
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
    int insertSelective(RoadShowModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return RoadShowModel 
     */
    RoadShowModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(RoadShowModel record);

    /**
     * 统计一段时间内某个券商的某个研究员的路演数量
     * @param startTime
     * @param endTime
     * @param sellerCompanyName
     * @param industry
     * @param sellerName
     * @return
     */
    Integer getRoadShowCountBySellerAndDate(@Param("buyerCompanyId") String buyerCompanyId,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime,
                                            @Param("sellerCompanyName") String sellerCompanyName,
                                            @Param("industry") String industry,
                                            @Param("sellerName") String sellerName);

    /**
     * 查询一段时间内某个券商推荐的股票次数
     * @param sellerCompanyName
     * @param sellerName
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getCompanyRoadShowCountBySellerAndDate(@Param("buyerCompanyId") String buyerCompanyId,
                                                                     @Param("sellerCompanyName") String sellerCompanyName,
                                                                     @Param("industry") String industry,
                                                                     @Param("sellerName") String sellerName,
                                                                     @Param("startTime") Date startTime,
                                                                     @Param("endTime") Date endTime);

    /**
     * 获取一段时间内某个券商研究员路演某公司的时间
     * @param startTime
     * @param endTime
     * @param sellerCompanyName
     * @param sellerName
     * @param companyId
     * @param companyName
     * @return
     */
    List<String> getRoadShowTimeBySellerAndDate(@Param("buyerCompanyId") String buyerCompanyId,
                                                @Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime,
                                                @Param("sellerCompanyName") String sellerCompanyName,
                                                @Param("industry") String industry,
                                                @Param("sellerName") String sellerName,
                                                @Param("companyId") String companyId,
                                                @Param("companyName") String companyName);

    /**
     * 查询路演列表-支持高级筛选-券商视角
     * @param condition
     * @param parentId
     * @return
     */
    List<Long> getRoadShowIdsByBroker(@Param("condition") RoadShowQueryVo condition, @Param("parentId") String parentId);

    /**
     * 查询路演列表-支持高级筛选-基金公司视角
     * @param roadShowQueryVo
     * @return
     */
    List<Long> getRoadShowIdsByFund(@Param("condition") RoadShowQueryVo roadShowQueryVo, @Param("buyerCompanyId") String buyerCompanyId);

    /**
     * 获取路演的不同公司列表
     * @param sellerCompanyId
     * @return
     */
    List<Map<String, String>> getDistinctCompanyList(@Param("sellerCompanyId") String sellerCompanyId, @Param("buyerCompanyId") String buyerCompanyId);

    /**
     * 获取路演的不同行业列表
     * @param sellerCompanyId
     * @return
     */
    List<Map<String, String>> getDistinctIndustryList(@Param("sellerCompanyId") String sellerCompanyId, @Param("buyerCompanyId") String buyerCompanyId);

    /**
     * 获取路演的不同卖方研究员列表
     * @param sellerCompanyId
     * @return
     */
    List<Map<String, String>> getDistinctSellerList(@Param("sellerCompanyId") String sellerCompanyId, @Param("buyerCompanyId") String buyerCompanyId);

    /**
     * 获取路演的不同买方研究员列表
     * @param sellerCompanyId
     * @return
     */
    List<Map<String, String>> getDistinctBuyerList(@Param("sellerCompanyId") String sellerCompanyId);

    /**
     * 获取路演的不同卖方公司列表
     * @return
     */
    List<Map<String, String>> getDistinctSellerCompanyList(@Param("buyerCompanyId") String buyerCompanyId);

    /**
     * 根据buyerCompanyId查询会议室列表
     * @param buyerCompanyId
     * @return
     */
    List<String> getMeetingRoomListByBuyerCompanyId(@Param("buyerCompanyId") String buyerCompanyId);

    List<Map<String,Object>> getByTime(@Param("buyerCompanyId") String buyerCompanyId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}