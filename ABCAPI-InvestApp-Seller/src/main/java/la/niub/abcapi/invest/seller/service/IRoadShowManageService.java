package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.RoadShowModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowAddVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowQueryVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowSellerAnalystSaveVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoadShowManageService {
    /**
     * 新增路演
     * @param roadShowAddVo
     * @throws Exception
     */
    void save(RoadShowAddVo roadShowAddVo) throws Exception;

    /**
     * 查询路演列表-支持高级筛选-券商视角
     * @param roadShowQueryVo
     * @return
     * @throws Exception
     */
    Map<String, List<RoadShowAddVo>> getRoadShowListByBroker(RoadShowQueryVo roadShowQueryVo) throws Exception;

    /**
     * 根据id获取路演详情
     * @param id
     * @param userId
     * @return
     * @throws Exception
     */
    RoadShowAddVo getRoadShowDetail(Long id, String userId) throws Exception;

    /**
     * 根据id获取tbl_invest_road_show的记录
     * @param id
     * @return
     * @throws Exception
     */
    RoadShowModel getRoadShowById(Long id) throws Exception;

    /**
     * 查询路演列表-支持高级筛选-基金公司视角
     * @param roadShowQueryVo
     * @return
     */
    Map<String, List<RoadShowAddVo>> getRoadShowListByFund(RoadShowQueryVo roadShowQueryVo) throws Exception;

    /**
     * 加入行程
     * @param roadShowId
     * @param userId
     * @throws Exception
     */
    void joinTrip(Long roadShowId, String userId) throws Exception;

    /**
     * 取消行程
     * @param roadShowId
     * @param userId
     */
    void cancelTrip(Long roadShowId, String userId) throws Exception;

    /**
     * 根据id更新路演
     * @param roadShowAddVo
     */
    void updateById(RoadShowAddVo roadShowAddVo) throws Exception;

    /**
     * 删除路演
     * @param id
     */
    void deleteById(Long id, String userId) throws Exception;

    /**
     * 获取券商路演管理筛选条件
     * @param userId
     * @return
     */
    Map<String, Object> getBrokerFilterCondition(String userId) throws Exception;

    /**
     * 获取基金公司管理筛选条件
     * @return
     */
    Map<String, Object> getFundCompanyFilterCondition(String userId) throws Exception;

    /**
     * 会议室下拉提示
     * @param userId
     * @param keyword
     * @param limit
     * @return
     */
    Set<String> getMeetingRoomList(String userId, String keyword, Integer limit) throws Exception;

    /**
     * 根据id更新路演会议室
     * @param id
     * @param meetingRoom
     */
    void updateMeetingRoomById(Long id, String meetingRoom) throws Exception;

    /**
     * 根据券商评价行业过滤预约行业
     * @param userId
     * @param buyerCompanyId
     * @param keyword
     * @param limit
     * @return
     */
    List<String> getIndustryList(String userId, String buyerCompanyId, String keyword, Integer limit) throws Exception;

    /**
     * 添加卖方研究员
     * @param sellerAnalystSaveVo
     */
    void saveSellerAnalyst(RoadShowSellerAnalystSaveVo sellerAnalystSaveVo) throws Exception;

    /**
     * 券商研究员列表 公众号
     * @param userId
     * @param keyword
     * @return
     * @throws Exception
     */
    Map<String, Object> getSellerAnalystList(String userId, String keyword, String buyerCompanyId, String industry) throws Exception;

    void export(ByteArrayOutputStream bos, String userId, String startTime, String endTime, String fields) throws Exception;

    void upload(MultipartFile file, String userId) throws Exception;

    Integer getRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception;

    List<Map<String, Object>> getCompanyRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception;

    List<String> getRoadShowTimeBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst, String stockCode, String stockName);
}
