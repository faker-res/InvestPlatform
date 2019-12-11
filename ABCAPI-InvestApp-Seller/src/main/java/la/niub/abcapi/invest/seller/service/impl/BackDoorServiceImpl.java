package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.client.IInvestReportClient;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.config.enums.QuarterDateEnum;
import la.niub.abcapi.invest.seller.config.enums.ReadArticleTypeEnum;
import la.niub.abcapi.invest.seller.constant.ReportSearchConstant;
import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.model.bo.StockMonthReturnRateBO;
import la.niub.abcapi.invest.seller.model.request.client.AnalystReportNumRequest;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.IBackDoorService;
import la.niub.abcapi.invest.seller.service.ITraderMarkJobTransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : ppan
 * @description : 券商评分定时任务
 * @date : 2019-01-25 14:23
 */
@Service
public class BackDoorServiceImpl implements IBackDoorService {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private TraderMarkConfigModeMapper configModeMapper;

    @Autowired
    private TraderMarkConfigDimensionMapper configDimensionMapper;

    @Autowired
    private TraderMarkConfigObjectMapper configObjectMapper;

    @Autowired
    private TraderMarkConfigWeightMapper configWeightMapper;

    @Autowired
    private ITraderMarkJobTransactionService traderMarkJobTransactionService;

    @Autowired
    private RoadShowMapper roadShowMapper;

    @Autowired
    private RecommendedStockMapper recommendedStockMapper;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private ReadLogMapper readLogMapper;

    @Autowired
    private TraderMarkAnalystTargetMapper traderMarkAnalystTargetMapper;

    @Autowired
    private IInvestReportClient investReportClient;

    @Override
    public void testTraderMarkJob(Boolean test) {
        logger.info("start produce trader mark job");
        try {
            logger.info("start get get effective parentId");
            List<String> parentIdList = null;
            for (int i = 0; i < 3; i++) {
                try {
                    parentIdList = getEffectiveParentId();
                    break;
                } catch (Exception e1) {
                    if (i == 2) {
                        throw new RuntimeException(e1);
                    }
                }
            }

            if (parentIdList == null || parentIdList.isEmpty()) {
                logger.warn("no effective parentId");
                return;
            }

            logger.info("end get effective parentId, parentIdList:{}", parentIdList);

            Date now = new Date();
            List<TraderMarkTaskModel> taskModelList = new ArrayList<>();
            Map<String, List<TraderMarkTaskPersonnelModel>> taskPersonnelMap = new HashMap<>();
            Map<String, List<TraderMarkTaskDetailModel>> taskDetailMap = new HashMap<>();
            Map<String, List<TraderMarkObjectiveDataBrokerModel>> objectiveDataBrokerMap = new HashMap<>();
            Map<String, List<TraderMarkObjectiveDataDetailModel>> objectiveDataDetailMap = new HashMap<>();

            logger.info("start set data");
            for (int i = 0; i < parentIdList.size(); i++) {
                String parentId = parentIdList.get(i);
                logger.info("current parentId:{}", parentId);
                TraderMarkConfigModeModel configModeModel = configModeMapper.selectByParentId(parentId);
                // 查询parentId以及对应模式的权重或阈值是否都设置
                Integer count = configWeightMapper.selectCountByParentIdAndModeId(parentId, configModeModel.getModeId());
                if (count != 0) {
                    // 大于O表示当前模式对应的权重或阈值未全部设置完成，跳过
                    logger.info("current parentId:{}, weight or threshold not set", parentId);
                    continue;
                }

                logger.info("set task data");
                // 设置TraderMarkTaskModel对象
                TraderMarkTaskModel taskModel = new TraderMarkTaskModel();
                taskModel.setParentId(parentId);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                Integer quarter;
                switch (month) {
                    case 1:
                    case 2:
                    case 3:
                        year = year - 1;
                        quarter = 4;
                        break;
                    case 4:
                    case 5:
                    case 6:
                        quarter = 1;
                        break;
                    case 7:
                    case 8:
                    case 9:
                        quarter = 2;
                        break;
                    case 10:
                    case 11:
                    case 12:
                        quarter = 3;
                        break;
                    default:
                        throw new RuntimeException("根据时间获取季度失败,时间：" + TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                }
                taskModel.setYear(year);
                taskModel.setQuarter(quarter);
                taskModel.setModeId(configModeModel.getModeId());
                taskModel.setStatus(0);
                taskModel.setCreateTime(now);
                taskModel.setUpdateTime(now);

                taskModelList.add(taskModel);


                // 设置TraderMarkTaskPersonnelModel对象
                logger.info("set task personnel data");
                List<TraderMarkTaskPersonnelModel> taskPersonnelModelList = new ArrayList<>();
                List<TraderMarkConfigWeightModel> configWeightModelList = configWeightMapper.selectByParentId(parentId);
                for (TraderMarkConfigWeightModel configWeightModel : configWeightModelList) {
                    TraderMarkTaskPersonnelModel taskPersonnelModel = new TraderMarkTaskPersonnelModel();
                    taskPersonnelModel.setUserId(configWeightModel.getUserId());
                    if (configModeModel.getModeId() == 1 || configModeModel.getModeId() == 3) {
                        taskPersonnelModel.setWeight(configWeightModel.getWeight());
                    } else {
                        taskPersonnelModel.setThreshold(configWeightModel.getThreshold());
                    }
                    taskPersonnelModel.setCreateTime(now);
                    taskPersonnelModel.setUpdateTime(now);

                    taskPersonnelModelList.add(taskPersonnelModel);
                }

                // 设置TraderMarkTaskDetailModel和客观数据
                List<TraderMarkTaskDetailModel> taskDetailModelList = new ArrayList<>();
                List<TraderMarkObjectiveDataDetailModel> objectiveDataDetailModelList = new ArrayList<>();
                List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerModelList = new ArrayList<>();
                Map<String, String> brokerObjectiveDataMap = new HashMap<>();

                logger.info("start set task detail and objective data");
                List<TraderMarkConfigObjectModel> configObjectModelList = configObjectMapper.selectByParentIdAndBrokerList(parentId, null);
                List<TraderMarkConfigDimensionModel> configDimensionModelList = configDimensionMapper.selectByParentId(parentId);

                for (TraderMarkConfigObjectModel configObjectModel : configObjectModelList) {
                    for (TraderMarkConfigDimensionModel configDimensionModel : configDimensionModelList) {
                        for (TraderMarkConfigWeightModel configWeightModel : configWeightModelList) {
                            TraderMarkTaskDetailModel taskDetailModel = new TraderMarkTaskDetailModel();
                            taskDetailModel.setUserId(configWeightModel.getUserId());
                            taskDetailModel.setBroker(configObjectModel.getBroker());
                            taskDetailModel.setIndustry(configObjectModel.getIndustry());
                            taskDetailModel.setAnalyst(configObjectModel.getAnalyst());
                            taskDetailModel.setDimension(configDimensionModel.getDimension());
                            taskDetailModel.setCalculateStatus(configDimensionModel.getCalculateStatus());
                            taskDetailModel.setCreateTime(now);
                            taskDetailModel.setUpdateTime(now);

                            taskDetailModelList.add(taskDetailModel);
                        }
                    }

                    // 设置TraderMarkObjectiveDataDetailModel对象
                    // 获取研报量接口
                    Integer reportCount = getReportCount(test, parentId, configObjectModel.getBroker(), configObjectModel.getAnalyst(), year, quarter);
                    // 获取阅读量接口
                    Integer readingCount = getReadingCount(test, parentId, configObjectModel.getAnalyst(), year, quarter);
                    // 获取平均完成天数接口
                    BigDecimal attainDays = getAttainDays(test, parentId, configObjectModel.getBroker(), configObjectModel.getAnalyst(), year, quarter);
                    // 获取达成率
                    BigDecimal attainProbability = getAttainProbability(test, parentId, configObjectModel.getBroker(), configObjectModel.getAnalyst(), year, quarter);
                    // 获取路演数量
                    Integer roadShowCount = getRoadShowCount(parentId, configObjectModel.getBroker(), configObjectModel.getIndustry(), configObjectModel.getAnalyst(), year, quarter);

                    TraderMarkObjectiveDataDetailModel objectiveDataDetailModel = new TraderMarkObjectiveDataDetailModel();
                    objectiveDataDetailModel.setBroker(configObjectModel.getBroker());
                    objectiveDataDetailModel.setIndustry(configObjectModel.getIndustry());
                    objectiveDataDetailModel.setAnalyst(configObjectModel.getAnalyst());
                    objectiveDataDetailModel.setReportCount(reportCount);
                    objectiveDataDetailModel.setReadCount(readingCount);
                    objectiveDataDetailModel.setAttainDays(attainDays);
                    objectiveDataDetailModel.setAttainProbability(attainProbability);
                    objectiveDataDetailModel.setRoadShowCount(roadShowCount);
                    objectiveDataDetailModel.setCreateTime(now);
                    objectiveDataDetailModel.setUpdateTime(now);

                    objectiveDataDetailModelList.add(objectiveDataDetailModel);

                    // 设置TraderMarkObjectiveDataBrokerModel对象，统计同一个券商的值
                    if (!brokerObjectiveDataMap.containsKey(configObjectModel.getBroker())) {
                        BigDecimal goldStockRate = getGoldStockRate(parentId, configObjectModel.getBroker(), year, quarter);
                        StringBuffer sb = new StringBuffer();
                        sb.append(reportCount).append("-")
                                .append(readingCount).append("-")
                                .append(attainDays).append("-")
                                .append(attainProbability).append("-")
                                .append(roadShowCount).append("-")
                                .append(goldStockRate == null ? "null" : goldStockRate).append("-")
                                .append("1");
                        brokerObjectiveDataMap.put(configObjectModel.getBroker(), sb.toString());
                    } else {
                        String value = brokerObjectiveDataMap.get(configObjectModel.getBroker());
                        List<String> valueList = Arrays.asList(value.split("-"));
                        StringBuffer sb = new StringBuffer();
                        sb.append(Integer.valueOf(valueList.get(0)) + reportCount).append("-")
                                .append(Integer.valueOf(valueList.get(1)) + readingCount).append("-")
                                .append(new BigDecimal(valueList.get(2)).add(attainDays)).append("-")
                                .append(new BigDecimal(valueList.get(3)).add(attainProbability)).append("-")
                                .append(Integer.valueOf(valueList.get(4)) + roadShowCount).append("-")
                                .append(valueList.get(5)).append("-")
                                .append(Integer.valueOf(valueList.get(6)) + 1);
                        brokerObjectiveDataMap.put(configObjectModel.getBroker(), sb.toString());
                    }
                }
                taskPersonnelMap.put(parentId, taskPersonnelModelList);

                taskDetailMap.put(parentId, taskDetailModelList);
                objectiveDataDetailMap.put(parentId, objectiveDataDetailModelList);

                brokerObjectiveDataMap.forEach((key, value) -> {
                    List<String> valueList = Arrays.asList(value.split("-"));
                    TraderMarkObjectiveDataBrokerModel objectiveDataBrokerModel = new TraderMarkObjectiveDataBrokerModel();
                    objectiveDataBrokerModel.setBroker(key);
                    objectiveDataBrokerModel.setReportCount(Integer.valueOf(valueList.get(0)));
                    objectiveDataBrokerModel.setReadCount(Integer.valueOf(valueList.get(1)));
                    objectiveDataBrokerModel.setAttainDays(new BigDecimal(valueList.get(2)).divide(new BigDecimal(valueList.get(6)), 2, BigDecimal.ROUND_HALF_UP));
                    objectiveDataBrokerModel.setAttainProbability(new BigDecimal(valueList.get(3)).divide(new BigDecimal(valueList.get(6)), 2, BigDecimal.ROUND_HALF_UP));
                    objectiveDataBrokerModel.setRoadShowCount(Integer.valueOf(valueList.get(4)));
                    objectiveDataBrokerModel.setGoldRate("null".equals(valueList.get(5)) ? null : new BigDecimal(valueList.get(5)).setScale(4, BigDecimal.ROUND_HALF_UP));
                    objectiveDataBrokerModel.setCreateTime(now);
                    objectiveDataBrokerModel.setUpdateTime(now);

                    objectiveDataBrokerModelList.add(objectiveDataBrokerModel);
                });
                objectiveDataBrokerMap.put(parentId, objectiveDataBrokerModelList);
            }
            logger.info("end set task detail and objective data");

            logger.info("start save data");
            try {
                traderMarkJobTransactionService.save(taskModelList, taskPersonnelMap, taskDetailMap, objectiveDataDetailMap, objectiveDataBrokerMap);
            } catch (Exception e) {
                logger.error("save data error, error:{}, try again", e);
                traderMarkJobTransactionService.save(taskModelList, taskPersonnelMap, taskDetailMap, objectiveDataDetailMap, objectiveDataBrokerMap);
            }
            logger.info("end save data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("定时任务生成评价任务失败, 时间：" + TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss") + ", 错误信息：" + e);
            throw new RuntimeException("定时任务生成评价任务失败, 时间：" + TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss") + ", 错误信息：" + e);
        }
    }

    private List<String> getEffectiveParentId() throws Exception {
        List<String> configModeParentIdList = configModeMapper.selectDistinctParentId();
        if (configModeParentIdList == null || configModeParentIdList.isEmpty()) {
            logger.info("no config mode data");
            return null;
        }

        List<String> configDimensionParentIdList = configDimensionMapper.selectDistinctParentId();
        if (configDimensionParentIdList == null || configDimensionParentIdList.isEmpty()) {
            logger.info("no config dimension data");
            return null;
        }

        List<String> configObjectParentIdList = configObjectMapper.selectDistinctParentId();
        if (configObjectParentIdList == null || configObjectParentIdList.isEmpty()) {
            logger.info("no config object data");
            return null;
        }

        List<String> configWeightParentIdList = configModeMapper.selectDistinctParentId();
        if (configWeightParentIdList == null || configModeParentIdList.isEmpty()) {
            logger.info("no config mode data");
            return null;
        }

        configModeParentIdList.retainAll(configDimensionParentIdList);
        configModeParentIdList.retainAll(configObjectParentIdList);
        configModeParentIdList.retainAll(configWeightParentIdList);
        if (configDimensionParentIdList.isEmpty()) {
            logger.info("no integral configuration data");
            return null;
        }

        return configModeParentIdList;
    }

    private Integer getReportCount(Boolean test, String parentId, String broker, String analyst, Integer year, Integer quarter) {
        // 从个人研报solr获取，analyst的名称需要去除末尾的数字，因为券商评分的同券商同行业不允许出现重名，如果重名在后面加数字
        try {
            if (test) {
                return new Random().nextInt(100);
            }
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            if (quarterDateEnum == null) {
                throw new RuntimeException("quarter error");
            }
            String startTime = year + "-" + quarterDateEnum.getStartDate();
            String endTime = year + "-" + quarterDateEnum.getEndDate();
            startTime = startTime.substring(0, 10);
            endTime = endTime.substring(0, 10);

            analyst = analyst.replaceAll("\\d+$", "");

            AnalystReportNumRequest analystReportNumRequest = new AnalystReportNumRequest();
            analystReportNumRequest.setAnalystName(analyst);
            analystReportNumRequest.setEndDate(endTime);
            analystReportNumRequest.setParentId(parentId);
            analystReportNumRequest.setPublishName(broker);
            analystReportNumRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
            analystReportNumRequest.setStartDate(startTime);
            Response<Integer> response = investReportClient.relatedAnalystReportNum(analystReportNumRequest);
            if (response == null || response.getCode() != 200) {
                throw new RuntimeException("get analyst report count from service-invest-report error, param:" + JSON.toJSONString(analystReportNumRequest) + ",response:" + JSON.toJSONString(response));
            }

            return response.getData() == null ? 0 : response.getData();
        } catch (Exception e) {
            logger.error("券商评分定时任务,获取研报数量失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return 0;
        }

    }

    private Integer getReadingCount(Boolean test, String parentId, String analyst, Integer year, Integer quarter) {
        // analyst的名称需要去除末尾的数字，因为券商评分的同券商同行业不允许出现重名，如果重名在后面加数字
        try {
            if (test) {
                return new Random().nextInt(1000);
            }
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            String startTimeStr = year + "-" + quarterDateEnum.getStartDate();
            String endTimeStr = year + "-" + quarterDateEnum.getEndDate();
            Date startTime = TimeUtil.parseDateStr(startTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
            Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

            analyst = analyst.replaceAll("\\d+$", "");

            List<String> articleTypeList = new ArrayList<>();
            articleTypeList.add(ReadArticleTypeEnum.MAIL_REPORT.getType());
            articleTypeList.add(ReadArticleTypeEnum.UPLOAD_REPORT.getType());

            return readLogMapper.selectArticleAuthorReadCount(parentId, analyst, articleTypeList, startTime, endTime);
        } catch (Exception e) {
            logger.error("券商评分定时任务,获取研报阅读量失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return 0;
        }
    }

    private BigDecimal getAttainDays(Boolean test, String parentId, String broker, String analyst, Integer year, Integer quarter) {
        // analyst的名称需要去除末尾的数字，因为券商评分的同券商同行业不允许出现重名，如果重名在后面加数字
        try {
            if (test) {
                return new BigDecimal(new Random().nextInt(250) + "");
            }
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            String endTimeStr = year + "-" + quarterDateEnum.getEndDate();
            Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

            analyst = analyst.replaceAll("\\d+$", "");

            return traderMarkAnalystTargetMapper.getAnalystAttainDays(parentId, broker, analyst, endTime);
        } catch (Exception e) {
            logger.error("券商评分定时任务,获取研究员达成天数失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return new BigDecimal("0");
        }
    }

    private BigDecimal getAttainProbability(Boolean test, String parentId, String broker, String analyst, Integer year, Integer quarter) {
        // analyst的名称需要去除末尾的数字，因为券商评分的同券商同行业不允许出现重名，如果重名在后面加数字
        try {
            if (test) {
                return new BigDecimal(Math.random() + "");
            }
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            String endTimeStr = year + "-" + quarterDateEnum.getEndDate();
            Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

            analyst = analyst.replaceAll("\\d+$", "");

            return traderMarkAnalystTargetMapper.getAnalystAttainProbability(parentId, broker, analyst, endTime);
        } catch (Exception e) {
            logger.error("券商评分定时任务,获取研究员达成率失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return new BigDecimal("0");
        }
    }

    private Integer getRoadShowCount(String parentId, String broker, String industry, String analyst, Integer year, Integer quarter) {
        try {
            // analyst的名称需要去除末尾的数字，因为券商评分的同券商同行业不允许出现重名，如果重名在后面加数字
            analyst = analyst.replaceAll("\\d+$", "");
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            if (quarterDateEnum == null) {
                throw new RuntimeException("quarter error");
            }
            String startTimeStr = year + "-" + quarterDateEnum.getStartDate();
            String endTimeStr = year + "-" + quarterDateEnum.getEndDate();
            Date startTime = TimeUtil.parseDateStr(startTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
            Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

            return roadShowMapper.getRoadShowCountBySellerAndDate(parentId, startTime, endTime, broker, industry, analyst);
        }catch (Exception e) {
            logger.error("券商评分定时任务,获取路演数量失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return 0;
        }
    }

    private BigDecimal getGoldStockRate(String parentId, String broker, int year, int quarter) {
        try {
            QuarterDateEnum quarterDateEnum = QuarterDateEnum.getQuarterDateEnumByQuarter(quarter);
            if (quarterDateEnum == null) {
                throw new RuntimeException("quarter error");
            }
            String startTimeStr = year + "-" + quarterDateEnum.getStartDate();
            String endTimeStr = year + "-" + quarterDateEnum.getEndDate();
            Date startTime = TimeUtil.parseDateStr(startTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
            Date endTime = TimeUtil.parseDateStr(endTimeStr, "yyyy-MM-dd HH:mm:ss.SSS");

            List<Map<String, Object>> list = recommendedStockMapper.getByParentIdAndBrokerAndDate(parentId, broker, startTime, endTime);
            if (list == null || list.isEmpty()) {
                return null;
            }

            Map<String, BigDecimal> map = new HashMap<>();
            for (Map<String, Object> item : list) {
                String pushMonth = item.get("pushMonth").toString();
                String secUniCode = item.get("secUniCode").toString();
                BigDecimal goldRate;
                if (item.get("goldRate") != null) {
                    goldRate = new BigDecimal(item.get("goldRate").toString());
                } else {
                    Response<Map<Long, StockMonthReturnRateBO>> response = apiPlatFormClient.getMonthReturnRate(secUniCode, pushMonth);
                    if (response == null || response.getCode() != 200) {
                        throw new RuntimeException("从平台获取股票月盈利失败,返回值：" + JSON.toJSONString(response));
                    } else if (response.getData() == null || response.getData().isEmpty() || response.getData().get(Long.valueOf(secUniCode)) == null) {
                        throw new RuntimeException("从平台获取股票月盈利成功,但是该secUniCode:" + secUniCode + "无数据,返回值：" + JSON.toJSONString(response));
                    }
                    StockMonthReturnRateBO stockMonthReturnRateBO = response.getData().get(Long.valueOf(secUniCode));
                    goldRate = stockMonthReturnRateBO.getGoldRate();
                }

                if (!map.containsKey(pushMonth)) {
                    map.put(pushMonth, goldRate);
                } else {
                    map.put(pushMonth, map.get(pushMonth).add(goldRate));
                }
            }

            BigDecimal totalGoldStockRate = new BigDecimal("0");
            for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                totalGoldStockRate = totalGoldStockRate.add(entry.getValue());
            }

            return totalGoldStockRate.divide(new BigDecimal(map.size() + ""), 4, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            logger.error("券商评分定时任务,获取金股收益率失败,时间:{}，错误:{}", TimeUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
            return null;
        }
    }
}
