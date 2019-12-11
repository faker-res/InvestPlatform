package la.niub.abcapi.invest.seller.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.config.enums.ExcelTypeEnum;
import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.CompanyResponse;
import la.niub.abcapi.invest.seller.model.vo.*;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.seller.model.response.client.RoleResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.exception.CustomException;
import la.niub.abcapi.invest.seller.component.exception.ValidatorException;
import la.niub.abcapi.invest.seller.component.util.ExcelUtil;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.config.code.RoadShowEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.RoadShowExportEnum;
import la.niub.abcapi.invest.seller.model.response.client.AccountInfoResponse;
import la.niub.abcapi.invest.seller.service.IRoadShowManageService;
import la.niub.abcapi.invest.seller.service.IRoadShowManageTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : ppan
 * @description : 路演管理service
 * @date : 2019-02-11 11:01
 */
@Service
public class RoadShowManageServiceImpl implements IRoadShowManageService {

    private Logger log = LogManager.getLogger(getClass());

    @Autowired
    private RoadShowMapper roadShowMapper;

    @Autowired
    private RoadShowToBuyerMapper roadShowToBuyerMapper;

    @Autowired
    private RoadShowToCompanyMapper roadShowToCompanyMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRoadShowManageTransactionService roadShowManageTransactionService;

    @Autowired
    private TraderMarkConfigObjectMapper traderMarkConfigObjectMapper;

    @Autowired
    private RoadShowBrokerAnalystMapper roadShowBrokerAnalystMapper;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private RoadShowToSellerMapper roadShowToSellerMapper;

    @Override
    public void save(RoadShowAddVo roadShowAddVo) throws Exception {
        RoadShowModel roadShowModel = new RoadShowModel();
        roadShowModel.setActivityDesc(roadShowAddVo.getActivityDesc());
        roadShowModel.setActivityEndTime(roadShowAddVo.getActivityEndTime());
        roadShowModel.setActivityStartTime(roadShowAddVo.getActivityStartTime());
        roadShowModel.setCompanyName(roadShowAddVo.getCompanyName());
        roadShowModel.setIndustryName(roadShowAddVo.getIndustryName());
        roadShowModel.setRoadShowDate(TimeUtil.parseDateStr(roadShowAddVo.getRoadShowDate(), "yyyy-MM-dd"));

        roadShowModel.setSellerId(roadShowAddVo.getSellerId());
        roadShowModel.setSellerName(roadShowAddVo.getSellerName());
        roadShowModel.setSellerCompanyId(roadShowAddVo.getSellerCompanyId());
        roadShowModel.setSellerCompanyName(roadShowAddVo.getSellerCompanyName());
        roadShowModel.setTheme(roadShowAddVo.getTheme());
        roadShowModel.setUserId(roadShowAddVo.getUserId());
        // 路演类型：路演、反路演、午餐路演、午餐反路演、专家反路演、专家午餐反路演
        String roadType = roadShowAddVo.getRoadType();
        roadShowModel.setRoadType(roadType);
        roadShowModel.setStatus((byte)0);
        roadShowModel.setBuyerCompanyId(roadShowAddVo.getBuyerCompanyId());
        roadShowModel.setBuyerCompanyName(roadShowAddVo.getBuyerCompanyName());

        roadShowManageTransactionService.save(roadShowModel, roadShowAddVo);
    }

    @Override
    public Map<String, List<RoadShowAddVo>> getRoadShowListByBroker(RoadShowQueryVo roadShowQueryVo) throws Exception {
        log.info("券商视角查询条件input:{}", JSON.toJSONString(roadShowQueryVo));
        String parentId = userInfoService.getUserParentId(roadShowQueryVo.getUserId());
        log.info("parentId:{}", parentId);
        List<Long> ids = roadShowMapper.getRoadShowIdsByBroker(roadShowQueryVo, parentId);
        log.info("ids:{}", ids);
        return getCommonRoadShowList(ids, roadShowQueryVo.getUserId());
    }

    @Override
    public RoadShowAddVo getRoadShowDetail(Long id, String userId) throws Exception {
        RoadShowModel roadShowModel = getRoadShowById(id);

        if (roadShowModel == null) {
            return null;
        }

        RoadShowAddVo roadShowAddVo = new RoadShowAddVo();
        roadShowAddVo.setId(roadShowModel.getId());
        roadShowAddVo.setTheme(roadShowModel.getTheme());
        roadShowAddVo.setIndustryId(roadShowModel.getIndustryId());
        roadShowAddVo.setIndustryName(roadShowModel.getIndustryName());
        roadShowAddVo.setCompanyId(roadShowModel.getCompanyId());
        roadShowAddVo.setCompanyName(roadShowModel.getCompanyName());
        roadShowAddVo.setSellerId(roadShowModel.getSellerId());
        roadShowAddVo.setSellerName(roadShowModel.getSellerName());
        roadShowAddVo.setSellerCompanyId(roadShowModel.getSellerCompanyId());
        roadShowAddVo.setSellerCompanyName(roadShowModel.getSellerCompanyName());
        roadShowAddVo.setRoadShowDate(TimeUtil.toString(roadShowModel.getRoadShowDate(), "yyyy-MM-dd"));
        Date activityStartTime = TimeUtil.parseDateStr(roadShowAddVo.getRoadShowDate() + " " + roadShowModel.getActivityStartTime(), "yyyy-MM-dd HH:mm:ss");
        roadShowAddVo.setActivityStartTime(TimeUtil.toString(activityStartTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        Date activityEndTime = TimeUtil.parseDateStr(roadShowAddVo.getRoadShowDate() + " " + roadShowModel.getActivityEndTime(), "yyyy-MM-dd HH:mm:ss");
        roadShowAddVo.setActivityEndTime(TimeUtil.toString(activityEndTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        roadShowAddVo.setActivityDesc(roadShowModel.getActivityDesc());
        roadShowAddVo.setMeetingRoom(roadShowModel.getMeetingRoom());
        roadShowAddVo.setExistMeetingRoom(!StringUtil.isEmpty(roadShowModel.getMeetingRoom()));
        roadShowAddVo.setRoadType(roadShowModel.getRoadType());
        roadShowAddVo.setStatus(roadShowModel.getStatus());
        roadShowAddVo.setBuyerCompanyId(roadShowModel.getBuyerCompanyId());
        roadShowAddVo.setBuyerCompanyName(roadShowModel.getBuyerCompanyName());

        List<RoadShowToBuyerModel> buyerList = roadShowToBuyerMapper.getBuyerListByRoadShowId(roadShowModel.getId());
        Map<String, String> buyerCompanyMap = new HashMap<>();
        List<RoadShowBuyerVo> buyers = new ArrayList<>();
        Boolean myItinerary = false;
        for (RoadShowToBuyerModel roadShowToBuyerModel : buyerList) {
            RoadShowBuyerVo roadShowBuyerVo = new RoadShowBuyerVo();
            roadShowBuyerVo.setBuyerId(roadShowToBuyerModel.getBuyerId());
            roadShowBuyerVo.setBuyerName(roadShowToBuyerModel.getBuyerName());
            roadShowBuyerVo.setBuyerCompanyId(roadShowToBuyerModel.getBuyerCompanyId());
            roadShowBuyerVo.setBuyerCompanyName(roadShowToBuyerModel.getBuyerCompanyName());
            buyers.add(roadShowBuyerVo);

            if (!buyerCompanyMap.containsKey(roadShowToBuyerModel.getBuyerCompanyId())) {
                buyerCompanyMap.put(roadShowToBuyerModel.getBuyerCompanyId(), roadShowToBuyerModel.getBuyerCompanyName());
            }

            if (!myItinerary && roadShowToBuyerModel.getBuyerId().equals(userId)) {
                myItinerary = true;
            }
        }
        roadShowAddVo.setBuyers(buyers);
        roadShowAddVo.setMyItinerary(myItinerary);

        List<RoadShowBuyerCompanyVo> buyerCompanyList = new ArrayList<>();
        for (Map.Entry<String, String> entry : buyerCompanyMap.entrySet()) {
            RoadShowBuyerCompanyVo buyerCompanyVo = new RoadShowBuyerCompanyVo();
            buyerCompanyVo.setBuyerCompanyId(entry.getKey());
            buyerCompanyVo.setBuyerCompanyName(entry.getValue());
            buyerCompanyList.add(buyerCompanyVo);
        }
        roadShowAddVo.setBuyerCompanyList(buyerCompanyList);

        List<RoadShowToCompanyModel> companyDomainList = roadShowToCompanyMapper.getCompanyListByRoadShowId(roadShowModel.getId());
        List<RoadShowCompanyVo> companys = new ArrayList<>();
        companyDomainList.forEach(roadShowToCompanyDomain -> {
            RoadShowCompanyVo companyVo = new RoadShowCompanyVo();
            companyVo.setCompanyId(roadShowToCompanyDomain.getCompanyId());
            companyVo.setCompanyName(roadShowToCompanyDomain.getCompanyName());
            companys.add(companyVo);
        });
        roadShowAddVo.setCompanys(companys);

        List<RoadShowToSellerModel> sellerModelList = roadShowToSellerMapper.getSellerListByRoadShowId(roadShowModel.getId());
        List<RoadShowSellerVo> sellers = new ArrayList<>();
        if (sellerModelList != null && !sellerModelList.isEmpty()) {
            sellerModelList.forEach(roadShowToSellerModel -> {
                RoadShowSellerVo roadShowSellerVo = new RoadShowSellerVo();
                roadShowSellerVo.setSellerId(roadShowToSellerModel.getSellerId());
                roadShowSellerVo.setSellerName(roadShowToSellerModel.getSellerName());

                sellers.add(roadShowSellerVo);
            });
        }
        roadShowAddVo.setSellers(sellers);

        return roadShowAddVo;
    }

    @Override
    public RoadShowModel getRoadShowById(Long id) throws Exception {
        return roadShowMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, List<RoadShowAddVo>> getRoadShowListByFund(RoadShowQueryVo roadShowQueryVo) throws Exception {
        log.info("基金公司视角查询条件input:{}", JSON.toJSONString(roadShowQueryVo));
        String parentId = userInfoService.getUserParentId(roadShowQueryVo.getUserId());
        log.info("parentId:{}", parentId);
        List<Long> ids = roadShowMapper.getRoadShowIdsByFund(roadShowQueryVo, parentId);
        log.info("ids:{}", ids);
        return getCommonRoadShowList(ids, roadShowQueryVo.getUserId());
    }

    @Override
    public void joinTrip(Long roadShowId, String userId) throws Exception {
        AccountInfoResponse userInfo = userInfoService.getUserInfo(userId);
        RoadShowToBuyerModel roadShowToBuyerModel = new RoadShowToBuyerModel();
        roadShowToBuyerModel.setBuyerId(userInfo.getUser_id());
        roadShowToBuyerModel.setBuyerName(userInfo.getUsername());
        roadShowToBuyerModel.setBuyerCompanyId(userInfo.getCompany().getCompany_id().toString());
        roadShowToBuyerModel.setBuyerCompanyName(userInfo.getCompany().getSname());
        roadShowToBuyerModel.setRoadShowId(roadShowId);
        roadShowManageTransactionService.joinTrip(roadShowToBuyerModel);
    }

    @Override
    public void cancelTrip(Long roadShowId, String userId) throws Exception {
        roadShowManageTransactionService.cancelTrip(roadShowId, userId);
    }

    @Override
    public void updateById(RoadShowAddVo roadShowAddVo) throws Exception {
        RoadShowModel roadShowModel = getRoadShowById(roadShowAddVo.getId());
        if (roadShowModel == null) {
            throw new RuntimeException("id:" + roadShowAddVo.getId() + ", no data");
        }

        roadShowModel.setTheme(roadShowAddVo.getTheme());
        roadShowModel.setIndustryId(roadShowAddVo.getIndustryId());
        roadShowModel.setIndustryName(roadShowAddVo.getIndustryName());
        roadShowModel.setCompanyId(roadShowAddVo.getCompanyId());
        roadShowModel.setCompanyName(roadShowAddVo.getCompanyName());
        roadShowModel.setSellerId(roadShowAddVo.getSellerId());
        roadShowModel.setSellerName(roadShowAddVo.getSellerName());
        roadShowModel.setSellerCompanyId(roadShowAddVo.getSellerCompanyId());
        roadShowModel.setSellerCompanyName(roadShowAddVo.getSellerCompanyName());
        roadShowModel.setRoadShowDate(TimeUtil.parseDateStr(roadShowAddVo.getRoadShowDate(), "yyyy-MM-dd"));
        roadShowModel.setActivityStartTime(roadShowAddVo.getActivityStartTime());
        roadShowModel.setActivityEndTime(roadShowAddVo.getActivityEndTime());
        roadShowModel.setActivityDesc(roadShowAddVo.getActivityDesc());
        roadShowModel.setUpdateTime(new Date());
        String roadType = roadShowAddVo.getRoadType();
        roadShowModel.setRoadType(roadType);
        roadShowModel.setBuyerCompanyId(roadShowAddVo.getBuyerCompanyId());
        roadShowModel.setBuyerCompanyName(roadShowAddVo.getBuyerCompanyName());

        List<RoadShowBuyerVo> buyers = new ArrayList<>();
        for (int i = 0; i < roadShowAddVo.getBuyers().size(); i++) {
            RoadShowBuyerVo buyerVo = new RoadShowBuyerVo();
            RoadShowBuyerVo item = roadShowAddVo.getBuyers().get(i);
            buyerVo.setBuyerId(item.getBuyerId());
            buyerVo.setBuyerName(item.getBuyerName());
            buyerVo.setBuyerCompanyId(item.getBuyerCompanyId());
            buyerVo.setBuyerCompanyName(item.getBuyerCompanyName());

            buyers.add(buyerVo);
        }

        List<RoadShowSellerVo> sellers = new ArrayList<>();
        for (int i = 0; i < roadShowAddVo.getSellers().size(); i++) {
            RoadShowSellerVo sellerVo = new RoadShowSellerVo();
            RoadShowSellerVo item = roadShowAddVo.getSellers().get(i);
            sellerVo.setSellerId(StringUtil.isEmpty(item.getSellerId()) ? "" : item.getSellerId());
            sellerVo.setSellerName(item.getSellerName());

            sellers.add(sellerVo);
        }

        roadShowManageTransactionService.update(roadShowModel, buyers, roadShowAddVo.getCompanys(), sellers);
    }

    @Override
    public void deleteById(Long id, String userId) throws Exception {
        RoadShowModel roadShowModel = getRoadShowById(id);
        if (roadShowModel == null) {
            throw new RuntimeException("id:" + id + ", no data");
        }

        AccountInfoResponse userInfo = userInfoService.getUserInfo(userId);
        CompanyTypeEnum companyTypeEnum = userInfo.getCompany() == null ? null : userInfo.getCompany().getType();
        RoadShowModel updateRoadShow = new RoadShowModel();
        updateRoadShow.setId(id);
        Date now = new Date();
        roadShowModel.setUpdateTime(now);
        if (CompanyTypeEnum.BROKER.equals(companyTypeEnum) && userId.equals(roadShowModel.getUserId())) {
            updateRoadShow.setStatus((byte) 2);
        } else if (CompanyTypeEnum.FUND.equals(companyTypeEnum) && isSuperAdministrator(userInfo)) {
            updateRoadShow.setStatus((byte) 1);
        } else {
            throw new CustomException(SystemEnumCodeConfig.ERROR_AUTHORIZED);
        }

        String roadShowStartTimeStr = TimeUtil.toString(roadShowModel.getRoadShowDate(), "yyyy-MM-dd") + " " + roadShowModel.getActivityStartTime();
        Date roadShowStartTime = TimeUtil.parseDateStr(roadShowStartTimeStr, "yyyy-MM-dd HH:mm:ss");
        if (roadShowStartTime == null || now.getTime() >= roadShowStartTime.getTime()) {
            throw new CustomException(RoadShowEnumCodeConfig.ERROR_ROAD_SHOW_DELETE_TIME_START);
        }

        roadShowManageTransactionService.updateRoadShowById(updateRoadShow);
    }

    @Override
    public Map<String, Object> getBrokerFilterCondition(String userId) throws Exception {
        List<Map<String, String>> companyList = roadShowMapper.getDistinctCompanyList(userId, null);
        List<Map<String, String>> industryList = roadShowMapper.getDistinctIndustryList(userId, null);
        List<Map<String, String>> sellerList = roadShowMapper.getDistinctSellerList(userId, null);
        List<Map<String, String>> buyerList = roadShowMapper.getDistinctBuyerList(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("companyList", companyList);
        result.put("industryList", industryList);
        result.put("sellerList", sellerList);
        result.put("buyerList", buyerList);
        return result;
    }

    @Override
    public Map<String, Object> getFundCompanyFilterCondition(String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        log.info("parentId:{}", parentId);
        List<Map<String, String>> companyList = roadShowMapper.getDistinctCompanyList(null, parentId);
        List<Map<String, String>> industryList = roadShowMapper.getDistinctIndustryList(null, parentId);
        List<Map<String, String>> sellerList = roadShowMapper.getDistinctSellerList(null, parentId);
        List<Map<String, String>> sellerCompanyList = roadShowMapper.getDistinctSellerCompanyList(parentId);

        Map<String, Object> result = new HashMap<>();
        result.put("companyList", companyList);
        result.put("industryList", industryList);
        result.put("sellerList", sellerList);
        result.put("sellerCompanyList", sellerCompanyList);
        return result;
    }

    @Override
    public Set<String> getMeetingRoomList(String userId, String keyword, Integer limit) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        List<String> meetingRoomList = roadShowMapper.getMeetingRoomListByBuyerCompanyId(parentId);

        if (meetingRoomList == null || meetingRoomList.isEmpty()) {
            return null;
        }

        Set<String> result = new HashSet<>();
        for (int i = 0; i < meetingRoomList.size(); i++) {
            String meetingRoom = meetingRoomList.get(i);
            if (StringUtil.isEmpty(meetingRoom)) {
                continue;
            }
            if (meetingRoom.contains(",")) {
                List<String> meetingRooms = Arrays.asList(meetingRoom.split(","));
                for (String meetingRoomStr : meetingRooms) {
                    if (StringUtil.isEmpty(keyword) || meetingRoomStr.contains(keyword)) {
                        if (limit != null && limit > 0 && result.size() >= limit) {
                            return result;
                        }
                        result.add(meetingRoomStr);
                    }
                }
            } else {
                if (StringUtil.isEmpty(keyword) || meetingRoom.contains(keyword)) {
                    if (limit != null && limit > 0 && result.size() >= limit) {
                        return result;
                    }
                    result.add(meetingRoom);
                }
            }
        }
        return result;
    }

    @Override
    public void updateMeetingRoomById(Long id, String meetingRoom) throws Exception {
        RoadShowModel existRoadShowModel = getRoadShowById(id);
        if (existRoadShowModel == null) {
            throw new RuntimeException("id:" + id + ", no data");
        }

        RoadShowModel roadShowModel = new RoadShowModel();
        roadShowModel.setId(id);
        roadShowModel.setMeetingRoom(meetingRoom);
        roadShowModel.setUpdateTime(new Date());
        roadShowManageTransactionService.updateRoadShowById(roadShowModel);
    }

    @Override
    public List<String> getIndustryList(String userId, String buyerCompanyId, String keyword, Integer limit) throws Exception {
        AccountInfoResponse userInfo = userInfoService.getUserInfo(userId);
        String broker = userInfo.getCompany().getSname();
        return roadShowBrokerAnalystMapper.getIndustryByParentIdAndBrokerAndKeyword(buyerCompanyId, broker, keyword, limit);
    }

    @Override
    public void saveSellerAnalyst(RoadShowSellerAnalystSaveVo sellerAnalystSaveVo) throws Exception {
        AccountInfoResponse userInfo = userInfoService.getUserInfo(sellerAnalystSaveVo.getUserId());
        RoadShowBrokerAnalystModel existBrokerAnalystModel = roadShowBrokerAnalystMapper.getByParentIdAndBroker(sellerAnalystSaveVo.getBuyerCompanyId(), userInfo.getCompany().getSname(), sellerAnalystSaveVo.getIndustry(), sellerAnalystSaveVo.getAnalyst());
        if (existBrokerAnalystModel != null) {
            throw new CustomException(RoadShowEnumCodeConfig.ERROR_ROAD_SHOW_SELLER_ANALYST_EXIST);
        } else {
            // 是否存在对应券商对应行业中研究员名称为空字符串的记录，如果存在表示改券商行业没有对应研究员，更新这条数据为当前研究员名称，否则直接添加
            existBrokerAnalystModel = roadShowBrokerAnalystMapper.getByParentIdAndBroker(sellerAnalystSaveVo.getBuyerCompanyId(), userInfo.getCompany().getSname(), sellerAnalystSaveVo.getIndustry(), "");
            RoadShowBrokerAnalystModel brokerAnalystModel = new RoadShowBrokerAnalystModel();
            if (existBrokerAnalystModel == null) {
                brokerAnalystModel.setParentId(sellerAnalystSaveVo.getBuyerCompanyId());
                brokerAnalystModel.setBroker(userInfo.getCompany().getSname());
                brokerAnalystModel.setIndustry(sellerAnalystSaveVo.getIndustry());
                brokerAnalystModel.setAnalyst(sellerAnalystSaveVo.getAnalyst());
                brokerAnalystModel.setSource(2);
                Date date = new Date();
                brokerAnalystModel.setCreateTime(date);
                brokerAnalystModel.setUpdateTime(date);
                brokerAnalystModel.setCreateId(sellerAnalystSaveVo.getUserId());
                brokerAnalystModel.setUpdateId(sellerAnalystSaveVo.getUserId());
            } else {
                brokerAnalystModel.setId(existBrokerAnalystModel.getId());
                brokerAnalystModel.setAnalyst(sellerAnalystSaveVo.getAnalyst());
                brokerAnalystModel.setSource(2);
                Date date = new Date();
                brokerAnalystModel.setUpdateTime(date);
                brokerAnalystModel.setUpdateId(sellerAnalystSaveVo.getUserId());
            }

            roadShowManageTransactionService.saveOrUpdateSellerAnalyst(brokerAnalystModel);
        }
    }

    @Override
    public Map<String, Object> getSellerAnalystList(String userId, String keyword, String buyerCompanyId, String industry) throws Exception {
        AccountInfoResponse userInfo = userInfoService.getUserInfo(userId);
        if (userInfo == null || userInfo.getCompany() == null
                || StringUtil.isEmpty(userInfo.getCompany().getSname())
                || StringUtil.isEmpty(userInfo.getCompany().getCompany_id())) {
            throw new RuntimeException("userId:" + userId + "用户信息中无公司信息");
        }
        List<Map<String, Object>> analystList = roadShowBrokerAnalystMapper.getAnalystList(buyerCompanyId, userInfo.getCompany().getSname(), industry, keyword);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("parentId", userInfo.getCompany().getCompany_id());
        result.put("parentName", userInfo.getCompany().getSname());
        result.put("analysts", analystList);
        return result;
    }

    @Override
    public void export(ByteArrayOutputStream bos, String userId, String startTime, String endTime, String fields) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        log.info("parentId:{}", parentId);
        Date startTimeDate = TimeUtil.parseDateStr(startTime, "yyyy-MM-dd");
        Date endTimeDate = TimeUtil.parseDateStr(endTime, "yyyy-MM-dd");
        if (startTimeDate == null || endTimeDate == null) {
            throw new ValidatorException();
        }

        if (StringUtil.isEmpty(fields)) {
            fields = "activityStartTime,title,buyer";
        }
        List<String> fieldList = Arrays.asList(fields.split(","));
        List<String> headList = new ArrayList<>();
        headList.add("");
        fieldList.forEach(field -> headList.add(RoadShowExportEnum.getFieldName(field)));

        List<List<String>> dataList = new ArrayList<>();
        List<Map<String, Object>> list = roadShowMapper.getByTime(parentId, startTimeDate, endTimeDate);

        String currentRoadShowDateStr = "";
        for (Map<String, Object> map : list) {
            List<String> rowList = new ArrayList<>();
            Date roadShowDate = (Date) map.get("roadShowDate");
            String roadShowDateStr = TimeUtil.toString(roadShowDate, "yyyy-MM-dd");
            String week = TimeUtil.getDayOfWeek(roadShowDate);
            roadShowDateStr = roadShowDateStr + "(" + week + ")";
            if (StringUtil.isEmpty(currentRoadShowDateStr) || !currentRoadShowDateStr.equals(roadShowDateStr)) {
                currentRoadShowDateStr = roadShowDateStr;
            } else {
                roadShowDateStr = "top";
            }
            rowList.add(roadShowDateStr);

            for (String field : fieldList) {
                if (!StringUtil.isEmpty(field)) {
                    String data;
                    if ("title".equals(field)) {
                        String sellerCompany = StringUtil.isEmpty(map.get("sellerCompany")) ? "" : map.get("sellerCompany").toString();
                        String industry = StringUtil.isEmpty(map.get("industry")) ? "" : map.get("industry").toString();
                        String roadType = StringUtil.isEmpty(map.get("roadType")) ? "" : map.get("roadType").toString();
                        data = sellerCompany + industry + roadType;
                    } else {
                        data = map.get(field) == null ? "" : map.get(field).toString();
                    }
                    rowList.add(data);
                }
            }
            dataList.add(rowList);
        }
        Map<String, List<List<String>>> sheetData = new HashMap<>();
        sheetData.put("路演预约", dataList);

        ExcelUtil.exportTraderMarkTemplate(bos, headList, sheetData, "路演预约");
    }

    @Override
    public void upload(MultipartFile file, String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);

        String excelType;
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(ExcelTypeEnum.XLSX.getType())) {
            excelType = ExcelTypeEnum.XLSX.getType();
        } else if (fileName.endsWith(ExcelTypeEnum.XLS.getType())) {
            excelType = ExcelTypeEnum.XLS.getType();
        } else {
            throw new RuntimeException("excel type error");
        }
        List<List<String>> excelDataList = ExcelUtil.readAll(file.getInputStream(), excelType, 2, null, 1, 3);

        Map<String, Map<String, Set<String>>> brokerMap = new LinkedHashMap<>();
        Set<String> brokerList = getBrokerListFromCommonData();
        for (int i = 0; i < excelDataList.size(); i++) {
            List<String> row = excelDataList.get(i);
            String broker = row.get(0);
            String industry = row.get(1);
            String analyst = row.get(2);

            if (StringUtil.isEmpty(broker) && StringUtil.isEmpty(industry) && StringUtil.isEmpty(analyst)) {
                continue;
            }

            if (i == 0 && (StringUtil.isEmpty(broker) || StringUtil.isEmpty(industry))) {
                throw new CustomException(RoadShowEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
            }

            if (i != 0) {
                if (StringUtil.isEmpty(broker)) {
                    broker = excelDataList.get(i - 1).get(0);
                    row.set(0, broker);
                }

                if (StringUtil.isEmpty(industry)) {
                    industry = excelDataList.get(i - 1).get(1);
                    row.set(1, industry);
                }

                excelDataList.set(i, row);
            }

            if (!brokerList.contains(broker)) {
                throw new CustomException(RoadShowEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_BROKER);
            }

            Map<String, Set<String>> industryMap = brokerMap.getOrDefault(broker, new LinkedHashMap<>());
            Set<String> analystSet = industryMap.getOrDefault(industry, new LinkedHashSet<>());
            if (!StringUtil.isEmpty(analyst)) {
                analystSet.add(analyst);
            }
            industryMap.put(industry, analystSet);
            brokerMap.put(broker, industryMap);
        }

        List<Map<String, Object>> list = roadShowBrokerAnalystMapper.getBrokerIndustryByParentIdAndSource(parentId, 2);

        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                String broker = map.get("broker").toString();
                String industry = map.get("industry").toString();
                if (brokerMap.containsKey(broker)) {
                    Map<String, Set<String>> industryMap = brokerMap.get(broker);
                    if (industryMap.containsKey(industry) && industryMap.get(industry).isEmpty()) {
                        industryMap.remove(industry);

                        brokerMap.put(broker, industryMap);
                    }
                }
            }
        }

        Date now = new Date();
        List<RoadShowBrokerAnalystModel> brokerAnalystModelList = new ArrayList<>();

        brokerMap.forEach((broker, industryMap) -> {
            industryMap.forEach((industry, analystSet) -> {
                if (analystSet.isEmpty()) {
                    RoadShowBrokerAnalystModel brokerAnalystModel = new RoadShowBrokerAnalystModel();
                    brokerAnalystModel.setParentId(parentId);
                    brokerAnalystModel.setBroker(broker);
                    brokerAnalystModel.setIndustry(industry);
                    brokerAnalystModel.setAnalyst("");
                    brokerAnalystModel.setSource(1);
                    brokerAnalystModel.setCreateTime(now);
                    brokerAnalystModel.setUpdateTime(now);
                    brokerAnalystModel.setCreateId(userId);
                    brokerAnalystModel.setUpdateId(userId);
                    brokerAnalystModelList.add(brokerAnalystModel);
                } else {
                    analystSet.forEach(analyst -> {
                        RoadShowBrokerAnalystModel brokerAnalystModel = new RoadShowBrokerAnalystModel();
                        brokerAnalystModel.setParentId(parentId);
                        brokerAnalystModel.setBroker(broker);
                        brokerAnalystModel.setIndustry(industry);
                        brokerAnalystModel.setAnalyst(analyst);
                        brokerAnalystModel.setSource(1);
                        brokerAnalystModel.setCreateTime(now);
                        brokerAnalystModel.setUpdateTime(now);
                        brokerAnalystModel.setCreateId(userId);
                        brokerAnalystModel.setUpdateId(userId);
                        brokerAnalystModelList.add(brokerAnalystModel);
                    });
                }

            });
        });

        roadShowManageTransactionService.insertBatchRoadShowBrokerAnalyst(parentId, brokerAnalystModelList);
    }

    @Override
    public Integer getRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception {
        return roadShowMapper.getRoadShowCountBySellerAndDate(parentId,
                TimeUtil.parseDateStr(startTime, "yyyy-MM-dd HH:mm:ss.SSS"),
                TimeUtil.parseDateStr(endTime, "yyyy-MM-dd HH:mm:ss.SSS"),
                broker,
                industry,
                analyst);
    }

    @Override
    public List<Map<String, Object>> getCompanyRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception {
        return roadShowMapper.getCompanyRoadShowCountBySellerAndDate(parentId,
                broker,
                industry,
                analyst,
                TimeUtil.parseDateStr(startTime, "yyyy-MM-dd HH:mm:ss.SSS"),
                TimeUtil.parseDateStr(endTime, "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Override
    public List<String> getRoadShowTimeBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst, String stockCode, String stockName) {
        return roadShowMapper.getRoadShowTimeBySellerAndDate(parentId,
                TimeUtil.parseDateStr(startTime, "yyyy-MM-dd HH:mm:ss.SSS"),
                TimeUtil.parseDateStr(endTime, "yyyy-MM-dd HH:mm:ss.SSS"),
                broker,
                industry,
                analyst,
                stockCode,
                stockName);
    }

    private Set<String> getBrokerListFromCommonData() throws Exception {
        Response<List<CompanyResponse>> response = apiPlatFormClient.broker();
        if (response == null ||response.getCode() != 200 || response.getData() == null || response.getData().isEmpty()) {
            throw new RuntimeException("从平台获取券商列表失败,返回值：" + JSONObject.toJSONString(response));
        } else {
            List<CompanyResponse> data = response.getData();
            Set<String> brokerSet = new HashSet<>();
            data.forEach(companyResponse -> brokerSet.add(companyResponse.getSname()));
            return brokerSet;
        }
    }

    /**
     * 获取路演公共方法
     * @param ids
     * @param userId
     * @return
     */
    public Map<String, List<RoadShowAddVo>> getCommonRoadShowList(List<Long> ids, String userId) throws Exception {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<RoadShowAddVo> roadShows = new ArrayList<RoadShowAddVo>(ids.size());
        ids.forEach(id -> {
            try {
                roadShows.add(getRoadShowDetail(id, userId));
            } catch (Exception e) {
                log.error("根据id获取路演详情异常", e);
            }
        });
        if (CollectionUtils.isEmpty(roadShows)) {
            return null;
        }
        return roadShows.stream().collect(Collectors.groupingBy(RoadShowAddVo::getRoadShowDate));
    }

    /**
     * 判断用户是否有管理员权限
     * @param userInfo
     * @return
     */
    private Boolean isSuperAdministrator(AccountInfoResponse userInfo) {
        List<RoleResponse> roles = userInfo.getRoles();
        Boolean result = false;
        if (roles != null && !roles.isEmpty()) {
            for (RoleResponse roleResponse : roles) {
                result = roleResponse != null && "超级管理员".equals(roleResponse.getName()) && "1".equals(roleResponse.getStatus());
                if (result) {
                    break;
                }
            }
        }
        return result;
    }
}
