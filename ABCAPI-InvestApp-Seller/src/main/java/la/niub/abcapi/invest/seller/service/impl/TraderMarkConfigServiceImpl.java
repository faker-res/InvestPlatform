package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.exception.CustomException;
import la.niub.abcapi.invest.seller.component.util.ExcelUtil;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.code.TraderMarkEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.ExcelTypeEnum;
import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.model.response.*;
import la.niub.abcapi.invest.seller.model.response.client.AccountInfoResponse;
import la.niub.abcapi.invest.seller.model.response.client.AccountListResponse;
import la.niub.abcapi.invest.seller.model.response.client.CompanyResponse;
import la.niub.abcapi.invest.seller.model.vo.*;
import la.niub.abcapi.invest.seller.service.ITraderMarkConfigService;
import la.niub.abcapi.invest.seller.service.ITraderMarkConfigTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : ppan
 * @description : 券商评分设置service
 * @date : 2019-01-19 11:50
 */
@Service
public class TraderMarkConfigServiceImpl implements ITraderMarkConfigService {

    @Autowired
    private TraderMarkConfigModeMapper traderMarkConfigModeMapper;

    @Autowired
    private TraderMarkModeMapper traderMarkModeMapper;

    @Autowired
    private ITraderMarkConfigTransactionService traderMarkConfigTransactionService;

    @Autowired
    private TraderMarkConfigDimensionMapper traderMarkConfigDimensionMapper;

    @Autowired
    private TraderMarkConfigObjectMapper traderMarkConfigObjectMapper;

    @Autowired
    private TraderMarkConfigWeightMapper traderMarkConfigWeightMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Override
    public Map<String, Object> getModeList(String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkConfigModeModel configModeModel = traderMarkConfigModeMapper.selectByParentId(parentId);
        Long currentModeId = configModeModel == null ? null : configModeModel.getModeId();
        List<TraderMarkModeModel> modeList = traderMarkModeMapper.selectAll();

        Map<String, Object> result = new HashMap<>();
        result.put("currentModeId", currentModeId);
        result.put("modeList", modeList);

        return result;
    }

    @Override
    public void saveOrUpdateMode(TraderMarkConfigModeAddVo configModeSaveVo) throws Exception {
        TraderMarkModeModel traderMarkModeModel = traderMarkModeMapper.selectByPrimaryKey(configModeSaveVo.getModeId());
        if (traderMarkModeModel == null) {
            throw new RuntimeException("modeId no exist");
        }
        String parentId = userInfoService.getUserParentId(configModeSaveVo.getUserId());
        TraderMarkConfigModeModel existConfigModeModel = traderMarkConfigModeMapper.selectByParentId(parentId);

        TraderMarkConfigModeModel configModeModel = new TraderMarkConfigModeModel();
        configModeModel.setModeId(configModeSaveVo.getModeId());
        configModeModel.setUpdateId(configModeSaveVo.getUserId());
        Date now = new Date();
        configModeModel.setUpdateTime(now);

        if (existConfigModeModel != null) {
            configModeModel.setId(existConfigModeModel.getId());
            traderMarkConfigTransactionService.updateMode(configModeModel);
        } else {
            configModeModel.setParentId(parentId);
            configModeModel.setCreateId(configModeSaveVo.getUserId());
            configModeModel.setCreateTime(now);

            traderMarkConfigTransactionService.saveMode(configModeModel);
        }
    }

    @Override
    public List<TraderMarkConfigDimensionResponse> getDimensionList(String userId) throws Exception {
        List<TraderMarkConfigDimensionResponse> result = new ArrayList<>();

        String parentId = userInfoService.getUserParentId(userId);
        List<TraderMarkConfigDimensionModel> list = traderMarkConfigDimensionMapper.selectByParentId(parentId);
        list.forEach(configDimensionModel -> {
            TraderMarkConfigDimensionResponse itemResponse = new TraderMarkConfigDimensionResponse();
            itemResponse.setId(configDimensionModel.getId());
            itemResponse.setDimension(configDimensionModel.getDimension());
            itemResponse.setCalculateStatus(configDimensionModel.getCalculateStatus());

            result.add(itemResponse);
        });

        return result;
    }

    @Override
    public void saveDimension(TraderMarkConfigDimensionAddVo configDimensionAddVo) throws Exception {
        String parentId = userInfoService.getUserParentId(configDimensionAddVo.getUserId());
        TraderMarkConfigDimensionModel existConfigDimensionModel = traderMarkConfigDimensionMapper.selectByParentIdAndDimension(parentId, configDimensionAddVo.getDimension(), null);
        if (existConfigDimensionModel != null) {
            throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
        } else {
            TraderMarkConfigDimensionModel configDimensionModel = new TraderMarkConfigDimensionModel();
            configDimensionModel.setDimension(configDimensionAddVo.getDimension());
            configDimensionModel.setCalculateStatus(configDimensionAddVo.getCalculateStatus());
            configDimensionModel.setParentId(parentId);
            configDimensionModel.setCreateId(configDimensionAddVo.getUserId());
            configDimensionModel.setUpdateId(configDimensionAddVo.getUserId());
            Date now = new Date();
            configDimensionModel.setCreateTime(now);
            configDimensionModel.setUpdateTime(now);

            traderMarkConfigTransactionService.saveDimension(configDimensionModel);
        }
    }

    @Override
    public TraderMarkConfigDimensionResponse getDimensionDetail(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkConfigDimensionModel configDimensionModel = traderMarkConfigDimensionMapper.selectByPrimaryKey(id);
        if (configDimensionModel == null || !configDimensionModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not select");
        } else {
            TraderMarkConfigDimensionResponse response = new TraderMarkConfigDimensionResponse();
            response.setId(configDimensionModel.getId());
            response.setDimension(configDimensionModel.getDimension());
            response.setCalculateStatus(configDimensionModel.getCalculateStatus());
            return response;
        }
    }

    @Override
    public void updateDimension(TraderMarkConfigDimensionUpdateVo configDimensionUpdateVo) throws Exception {
        String parentId = userInfoService.getUserParentId(configDimensionUpdateVo.getUserId());
        TraderMarkConfigDimensionModel existConfigDimensionModel = traderMarkConfigDimensionMapper.selectByPrimaryKey(configDimensionUpdateVo.getId());
        if (existConfigDimensionModel == null || !existConfigDimensionModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + configDimensionUpdateVo.getId() + ", no data or other company data not update");
        } else {
            existConfigDimensionModel = traderMarkConfigDimensionMapper.selectByParentIdAndDimension(parentId, configDimensionUpdateVo.getDimension(), configDimensionUpdateVo.getId());
            if (existConfigDimensionModel != null) {
                throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
            }

            TraderMarkConfigDimensionModel configDimensionModel = new TraderMarkConfigDimensionModel();
            configDimensionModel.setId(configDimensionUpdateVo.getId());
            configDimensionModel.setDimension(configDimensionUpdateVo.getDimension());
            configDimensionModel.setCalculateStatus(configDimensionUpdateVo.getCalculateStatus());
            configDimensionModel.setUpdateId(configDimensionUpdateVo.getUserId());
            configDimensionModel.setUpdateTime(new Date());

            traderMarkConfigTransactionService.updateDimension(configDimensionModel);
        }
    }

    @Override
    public void deleteDimension(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkConfigDimensionModel existConfigDimensionModel = traderMarkConfigDimensionMapper.selectByPrimaryKey(id);
        if (existConfigDimensionModel == null || !existConfigDimensionModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not update");
        } else {
            traderMarkConfigTransactionService.deleteDimension(id);
        }
    }

    @Override
    public void uploadObject(MultipartFile file, String userId) throws Exception {
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
        List<List<String>> excelDataList = ExcelUtil.read(file.getInputStream(), excelType, 0, 2, null, 1, 3);

        Map<String, Map<String, List<String>>> brokerMap = new LinkedHashMap<>();
        Map<String, List<String>> industryMap = new LinkedHashMap<>();
        List<String> analystList = new ArrayList<>();
        String currentBroker = "";
        String currentIndustry = "";
        Set<String> brokerList = getBrokerListFromCommonData();
        for (int i = 0; i < excelDataList.size(); i++) {
            String broker = excelDataList.get(i).get(0);
            String industry = excelDataList.get(i).get(1);
            String analyst = excelDataList.get(i).get(2);

            if (StringUtil.isEmpty(analyst)) {
                if (i == 0) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                } else {
                    industryMap.put(currentIndustry, analystList);
                    brokerMap.put(currentBroker, industryMap);
                    break;
                }
            } else {
                if (i == 0) {
                    if (StringUtil.isEmpty(broker) || StringUtil.isEmpty(industry)) {
                        throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                    } else {
                        currentBroker = broker;
                        currentIndustry = industry;

                        if (!brokerList.contains(broker)) {
                            throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_BROKER);
                        }

                        analystList.add(analyst);
                    }
                } else {
                    if (!StringUtil.isEmpty(broker) && StringUtil.isEmpty(industry)) {
                        throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                    }

                    if (StringUtil.isEmpty(industry)) {
                        analystList.add(analyst);
                    } else {
                        if (StringUtil.isEmpty(broker)) {
                            if (currentIndustry.equals(industry)) {
                                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                            } else {
                                if (analystList.size() != new HashSet<>(analystList).size()) {
                                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                                }
                                industryMap.put(currentIndustry, analystList);

                                currentIndustry = industry;
                                analystList = new ArrayList<>();
                                analystList.add(analyst);
                            }
                        } else {
                            if (analystList.size() != new HashSet<>(analystList).size()) {
                                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT);
                            }
                            industryMap.put(currentIndustry, analystList);
                            brokerMap.put(currentBroker, industryMap);

                            currentBroker = broker;
                            currentIndustry = industry;
                            industryMap = new LinkedHashMap<>();
                            analystList = new ArrayList<>();

                            if (!brokerList.contains(broker)) {
                                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_UPLOAD_EXCEL_BROKER);
                            }

                            analystList.add(analyst);
                        }
                    }

                    if (i == excelDataList.size() - 1) {
                        industryMap.put(currentIndustry, analystList);
                        brokerMap.put(currentBroker, industryMap);
                    }
                }
            }
        }

        List<TraderMarkConfigObjectModel> configObjectModelList = new ArrayList<>();
        Date now = new Date();

        brokerMap.forEach((brokerKey, industryKeyMap) -> {
            industryKeyMap.forEach((industryKey, analystNameList) -> {
                analystNameList.forEach(analyst -> {
                    TraderMarkConfigObjectModel configObjectModel = new TraderMarkConfigObjectModel();
                    configObjectModel.setBroker(brokerKey);
                    configObjectModel.setIndustry(industryKey);
                    configObjectModel.setAnalyst(analyst);
                    configObjectModel.setParentId(parentId);
                    configObjectModel.setCreateId(userId);
                    configObjectModel.setUpdateId(userId);
                    configObjectModel.setCreateTime(now);
                    configObjectModel.setUpdateTime(now);

                    configObjectModelList.add(configObjectModel);
                });
            });
        });

        if (configObjectModelList.isEmpty()) {
            throw new RuntimeException("extract excel no data");
        }

        Integer count = traderMarkConfigObjectMapper.selectCountByParentId(parentId);
        // 查询parentId的记录是否存在,如果存在先删除,再添加(0:添加  1：删除再添加)
        Integer option = count == 0 ? 0 : 1;
        traderMarkConfigTransactionService.saveOrUpdateObject(configObjectModelList, parentId, option);
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

    @Override
    public List<Map<String, Object>> getObjectBrokerList(String userId, String keyword, Integer limit) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        return traderMarkConfigObjectMapper.selectBrokerListByParentIdAndKeyword(parentId, keyword, limit);
    }

    @Override
    public List<TraderMarkConfigObjectResponse> getObjectList(String userId, String brokers) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        List<String> brokerList = StringUtil.isEmpty(brokers) ? new ArrayList<>() : Arrays.asList(brokers.split(","));
        List<TraderMarkConfigObjectModel> list = traderMarkConfigObjectMapper.selectByParentIdAndBrokerList(parentId, brokerList);

        List<TraderMarkConfigObjectResponse> result = new ArrayList<>();
        list.forEach(configObjectModel -> {
            TraderMarkConfigObjectResponse itemResponse = new TraderMarkConfigObjectResponse();
            itemResponse.setId(configObjectModel.getId());
            itemResponse.setBroker(configObjectModel.getBroker());
            itemResponse.setIndustry(configObjectModel.getIndustry());
            itemResponse.setAnalyst(configObjectModel.getAnalyst());

            result.add(itemResponse);
        });
        return result;
    }

    @Override
    public List<TraderMarkConfigWeightResponse> getWeightList(String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkConfigModeModel configModeModel = traderMarkConfigModeMapper.selectByParentId(parentId);
        if (configModeModel == null) {
            throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_NO_MODE);
        }
        Long modeId = configModeModel.getModeId();
        List<TraderMarkConfigWeightModel> list = traderMarkConfigWeightMapper.selectByParentId(parentId);

        List<TraderMarkConfigWeightResponse> result = new ArrayList<>();
        list.forEach(configWeightModel -> {
            TraderMarkConfigWeightResponse itemResponse = new TraderMarkConfigWeightResponse();
            itemResponse.setId(configWeightModel.getId());
            itemResponse.setUserId(configWeightModel.getUserId());
            itemResponse.setUserAccount(configWeightModel.getUserAccount());
            itemResponse.setUserName(configWeightModel.getUserName());
            if (modeId == 1 || modeId == 3) {
                itemResponse.setWeight(configWeightModel.getWeight());
            } else {
                itemResponse.setThreshold(configWeightModel.getThreshold());
            }

            result.add(itemResponse);
        });

        return result;
    }

    @Override
    public List<TraderMarkConfigWeightUserInfoResponse> getUserInfoList(String userId, String keyword, Integer limit) throws Exception {
        Response<AccountListResponse> response = apiPlatFormClient.getAccountListByKeyword(userId, keyword, 1, limit);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台获取同公司用户信息失败，错误：" + JSON.toJSONString(response));
        }
        List<AccountInfoResponse> list = response.getData().getList();

        List<TraderMarkConfigWeightUserInfoResponse> result = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return result;
        }

        list.forEach(accountInfoResponse -> {
            TraderMarkConfigWeightUserInfoResponse item = new TraderMarkConfigWeightUserInfoResponse();
            item.setUserId(accountInfoResponse.getUser_id());
            item.setName(accountInfoResponse.getUsername());
            item.setEmail(accountInfoResponse.getEmail());
            item.setTelephone(accountInfoResponse.getMobile());

            result.add(item);
        });

        return result;
    }

    @Override
    public void saveWeight(TraderMarkConfigWeightAddVo configWeightAddVo) throws Exception {
        String currentParentId = userInfoService.getUserParentId(configWeightAddVo.getUserId());

        String weightParentId = userInfoService.getUserParentId(configWeightAddVo.getObjectUserId());

        if (!currentParentId.equals(weightParentId)) {
            throw new RuntimeException("add trader mark config weight no your company user");
        }

        TraderMarkConfigModeModel configModeModel = traderMarkConfigModeMapper.selectByParentId(currentParentId);
        if (configModeModel == null) {
            throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_NO_MODE);
        }

        TraderMarkConfigWeightModel existModel = traderMarkConfigWeightMapper.selectByParentIdAndUserId(currentParentId, configWeightAddVo.getObjectUserId(), null);
        if (existModel != null) {
            throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
        }

        TraderMarkConfigWeightModel configWeightModel = new TraderMarkConfigWeightModel();
        configWeightModel.setUserId(configWeightAddVo.getObjectUserId());
        configWeightModel.setUserAccount(configWeightAddVo.getUserAccount());
        configWeightModel.setUserName(configWeightAddVo.getUserName());
        if (configModeModel.getModeId() == 1 || configModeModel.getModeId() == 3) {
            if (configWeightAddVo.getWeight() <= 0 || configWeightAddVo.getWeight() > 100) {
                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_WEIGHT);
            }
            configWeightModel.setWeight(new BigDecimal(configWeightAddVo.getWeight().toString()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            if (configWeightAddVo.getWeight() <= 0) {
                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_WEIGHT);
            }
            configWeightModel.setThreshold(configWeightAddVo.getWeight());
        }
        configWeightModel.setParentId(currentParentId);
        configWeightModel.setCreateId(configWeightAddVo.getUserId());
        configWeightModel.setUpdateId(configWeightAddVo.getUserId());
        Date now = new Date();
        configWeightModel.setCreateTime(now);
        configWeightModel.setUpdateTime(now);

        traderMarkConfigTransactionService.saveWeight(configWeightModel);
    }

    @Override
    public TraderMarkConfigWeightResponse getWeightDetail(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkConfigModeModel configModeModel = traderMarkConfigModeMapper.selectByParentId(parentId);
        if (configModeModel == null) {
            throw new RuntimeException("no set mode");
        }
        TraderMarkConfigWeightModel configWeightModel = traderMarkConfigWeightMapper.selectByPrimaryKey(id);
        if (configWeightModel == null || !configWeightModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not select");
        } else {
            TraderMarkConfigWeightResponse response = new TraderMarkConfigWeightResponse();
            response.setId(configWeightModel.getId());
            response.setUserId(configWeightModel.getUserId());
            response.setUserAccount(configWeightModel.getUserAccount());
            response.setUserName(configWeightModel.getUserName());
            if (configModeModel.getModeId() == 1 || configModeModel.getModeId() == 3) {
                response.setWeight(configWeightModel.getWeight());
            } else {
                response.setThreshold(configWeightModel.getThreshold());
            }
            return response;
        }
    }

    @Override
    public void updateWeight(TraderMarkConfigWeightUpdateVo configWeightUpdateVo) throws Exception {
        String currentParentId = userInfoService.getUserParentId(configWeightUpdateVo.getUserId());

        TraderMarkConfigWeightModel existModel = traderMarkConfigWeightMapper.selectByPrimaryKey(configWeightUpdateVo.getId());

        if (existModel == null) {
            throw new RuntimeException("id:" + configWeightUpdateVo.getId() + ", no data");
        }

        String needUpdateParentId = userInfoService.getUserParentId(configWeightUpdateVo.getObjectUserId());

        Boolean isEqual = currentParentId.equals(needUpdateParentId) && currentParentId.equals(existModel.getParentId());

        if (!isEqual) {
            throw new RuntimeException("id:" + configWeightUpdateVo.getId() + ", other company data not update");
        } else {
            TraderMarkConfigModeModel configModeModel = traderMarkConfigModeMapper.selectByParentId(currentParentId);
            if (configModeModel == null) {
                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_NO_MODE);
            }

            TraderMarkConfigWeightModel existConfigWeightModel = traderMarkConfigWeightMapper.selectByParentIdAndUserId(currentParentId, configWeightUpdateVo.getObjectUserId(), configWeightUpdateVo.getId());
            if (existConfigWeightModel != null) {
                throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
            }

            TraderMarkConfigWeightModel configWeightModel = new TraderMarkConfigWeightModel();
            configWeightModel.setId(configWeightUpdateVo.getId());
            configWeightModel.setUserId(configWeightUpdateVo.getObjectUserId());
            configWeightModel.setUserAccount(configWeightUpdateVo.getUserAccount());
            configWeightModel.setUserName(configWeightUpdateVo.getUserName());
            if (configModeModel.getModeId() == 1 || configModeModel.getModeId() == 3) {
                if (configWeightUpdateVo.getWeight() <= 0 || configWeightUpdateVo.getWeight() > 100) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_WEIGHT);
                }
                configWeightModel.setWeight(new BigDecimal(configWeightUpdateVo.getWeight().toString()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
            } else {
                if (configWeightUpdateVo.getWeight() <= 0) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_WEIGHT);
                }
                configWeightModel.setThreshold(configWeightUpdateVo.getWeight());
            }
            configWeightModel.setUpdateId(configWeightUpdateVo.getUserId());
            configWeightModel.setUpdateTime(new Date());

            traderMarkConfigTransactionService.updateWeight(configWeightModel);
        }
    }

    @Override
    public void deleteWeight(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);

        TraderMarkConfigWeightModel existModel = traderMarkConfigWeightMapper.selectByPrimaryKey(id);

        if (existModel == null || !existModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not delete");
        }

        traderMarkConfigTransactionService.deleteWeight(id);
    }
}
