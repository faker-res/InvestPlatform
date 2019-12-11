package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.client.IInvestReportClient;
import la.niub.abcapi.invest.seller.component.util.RedisUtil;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.constant.RedisConstant;
import la.niub.abcapi.invest.seller.constant.ReportSearchConstant;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkAnalystTargetMapper;
import la.niub.abcapi.invest.seller.model.TraderMarkAnalystTargetModel;
import la.niub.abcapi.invest.seller.model.request.client.AnalystReportRequest;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.*;
import la.niub.abcapi.invest.seller.service.IAnalystTargetService;
import la.niub.abcapi.invest.seller.service.IAnalystTargetTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : ppan
 * @description : 研究员达成天数历史数据同步
 * @date : 2019-03-01 10:04
 */
@Service
public class AnalystTargetServiceImpl implements IAnalystTargetService {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private IAnalystTargetTransactionService analystTargetTransactionService;

    @Autowired
    private TraderMarkAnalystTargetMapper traderMarkAnalystTargetMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IInvestReportClient investReportClient;

    private final static Integer LIMIT = 100;

    //线程池
    private static ExecutorService fixed_thread_pool;
    {
        fixed_thread_pool = Executors.newFixedThreadPool(4);
    }

    @Override
    public void synchronizeHistoricalData(String parentId, String startTime, String endTime) throws Exception {
        logger.info("synchronize historical data begin");

        logger.info("同步研究员达成率参数, parentId:{}. startTime：{}, endTime: {}", parentId, startTime, endTime);
        AnalystReportRequest analystReportRequest = new AnalystReportRequest();
        analystReportRequest.setEndDate(endTime);
        analystReportRequest.setPageIndex(1);
        analystReportRequest.setPageSize(1);
        analystReportRequest.setParentId(parentId);
        analystReportRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
        analystReportRequest.setStartDate(startTime);
        logger.info("从研报云端获取邮件透视中存在目标价的研报,param:{}", JSON.toJSONString(analystReportRequest));
        Response<AnalystReportResponse> countResponse = investReportClient.relatedAnalystReport(analystReportRequest);
        if (countResponse == null || countResponse.getCode() != 200) {
            throw new RuntimeException("从研报云端获取邮件透视中存在目标价的研报数量失败, 返回值:" + JSON.toJSONString(countResponse));
        }
        logger.info("同步研究员达成率获取研报数量返回值, response:{}", JSON.toJSONString(countResponse));

        Long count = countResponse.getData() == null || countResponse.getData().getNumFound() == null ? 0L : countResponse.getData().getNumFound();
        logger.info("同步研究员达成率count, count：{}", count);
        count = count % LIMIT == 0 ? count / LIMIT : count / LIMIT + 1;
        logger.info("同步研究员达成率页数, page：{}", count);
        List<Integer> pageList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            pageList.add(i);
        }

        for (Integer page : pageList) {
            fixed_thread_pool.execute(() -> {
                try {
                    handleDateByPage(parentId, startTime, endTime, page);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("同步研究员邮件研报达成天数失败, parentId, startTime: {}, endTime: {}, page: {}, error: {}", parentId, startTime, endTime, page, e);
                    throw new RuntimeException(e);
                }
            });
        }

        logger.info("synchronize historical data end");
    }

    @Override
    public void synchronizeHistoricalDataByPage(String parentId, String startTime, String endTime, Integer page) throws Exception {
        logger.info("synchronize historical data by page begin");

        logger.info("param: parentId：{} starTime: {}, endTime: {}, page: {}", parentId, startTime, endTime, page);

        AnalystReportRequest analystReportRequest = new AnalystReportRequest();
        analystReportRequest.setEndDate(endTime);
        analystReportRequest.setPageIndex(1);
        analystReportRequest.setPageSize(1);
        analystReportRequest.setParentId(parentId);
        analystReportRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
        analystReportRequest.setStartDate(startTime);

        logger.info("同步研究员达成率参数, param: {}", JSON.toJSONString(analystReportRequest));
        Response<AnalystReportResponse> countResponse = investReportClient.relatedAnalystReport(analystReportRequest);
        if (countResponse == null || countResponse.getCode() != 200) {
            throw new RuntimeException("从研报云端获取邮件透视中存在目标价的研报数量失败, 返回值:" + JSON.toJSONString(countResponse));
        }
        logger.info("同步研究员达成率获取研报数量返回值, response:{}", JSON.toJSONString(countResponse));

        Long count = countResponse.getData() == null || countResponse.getData().getNumFound() == null ? 0L : countResponse.getData().getNumFound();
        logger.info("同步研究员达成率count, count：{}", count);
        count = count % LIMIT == 0 ? count / LIMIT : count / LIMIT + 1;
        logger.info("同步研究员达成率页数, page：{}", count);

        if (page < 0 && page >= count) {
            logger.info("参数:page: {}, 超过最大或最小值", page);
            throw new RuntimeException("参数[page]超过最大或最小值");
        }

        handleDateByPage(parentId, startTime, endTime, page);

        logger.info("synchronize historical data by page end");
    }

    @Override
    public void removeCache() throws Exception {
        redisUtil.delete(RedisConstant.ANALYST_TARGET_STOCK);
    }

    private void handleDateByPage(String parentId, String startTime, String endTime, Integer page) throws Exception {
        Map<String, TraderMarkAnalystTargetModel> analystTargetModelMap = new HashMap<>();

        AnalystReportRequest analystReportRequest = new AnalystReportRequest();
        analystReportRequest.setEndDate(endTime);
        analystReportRequest.setPageIndex(page);
        analystReportRequest.setPageSize(LIMIT);
        analystReportRequest.setParentId(parentId);
        analystReportRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
        analystReportRequest.setStartDate(startTime);

        logger.info("获取邮件透视中存在目标价的研报集合参数, param:{}", JSON.toJSONString(analystReportRequest));
        Response<AnalystReportResponse> response = investReportClient.relatedAnalystReport(analystReportRequest);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从研报云端获取邮件透视中存在目标价的研报集合失败, 返回值:" + JSON.toJSONString(response));
        }
        logger.info("获取邮件透视中存在目标价的研报返回值, response：{}", JSON.toJSONString(response));
        List<AnalystReportItemResponse> list = response.getData() == null || response.getData().getData() == null ? null : response.getData().getData();
        if (list != null && list.size() > 0) {
            for(AnalystReportItemResponse item : list) {
                String reportParentId = item.getReportParentId();
                if (StringUtil.isEmpty(reportParentId)) {
                    if (StringUtil.isEmpty(item.getReportUserId())) {
                        continue;
                    } else {
                        try {
                            reportParentId = userInfoService.getUserParentId(item.getReportUserId());
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }

                String stockCode = item.getReportSecurityId();
                if (StringUtil.isEmpty(stockCode) || item.getReportTargetPriceLow() == null
                        || !stockCode.matches("^[0-9A-Za-z]{4,6}(\\.[A-Za-z]{2})?$") || stockCode.toUpperCase().endsWith(".HK")) {
                    continue;
                }
                String broker = item.getReportPublishName();
                String analystNames = item.getReportAnalystName();
                if (StringUtil.isEmpty(analystNames)) {
                    continue;
                }
                List<String> analystNameList = Arrays.asList(analystNames.split(","));
                if (analystNameList.isEmpty()) {
                    continue;
                }
                String publishDateStr = item.getReportPublishDate();
                Date targetDate = TimeUtil.parseDateStr(publishDateStr, "yyyy-MM-dd");
                if (targetDate == null) {
                    continue;
                }
                String stockName = item.getReportSecurityName();
                if (StringUtil.isEmpty(stockName)) {
                    continue;
                }
                BigDecimal targetValue = item.getReportTargetPriceLow();

                Long secUniCode;
                if (redisUtil.hexists(RedisConstant.ANALYST_TARGET_STOCK, stockCode) &&
                        !StringUtil.isEmpty(redisUtil.hget(RedisConstant.ANALYST_TARGET_STOCK, stockCode, String.class))) {
                    secUniCode = Long.valueOf(redisUtil.hget(RedisConstant.ANALYST_TARGET_STOCK, stockCode, String.class));
                } else {
                    logger.info("获取股票基本信息参数, stockCode: {}", stockCode);
                    Response<List<SecBasicInfoResponse>> stockResponse = apiPlatFormClient.getStockByNameOrCode(stockCode);
                    logger.info("获取股票基本信息返回值, response: {}", JSON.toJSONString(stockResponse));
                    if (stockResponse == null || stockResponse.getCode() !=  200) {
                        throw new RuntimeException("从平台获取股票基本信息失败, 返回值:" + JSON.toJSONString(stockResponse));
                    }
                    if (stockResponse.getData() == null || stockResponse.getData().isEmpty()
                            || stockResponse.getData().get(0) == null || stockResponse.getData().get(0).getSec_uni_code() == null) {
                        continue;
                    }
                    secUniCode = stockResponse.getData().get(0).getSec_uni_code();

                    redisUtil.hset(RedisConstant.ANALYST_TARGET_STOCK, stockCode, secUniCode.toString());
                }

                logger.info("获取发出目标价时股票价格参数, secUniCode: {}, day: {}", secUniCode, publishDateStr);
                Response<Map<Long, KlineResponse>> stockPriceResponse = apiPlatFormClient.getPriceByDay(secUniCode.toString(), publishDateStr);
                logger.info("获取发出目标价时股票价格返回值, response: {}", JSON.toJSONString(stockPriceResponse));
                if (stockPriceResponse == null || stockPriceResponse.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票价格信息失败, 返回值:" + JSON.toJSONString(stockPriceResponse));
                }
                if (stockPriceResponse.getData() == null || stockPriceResponse.getData().isEmpty()
                        || stockPriceResponse.getData().get(secUniCode) == null || stockPriceResponse.getData().get(secUniCode).getOpen() == null) {
                    continue;
                }
                BigDecimal targetStockValue = stockPriceResponse.getData().get(secUniCode).getOpen();
                logger.info("获取达到目标价时股票价格和交易时间参数, secUniCode: {}, startTime: {}, targetPrice: {}, currentPrice: {}", secUniCode, publishDateStr, targetValue, targetStockValue);
                Response<KlineResponse> tradeDateResponse = apiPlatFormClient.getTradeDateByPrice(secUniCode, publishDateStr, targetValue, targetStockValue);
                logger.info("获取达到目标价时股票价格和交易时间返回值, response: {}", JSON.toJSONString(tradeDateResponse));
                if (tradeDateResponse == null || tradeDateResponse.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票价格信息失败, 返回值:" + JSON.toJSONString(tradeDateResponse));
                }

                Date targetCompDate = tradeDateResponse.getData() == null ? null : tradeDateResponse.getData().getTrade_date();

                BigDecimal targetCompStockValue = tradeDateResponse.getData() == null ? null : tradeDateResponse.getData().getClose_price();

                Long targetCompDays = targetCompDate == null ? null : (targetCompDate.getTime() - targetDate.getTime()) / (1000 * 3600 * 24);

                for (String analyst : analystNameList) {
                    if (StringUtil.isEmpty(analyst)) {
                        continue;
                    }
                    String key = broker + "-" + analyst + "-" + secUniCode + "-" + stockCode + "-" + stockName + "-" + targetValue + "-" + publishDateStr;
                    if (analystTargetModelMap.containsKey(key)) {
                        continue;
                    }

                    TraderMarkAnalystTargetModel analystTargetModel = new TraderMarkAnalystTargetModel();
                    analystTargetModel.setParentId(reportParentId);
                    analystTargetModel.setBroker(broker);
                    analystTargetModel.setAnalyst(analyst);
                    analystTargetModel.setSecUniCode(secUniCode);
                    analystTargetModel.setStockCode(stockCode);
                    analystTargetModel.setStockName(stockName);
                    analystTargetModel.setTargetValue(targetValue);
                    analystTargetModel.setTargetDate(targetDate);
                    analystTargetModel.setTargetStockValue(targetStockValue);
                    analystTargetModel.setTargetCompDate(targetCompDate);
                    analystTargetModel.setTargetCompStockValue(targetCompStockValue);
                    analystTargetModel.setTargetCompDays(targetCompDays == null ? null : Integer.valueOf(targetCompDays.toString()));
                    Date now = new Date();
                    analystTargetModel.setCreateTime(now);
                    analystTargetModel.setUpdateTime(now);

                    TraderMarkAnalystTargetModel existModel = traderMarkAnalystTargetMapper.selectByCondition(reportParentId, broker, analyst, secUniCode, stockCode, stockName, targetValue, targetDate);
                    if (existModel != null) {
                        continue;
                    }
                    analystTargetModelMap.put(key, analystTargetModel);
                }
            }
        }

        Collection<TraderMarkAnalystTargetModel> values = analystTargetModelMap.values();
        logger.info("插入研究员达成天数数据: {}", JSON.toJSONString(values));
        if (!values.isEmpty()) {
            analystTargetTransactionService.insertBatch(values);
        }
    }
}
