package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.exception.CustomException;
import la.niub.abcapi.invest.seller.component.util.ExcelUtil;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.config.code.TraderMarkEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.ExcelTypeEnum;
import la.niub.abcapi.invest.seller.config.enums.LineTypeEnum;
import la.niub.abcapi.invest.seller.config.enums.QuarterDateEnum;
import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.model.bo.StockMonthReturnRateBO;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskBrokerResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskListResponse;
import la.niub.abcapi.invest.seller.model.response.client.KlineResponse;
import la.niub.abcapi.invest.seller.model.response.client.SecBasicInfoResponse;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveItemVo;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveVo;
import la.niub.abcapi.invest.seller.service.ITraderMarkTaskService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import la.niub.abcapi.invest.seller.service.TraderMarkTaskTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author : ppan
 * @description : 券商评分
 * @date : 2019-01-26 18:08
 */
@Service
public class TraderMarkTaskServiceImpl implements ITraderMarkTaskService {

    @Autowired
    private TraderMarkTaskPersonnelMapper taskPersonnelMapper;

    @Autowired
    private TraderMarkTaskMapper taskMapper;

    @Autowired
    private TraderMarkTaskDetailMapper taskDetailMapper;

    @Autowired
    private TraderMarkObjectiveDataBrokerMapper objectiveDataBrokerMapper;

    @Autowired
    private TraderMarkTaskTransactionService taskTransactionService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RecommendedStockMapper recommendedStockMapper;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private TraderMarkStatisticsDetailMapper statisticsDetailMapper;

    @Autowired
    private RoadShowMapper roadShowMapper;

    @Override
    public Map<Integer, List<TraderMarkTaskListResponse>> getTaskList(String userId) throws Exception {
        List<Long> taskIdList = taskPersonnelMapper.getTaskIdListByUserId(userId);
        if (taskIdList == null || taskIdList.isEmpty()) {
            return null;
        }
        List<TraderMarkTaskModel> list = taskMapper.selectByTaskIdList(taskIdList);

        Map<Integer, List<TraderMarkTaskListResponse>> result = new LinkedHashMap<>();
        list.forEach(taskModel -> {
            TraderMarkTaskListResponse item = new TraderMarkTaskListResponse();
            item.setId(taskModel.getId());
            item.setYear(taskModel.getYear());
            item.setQuarter(taskModel.getQuarter());
            BigDecimal progressRate;
            Integer status;
            if (taskModel.getStatus() == 0) {
                progressRate = taskDetailMapper.selectProgressRate(taskModel.getId());
                Integer count = taskDetailMapper.selectScoreNullCountByTaskIdAndUserId(taskModel.getId(), userId);
                status = count > 0 ? 0 : 1;
            } else {
                progressRate = new BigDecimal("1");
                status = 1;
            }
            item.setStatus(status);
            item.setProgressRate(progressRate);

            List<TraderMarkTaskListResponse> itemList;
            if (result.containsKey(taskModel.getYear())) {
                itemList = result.get(taskModel.getYear());
            } else {
                itemList = new ArrayList<>();
            }
            itemList.add(item);

            result.put(taskModel.getYear(), itemList);
        });
        return result;
    }

    @Override
    public List<TraderMarkTaskBrokerResponse> getTaskBroker(String userId, Long taskId, Integer status) throws Exception {
        List<TraderMarkTaskBrokerResponse> result = new ArrayList<>();

        List<Map<String, Object>> brokerStatusList = taskDetailMapper.getBrokerStatusByTaskIdAndUserId(taskId, userId);
        for (Map<String, Object> map : brokerStatusList) {
            String broker = map.get("broker").toString();
            Integer count = Integer.valueOf(map.get("count").toString());

            if ((status != null && status == 0 && count == 0) ||
                    (status != null && status == 1 && count > 0)) {
                continue;
            }

            BigDecimal totalScore = null;
            List<Map<String, Object>> dimensions = new ArrayList<>();
            List<Map<String, Object>> dimensionList = taskDetailMapper.getDimensionScoreByTaskIdAndUserIdAndBroker(taskId, userId, broker);

            BigDecimal maxScore = null;
            if (count == 0) {
                maxScore = new BigDecimal(dimensionList.get(0).get("score").toString());
                for (int i = 1; i < dimensionList.size(); i++) {
                    BigDecimal dimensionScore = new BigDecimal(dimensionList.get(i).get("score").toString());
                    if (maxScore.compareTo(dimensionScore) < 0) {
                        maxScore = dimensionScore;
                    }
                }
            }

            for (Map<String, Object> dimensionMap : dimensionList) {
                BigDecimal score = null;
                BigDecimal percentage = null;
                if (count == 0) {
                    score = new BigDecimal(dimensionMap.get("score").toString());
                    totalScore = totalScore == null ? new BigDecimal("0") : totalScore;
                    Integer calculateStatus = (Integer) dimensionMap.get("calculate_status");
                    if (calculateStatus == 0) {
                        totalScore = totalScore.add(score);
                    }
                    percentage = maxScore.compareTo(new BigDecimal("0")) == 0 ? new BigDecimal("1") : score.divide(maxScore, 2, BigDecimal.ROUND_HALF_UP);
                    score = score.setScale(1, BigDecimal.ROUND_HALF_UP);
                }
                Map<String, Object> dimensionScoreMap = new HashMap<>();
                dimensionScoreMap.put("dimension", dimensionMap.get("dimension").toString());
                dimensionScoreMap.put("score", score);
                dimensionScoreMap.put("percentage", percentage);
                dimensions.add(dimensionScoreMap);
            }

            TraderMarkObjectiveDataBrokerModel objectiveDataBrokerModel = objectiveDataBrokerMapper.selectByTaskIdAndBroker(taskId, broker);

            TraderMarkTaskBrokerResponse item = new TraderMarkTaskBrokerResponse();
            item.setBroker(broker);
            item.setReportCount(objectiveDataBrokerModel == null ? null : objectiveDataBrokerModel.getReportCount());
            item.setReadCount(objectiveDataBrokerModel == null ? null : objectiveDataBrokerModel.getReadCount());
            item.setRoadShowCount(objectiveDataBrokerModel == null ? null : objectiveDataBrokerModel.getRoadShowCount());
            item.setAttainDays(objectiveDataBrokerModel == null ? null : objectiveDataBrokerModel.getAttainDays().setScale(1, BigDecimal.ROUND_HALF_UP));
            item.setGoldStockRate(objectiveDataBrokerModel == null ? null : objectiveDataBrokerModel.getGoldRate());
            item.setDimensions(dimensions);
            item.setTotalScore(totalScore == null ? null : totalScore.setScale(1, BigDecimal.ROUND_HALF_UP));
            item.setStatus(count == 0 ? 1 : 0);

            result.add(item);
        }
        return result;
    }

    @Override
    public Map<String, Object> getIndustryInfo(String userId, Long taskId, String broker) throws Exception {
        List<Map<String, Object>> industryAnalystCountList = taskDetailMapper.getIndustryAnalystCount(userId, taskId, broker);
        List<Map<String, Object>> finishedIndustryMapList = taskDetailMapper.getFinishedIndustry(userId, taskId, broker);

        List<String> finishedIndustryList = new ArrayList<>();
        finishedIndustryMapList.forEach(map -> finishedIndustryList.add(map.get("industry").toString()));

        Map<String, Object> result = new HashMap<>();
        result.put("analystCount", industryAnalystCountList);
        result.put("finishedIndustry", finishedIndustryList);

        return result;
    }

    @Override
    public Map<String, Object> getTaskBrokerDetail(String userId, Long taskId, String broker, String industry) throws Exception {
        List<Map<String, Object>> list = taskDetailMapper.getAnalystObjectiveAndSubjectiveData(userId, taskId, broker, industry);
        list.forEach(map -> {
            String scoreValues = map.get("scoreValues").toString();
            JSONArray scoreValuesJson = JSON.parseArray("[" + scoreValues + "]");
            map.put("scoreValues", scoreValuesJson);
        });

        List<String> dimensions = taskDetailMapper.getDistinctDimensionList(userId, taskId, broker);
        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        Integer topScore;
        Integer currentPageTotalScore = null;
        if (taskModel.getModeId() == 1) {
            topScore = 10;
        } else if (taskModel.getModeId() == 2) {
            TraderMarkTaskPersonnelModel taskPersonnelModel = taskPersonnelMapper.getByUserIdAndTaskId(taskId, userId);
            topScore = taskPersonnelModel.getThreshold();
        } else {
            topScore = 100 - taskDetailMapper.getTotalScore(userId, taskId);
            // 当模式为“百分分配模式”,需要用到当前页已保存的分数之和，给前端动态计算剩余分数
            currentPageTotalScore = taskDetailMapper.getTotalScoreByBrokerAndIndustry(userId, taskId, broker, industry);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("modeId", taskModel.getModeId());
        result.put("dimensions", dimensions);
        result.put("topScore", topScore);
        result.put("currentPageTotalScore", currentPageTotalScore);
        result.put("analystScore", list);

        return result;
    }

    @Override
    public void saveScore(TraderMarkTaskSaveVo saveVo) throws Exception {
        Long taskId = saveVo.getTaskId();
        String userId = saveVo.getUserId();
        List<TraderMarkTaskSaveItemVo> scoreList = saveVo.getScoreList();
        // 判断评分任务是否已经全部打完分,如果全部打完,结束
        Integer count = taskDetailMapper.selectScoreNullCountByTaskIdAndUserId(taskId, userId);
        if (count == 0) {
            return;
        }

        // 检查分数是否都合规范
        checkScore(taskId, userId, scoreList, false);

        // 判断此次分数入库后是否评分任务是否结束，进行统计
        List<Long> scoreNulLIdList = taskDetailMapper.getScoreNullIdList(taskId);
        List<Long> currentIdList = new ArrayList<>();
        Map<Long, Integer> scoreMap = new HashMap<>();
        scoreList.forEach(scoreItemVo -> {
            currentIdList.add(scoreItemVo.getId());
            scoreMap.put(scoreItemVo.getId(), scoreItemVo.getScore());
        });

        List<TraderMarkStatisticsDetailModel> statisticsDetailModelList = null;
        if (currentIdList.containsAll(scoreNulLIdList)) {
            // 此次入库后评分任务已完成，置状态，且生成统计表数据
            statisticsDetailModelList = getStatisticsDetailList(taskId, scoreMap);
        }

        // 保存评分和统计数据入库
        taskTransactionService.saveScore(taskId, userId, scoreList, statisticsDetailModelList);
    }

    @Override
    public String getNextIncompleteBroker(Long taskId, String userId) throws Exception {
        List<Map<String, Object>> list = taskDetailMapper.getBrokerStatusByTaskIdAndUserId(taskId, userId);
        for (Map<String, Object> map : list) {
            if (map != null && map.get("count") != null && Integer.valueOf(map.get("count").toString()) > 0) {
                return map.get("broker").toString();
            }
        }

        return null;
    }

    @Override
    public Map<String, Object> getBrokerRate(String userId, String broker, Long taskId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        if (taskModel == null) {
            return null;
        }
        QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(taskModel.getQuarter());
        String maxPushMonth = taskModel.getYear() + "-" + quarterDateEnum.getEndDate();
        maxPushMonth = maxPushMonth.substring(0, 7);
        List<String> pushMonthList = recommendedStockMapper.getSevenPushMonthByParentIdAndBroker(parentId, broker, maxPushMonth);
        if (pushMonthList == null || pushMonthList.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> list = recommendedStockMapper.getByParentIdAndBrokerAndPushMonthList(parentId, broker, pushMonthList);
        Map<String, BigDecimal> monthRateMap = new LinkedHashMap<>();
        list.forEach(map -> {
            BigDecimal goldRate = map.get("gold_rate") == null ? null : (BigDecimal) map.get("gold_rate");
            String monthPush = map.get("push_month").toString();
            if (goldRate == null) {
                Response<Map<Long, StockMonthReturnRateBO>> response = apiPlatFormClient.getMonthReturnRate(map.get("sec_uni_code").toString(), monthPush);
                if (response == null || response.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票月盈利失败,返回值：" + JSON.toJSONString(response));
                } else if (response.getData() == null || response.getData().isEmpty() || response.getData().get(Long.valueOf(map.get("sec_uni_code").toString())) == null) {
                    throw new RuntimeException("从平台获取股票月盈利成功,但是该secUniCode:" + map.get("sec_uni_code").toString() + "无数据,返回值：" + JSON.toJSONString(response));
                }
                StockMonthReturnRateBO stockMonthReturnRateBO = response.getData().get(Long.valueOf(map.get("sec_uni_code").toString()));
                goldRate = stockMonthReturnRateBO.getGoldRate();
            }

            if (!monthRateMap.containsKey(monthPush)) {
                monthRateMap.put(monthPush, goldRate);
            } else {
                monthRateMap.put(monthPush, monthRateMap.get(monthPush).add(goldRate));
            }
        });

        BigDecimal recentRate = monthRateMap.get(pushMonthList.get(0));

        Map<String, Object> result = new HashMap<>();
        result.put("recentRate", recentRate);
        result.put("monthRate", monthRateMap);
        return result;
    }

    @Override
    public Map<String, Object> getBrokerTotalScoreChart(String userId, String broker) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        List<Map<String, Object>> list = statisticsDetailMapper.getBrokerSevenTotalScoreByBroker(parentId, broker);
        Collections.reverse(list);

        Object recentTotalScore = list.isEmpty() ? null : list.get(list.size() - 1).get("totalScore");

        Map<String, Object> result = new HashMap<>();
        result.put("totalScoreList", list);
        result.put("recentTotalScore", recentTotalScore);
        return result;
    }

    @Override
    public List<Map<String, Object>> getBrokerRoadShow(String userId, Long taskId, String broker, String industry, String analyst) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(taskModel.getQuarter());
        if (quarterDateEnum == null) {
            throw new RuntimeException("task quarter error");
        }
        String startTimeStr = taskModel.getYear() + "-" + quarterDateEnum.getStartDate();
        String endTimeStr = taskModel.getYear() + "-" + quarterDateEnum.getEndDate();
        Date startTime = TimeUtil.parseDateStr(startTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
        Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

        return roadShowMapper.getCompanyRoadShowCountBySellerAndDate(parentId, broker, industry, analyst, startTime, endTime);
    }

    @Override
    public Map<String, Object> getStockMarket(String userId, Long taskId, String stockCode, String stockName, String broker, String industry, String analyst) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        if (taskModel == null) {
            throw  new RuntimeException("taskId:" + taskId + ",no data");
        }

        QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(taskModel.getQuarter());
        if (quarterDateEnum == null) {
            throw new RuntimeException("task quarter error");
        }
        String startTimeStr = taskModel.getYear() + "-" + quarterDateEnum.getStartDate();
        String endTimeStr = taskModel.getYear() + "-" + quarterDateEnum.getEndDate();
        Date startTime = TimeUtil.parseDateStr(startTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
        Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
        startTimeStr = startTimeStr.substring(0, 10);

        // TODO 调用平台服务获取一段时间内的股票收盘价
        Map<String, BigDecimal> stockMarketMap = new LinkedHashMap<>();

        Response<List<SecBasicInfoResponse>> stockResponse = apiPlatFormClient.getStockByNameOrCode(stockName);
        if (stockResponse == null || stockResponse.getCode() != 200) {
            throw new RuntimeException("调用平台接口获取股票信息失败: stockName:" + stockName + ", 返回值:" + JSON.toJSONString(stockResponse));
        }

        if (stockResponse.getData() == null || stockResponse.getData().isEmpty() || !stockCode.equals(stockResponse.getData().get(0).getAbc_code())) {
            throw new RuntimeException("stockName：" + stockCode + ", 调用平台接口获取不到stockCode，或者查询出的stockCode与传递的stockCode不一致, 返回值:" + JSON.toJSONString(stockResponse));
        }

        Response<Map<String, KlineResponse>> klineResponse = apiPlatFormClient.getKline(startTimeStr, stockCode, LineTypeEnum.DAY.getTag());
        if (klineResponse == null || klineResponse.getCode() != 200) {
            throw new RuntimeException("调用平台接口获取k线失败: stockCode:" + stockCode + ", 返回值:" + JSON.toJSONString(klineResponse));
        }

        if (klineResponse.getData() == null || klineResponse.getData().isEmpty()) {
            throw new RuntimeException("调用平台接口获取k线无数据: stockCode:" + stockCode + ", 返回值:" + JSON.toJSONString(klineResponse));
        }

        Map<String, KlineResponse> klineResponseMap = klineResponse.getData();
        klineResponseMap.forEach((key, value) -> {
            Date keyDate = TimeUtil.parseDateStr(key, "yyyy-MM-dd HH:mm:ss");
            if (keyDate != null && endTime != null && keyDate.getTime() > endTime.getTime()) {
                return;
            }

            stockMarketMap.put(key.substring(0, 10), value.getClose_price());
        });

        List<String> roadShowTimeList = roadShowMapper.getRoadShowTimeBySellerAndDate(parentId, startTime, endTime, broker, industry, analyst, stockCode, stockName);

        Map<String, Object> result = new HashMap<>();
        result.put("stockMarket", stockMarketMap);
        result.put("roadShowTime", roadShowTimeList);
        return result;
    }

    @Override
    public void download(ByteArrayOutputStream bos, String userId, Long taskId) throws Exception {
        List<TraderMarkTaskDetailModel> list = taskDetailMapper.selectByUserIdAndTaskId(userId, taskId);
        List<String> dimensionList = taskDetailMapper.getDistinctDimensionList(userId, taskId, null);
        dimensionList.add(0, "研究员");
        dimensionList.add(0, "行业");

        Map<String, List<TraderMarkTaskDetailModel>> analystMap = new LinkedHashMap<>();
        list.forEach(taskDetailModel -> {
            String key = taskDetailModel.getBroker() + "-" + taskDetailModel.getIndustry() + "-" + taskDetailModel.getAnalyst();

            List<TraderMarkTaskDetailModel> analystList;
            if (analystMap.containsKey(key)) {
                analystList = analystMap.get(key);
            } else {
                analystList = new ArrayList<>();
            }

            analystList.add(taskDetailModel);
            analystMap.put(key, analystList);
        });

        Map<String, List<List<String>>> dataMap = new HashMap<>();
        String currentIndustry = "";
        for (Map.Entry<String, List<TraderMarkTaskDetailModel>> entry : analystMap.entrySet()) {
            List<String> keyList = Arrays.asList(entry.getKey().split("-"));
            String broker = keyList.get(0);
            String industry = keyList.get(1);

            List<List<String>> sheetDataList;
            if (dataMap.containsKey(broker)) {
                sheetDataList = dataMap.get(broker);
            } else {
                sheetDataList = new ArrayList<>();
                currentIndustry = "";
            }

            List<String> rowList = Arrays.asList(new String[dimensionList.size()]);
            if (StringUtil.isEmpty(currentIndustry) || !currentIndustry.equals(industry)) {
                rowList.set(0, industry);
                currentIndustry = industry;
            } else {
                rowList.set(0, "top");
            }
            rowList.set(1, keyList.get(2));

            entry.getValue().forEach(taskDetailModel -> {
                Integer index = dimensionList.indexOf(taskDetailModel.getDimension());
                rowList.set(index, taskDetailModel.getScore() == null ? null : taskDetailModel.getScore() + "");
            });

            sheetDataList.add(rowList);
            dataMap.put(broker, sheetDataList);
        }
        ExcelUtil.exportTraderMarkTemplate(bos, dimensionList, dataMap, "券商评分");
    }

    @Override
    public void upload(MultipartFile file, String userId, Long taskId) throws Exception {
        Integer count = taskDetailMapper.selectScoreNullCountByTaskIdAndUserId(taskId, userId);
        // 打分已完成,无法再次打分
        if (count == 0) {
            throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_SCORE_FINISH);
        }

        String excelType;
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(ExcelTypeEnum.XLSX.getType())) {
            excelType = ExcelTypeEnum.XLSX.getType();
        } else if (fileName.endsWith(ExcelTypeEnum.XLS.getType())) {
            excelType = ExcelTypeEnum.XLS.getType();
        } else {
            throw new RuntimeException("excel type error");
        }
        Map<String, List<List<String>>> sheetDataList = ExcelUtil.readTraderMarkTaskUplaodExcel(file.getInputStream(), excelType, 2);

        List<TraderMarkTaskDetailModel> taskDetailModelList = taskDetailMapper.selectByUserIdAndTaskId(userId, taskId);
        Set<String> brokerSet = new HashSet<>();
        Map<String, Long> analystMap = new HashMap<>();
        taskDetailModelList.forEach(taskDetailModel -> {
            brokerSet.add(taskDetailModel.getBroker());
            String key = taskDetailModel.getBroker() + "-" + taskDetailModel.getIndustry() + "-" + taskDetailModel.getAnalyst() + "-" + taskDetailModel.getDimension();
            analystMap.put(key, taskDetailModel.getId());
        });

        List<TraderMarkTaskSaveItemVo> scoreList = new ArrayList<>();
        Map<Long, Integer> scoreMap = new HashMap<>();
        Set<Long> currentTaskDetailIdList = new HashSet<>();
        for (Map.Entry<String, List<List<String>>> entry : sheetDataList.entrySet()) {
            String broker = entry.getKey().trim();
            if (!brokerSet.contains(broker)) {
                continue;
            }

            String currentIndustry = "";

            List<List<String>> value = entry.getValue();
            List<String> keyList = value.get(0);

            for (int i = 1; i < value.size(); i++) {
                List<String> dataList = value.get(i);
                for (int j = 2; j < keyList.size(); j++) {
                    String industry = dataList.get(0);
                    if (StringUtil.isEmpty(currentIndustry) || (!StringUtil.isEmpty(industry) && !currentIndustry.equals(industry))) {
                        currentIndustry = industry;
                    }
                    String str = broker + "-" + currentIndustry + "-" + dataList.get(1) + "-" + keyList.get(j);
                    if (!analystMap.containsKey(str) || StringUtil.isEmpty(dataList.get(j))) {
                        continue;
                    }

                    scoreMap.put(analystMap.get(str), Integer.valueOf(dataList.get(j)));
                    currentTaskDetailIdList.add(analystMap.get(str));

                    TraderMarkTaskSaveItemVo saveItemVo = new TraderMarkTaskSaveItemVo();
                    saveItemVo.setId(analystMap.get(str));
                    saveItemVo.setScore(Integer.valueOf(dataList.get(j)));

                    scoreList.add(saveItemVo);
                }
            }
        }

        checkScore(taskId, userId, scoreList, true);

        List<Long> scoreNulLIdList = taskDetailMapper.getScoreNullIdList(taskId);

        // 判断上传的打分是否全部完成
        List<TraderMarkStatisticsDetailModel> statisticsDetailModelList = null;
        if (currentTaskDetailIdList.containsAll(scoreNulLIdList)) {
            // 此次入库后评分任务已完成，置状态，且生成统计表数据
            statisticsDetailModelList = getStatisticsDetailList(taskId, scoreMap);
        }

        taskTransactionService.saveScore(taskId, userId, scoreList, statisticsDetailModelList);
    }

    private List<TraderMarkStatisticsDetailModel> getStatisticsDetailList(Long taskId, Map<Long, Integer> scoreMap) throws Exception {
        List<TraderMarkTaskDetailModel> detailModelList = taskDetailMapper.getByTaskId(taskId);

        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        List<TraderMarkTaskPersonnelModel> taskPersonnelModelList = taskPersonnelMapper.getByTaskId(taskId);
        Map<String, BigDecimal> weightMap = new HashMap<>();
        taskPersonnelModelList.forEach(item -> weightMap.put(item.getUserId(), item.getWeight()));

        Map<String, BigDecimal> totalScoreMap = new LinkedHashMap<>();
        detailModelList.forEach(taskDetailModel -> {
            BigDecimal weight;
            if (taskModel.getModeId() == 1 || taskModel.getModeId() == 3) {
                weight = weightMap.get(taskDetailModel.getUserId());
            } else {
                weight = new BigDecimal("1");
            }

            String key = taskDetailModel.getTaskId() + "-" + taskDetailModel.getBroker() + "-" +
                    taskDetailModel.getIndustry() + "-" + taskDetailModel.getAnalyst() + "-" +
                    taskDetailModel.getDimension() + "-" + taskDetailModel.getCalculateStatus();
            Integer value;
            if (scoreMap.containsKey(taskDetailModel.getId())) {
                value = scoreMap.get(taskDetailModel.getId());
            } else {
                value = taskDetailModel.getScore();
            }

            if (!totalScoreMap.containsKey(key)) {
                totalScoreMap.put(key, weight.multiply(new BigDecimal(value.toString())));
            } else {
                totalScoreMap.put(key, totalScoreMap.get(key).add(weight.multiply(new BigDecimal(value.toString()))));
            }
        });

        List<TraderMarkStatisticsDetailModel> statisticsDetailModelList = new ArrayList<>();
        Date now = new Date();
        totalScoreMap.forEach((key, value) -> {
            List<String> keyList = Arrays.asList(key.split("-"));
            TraderMarkStatisticsDetailModel statisticsDetailModel = new TraderMarkStatisticsDetailModel();
            statisticsDetailModel.setTaskId(Long.valueOf(keyList.get(0)));
            statisticsDetailModel.setBroker(keyList.get(1));
            statisticsDetailModel.setIndustry(keyList.get(2));
            statisticsDetailModel.setAnalyst(keyList.get(3));
            statisticsDetailModel.setDimension(keyList.get(4));
            statisticsDetailModel.setScore(value.divide(new BigDecimal(taskPersonnelModelList.size() + ""), 2, BigDecimal.ROUND_HALF_UP));
            statisticsDetailModel.setCalculateStatus(Integer.valueOf(keyList.get(5)));
            statisticsDetailModel.setCreateTime(now);
            statisticsDetailModel.setUpdateTime(now);

            statisticsDetailModelList.add(statisticsDetailModel);
        });

        return statisticsDetailModelList;
    }

    private void checkScore(Long taskId, String userId, List<TraderMarkTaskSaveItemVo> scoreList, Boolean isUploadExcel) throws Exception {
        TraderMarkTaskModel taskModel = taskMapper.selectByPrimaryKey(taskId);
        if (taskModel == null) {
            throw new RuntimeException("taskId：" + taskId + ", no data");
        }

        if (taskModel.getModeId() == 1) {
            for (TraderMarkTaskSaveItemVo saveItemVo : scoreList) {
                if (saveItemVo.getScore() > 10 || saveItemVo.getScore() < 0) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_SCORE);
                }
            }
        } else if (taskModel.getModeId() == 2) {
            TraderMarkTaskPersonnelModel taskPersonnelModel = taskPersonnelMapper.getByUserIdAndTaskId(taskId, userId);
            for (TraderMarkTaskSaveItemVo saveItemVo : scoreList) {
                if (saveItemVo.getScore() > taskPersonnelModel.getThreshold() || saveItemVo.getScore() < 0) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_SCORE);
                }
            }
        } else {
            Integer topScore = isUploadExcel ? 100 : 100 - taskDetailMapper.getTotalScore(userId, taskId);
            Integer totalScore = 0;
            for (TraderMarkTaskSaveItemVo saveItemVo : scoreList) {
                if (saveItemVo.getScore() < 0) {
                    throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_SCORE);
                } else {
                    totalScore += saveItemVo.getScore();
                }
            }
            if (totalScore > topScore) {
                throw new CustomException(TraderMarkEnumCodeConfig.ERROR_TRADER_MARK_SCORE);
            }
        }
    }
}
