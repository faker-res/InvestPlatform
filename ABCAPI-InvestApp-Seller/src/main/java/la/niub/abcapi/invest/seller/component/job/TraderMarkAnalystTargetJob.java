package la.niub.abcapi.invest.seller.component.job;

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
import la.niub.abcapi.invest.seller.service.IAnalystTargetTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : ppan
 * @description : 同步并更新研究员达成天数定时任务
 * @date : 2019-03-01 16:26
 */
@Component
public class TraderMarkAnalystTargetJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private TraderMarkAnalystTargetMapper traderMarkAnalystTargetMapper;

    @Autowired
    private IAnalystTargetTransactionService analystTargetTransactionService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IInvestReportClient investReportClient;

    private final static Integer LIMIT = 100;

    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    public void synchronizeAnalystTarget() {
        logger.info("synchronize analyst target begin");

        ExecutorService updateFixedThreadPool = Executors.newFixedThreadPool(4);
        ExecutorService saveFixedThreadPool = Executors.newFixedThreadPool(4);

        try {
            updateAnalystTarget(updateFixedThreadPool);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("update analyst target fail, error: {}", e);
        }

        updateFixedThreadPool.shutdown();
        try {
            updateFixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("update_fixed_thread_pool.shutdown() fail, e: {}", e);
        }

        try {
            saveAnalystTarget(saveFixedThreadPool);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("save analyst target fail, error: {}", e);
        }

        saveFixedThreadPool.shutdown();
        try {
            saveFixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("save_fixed_thread_pool.shutdown() fail, e: {}", e);
        }

        logger.info("synchronize analyst target end");
    }

    private void updateAnalystTarget(ExecutorService updateFixedThreadPool) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        String dateStr = TimeUtil.toString(date, "yyyy-MM-dd");

        logger.info("update analyst target from table investnew_trader_mark_analyst_target begin");
        Integer count;
        try {
            count = traderMarkAnalystTargetMapper.selectCountByNotComp();
        } catch (Exception e1) {
            // 防止数据库连接失效
            try {
                count = traderMarkAnalystTargetMapper.selectCountByNotComp();
            } catch (Exception e2) {
                e2.printStackTrace();
                logger.error("获取表investnew_trader_mark_analyst_target中未完成目标的记录数量失败, e: {}", e2);
                throw new RuntimeException(e2);
            }
        }

        logger.info("table investnew_trader_mark_analyst_target, 未达成目标count, count: {}", count);
        count = count % LIMIT == 0 ? count / LIMIT : count / LIMIT + 1;
        logger.info("table investnew_trader_mark_analyst_target, 未达成目标page, page: {}", count);
        List<Integer> pageList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            pageList.add(i);
        }

        for (Integer page : pageList) {
            updateFixedThreadPool.execute(() -> {
                try {
                    update(dateStr, page);
                } catch (Exception e1) {
                    // 防止数据库连接失效
                    try {
                        update(dateStr, page);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        logger.error("update analyst target from table investnew_trader_mark_analyst_target fail, date: {}, page: {}, error: {}", dateStr, page, e2);
                    }
                }
            });
        }

        logger.info("update analyst target from table investnew_trader_mark_analyst_target end");
    }

    private void update(String dateStr, Integer page) throws Exception {
        List<TraderMarkAnalystTargetModel> updateList = new ArrayList<>();

        logger.info("获取未达成目标记录参数, page : {}, limit: {}, offset: {}", page, LIMIT, page * LIMIT);
        List<TraderMarkAnalystTargetModel> list = traderMarkAnalystTargetMapper.selectByNotComp(page * LIMIT, LIMIT);
        logger.info("获取未达成目标记录list:{}", JSON.toJSONString(list));

        Map<Long, BigDecimal> stockPriceMap = new HashMap<>();
        Map<Long, Date> stockTradeDateMap = new HashMap<>();
        for (TraderMarkAnalystTargetModel item : list) {
            Long secUniCode = item.getSecUniCode();

            if (!redisUtil.hexists(RedisConstant.ANALYST_TARGET_STOCK, item.getStockCode()) ||
                    StringUtil.isEmpty(redisUtil.hget(RedisConstant.ANALYST_TARGET_STOCK, item.getStockCode(), String.class))) {
                redisUtil.hset(RedisConstant.ANALYST_TARGET_STOCK, item.getStockCode(), secUniCode.toString());
            }

            BigDecimal stockPrice;
            Date tradeDate;
            if (stockPriceMap.containsKey(secUniCode) && stockTradeDateMap.containsKey(secUniCode)) {
                stockPrice = stockPriceMap.get(secUniCode);
                tradeDate = stockTradeDateMap.get(secUniCode);
            } else {
                logger.info("获取股票价格参数, secUniCode：{}, day:{}", secUniCode, dateStr);
                Response<Map<Long, KlineResponse>> stockPriceResponse = apiPlatFormClient.getPriceByDay(secUniCode.toString(), dateStr);
                logger.info("获取股票价格返回值, response：{}", JSON.toJSONString(stockPriceResponse));
                if (stockPriceResponse == null || stockPriceResponse.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票价格信息失败, 返回值:" + JSON.toJSONString(stockPriceResponse));
                }
                stockPrice = stockPriceResponse.getData() == null
                        || stockPriceResponse.getData().isEmpty()
                        || stockPriceResponse.getData().get(secUniCode) == null ? null : stockPriceResponse.getData().get(secUniCode).getClose_price();
                tradeDate = stockPriceResponse.getData() == null
                        || stockPriceResponse.getData().isEmpty()
                        || stockPriceResponse.getData().get(secUniCode) == null ? null : stockPriceResponse.getData().get(secUniCode).getTrade_date();

                stockPriceMap.put(secUniCode, stockPrice);
                stockTradeDateMap.put(secUniCode, tradeDate);
            }

            Boolean update = (tradeDate != null && tradeDate.getTime() >= item.getTargetDate().getTime()) && stockPrice != null &&
                    ((item.getTargetValue().compareTo(item.getTargetStockValue()) >= 0 && stockPrice.compareTo(item.getTargetValue()) >= 0) ||
                            (item.getTargetValue().compareTo(item.getTargetStockValue()) < 0 && stockPrice.compareTo(item.getTargetValue()) <= 0));
            if (update) {
                TraderMarkAnalystTargetModel analystTargetModel = new TraderMarkAnalystTargetModel();
                analystTargetModel.setId(item.getId());
                analystTargetModel.setTargetCompDate(tradeDate);
                analystTargetModel.setTargetCompStockValue(stockPrice);
                Long targetCompDays = (tradeDate.getTime() - item.getTargetDate().getTime()) / (1000 * 3600 * 24);
                analystTargetModel.setTargetCompDays(Integer.valueOf(targetCompDays.toString()));
                analystTargetModel.setUpdateTime(new Date());

                updateList.add(analystTargetModel);
            }
        }

        logger.info("update table investnew_trader_mark_analyst_target begin");
        logger.info("update list : {}", JSON.toJSONString(updateList));
        if (!updateList.isEmpty()) {
            analystTargetTransactionService.updateBatch(updateList);
        }
        logger.info("update table investnew_trader_mark_analyst_target end");
    }

    private void saveAnalystTarget(ExecutorService saveFixedThreadPool) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        String dateStr = TimeUtil.toString(date, "yyyy-MM-dd");

        logger.info("synchronize yesterday mail report analyst target begin");
        logger.info("同步研报中研究员达成率参数, startTime: {}, endTime: {}", dateStr, dateStr);
        AnalystReportRequest analystReportRequest = new AnalystReportRequest();
        analystReportRequest.setEndDate(dateStr);
        analystReportRequest.setPageIndex(1);
        analystReportRequest.setPageSize(1);
        analystReportRequest.setParentId(null);
        analystReportRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
        analystReportRequest.setStartDate(dateStr);
        logger.info("从研报云端获取邮件透视中存在目标价的研报,param:{}", JSON.toJSONString(analystReportRequest));
        Response<AnalystReportResponse> countResponse = investReportClient.relatedAnalystReport(analystReportRequest);
        if (countResponse == null || countResponse.getCode() != 200) {
            throw new RuntimeException("从研报云端获取邮件透视中存在目标价的研报数量失败, 返回值:" + JSON.toJSONString(countResponse));
        }
        logger.info("同步研究员达成率获取研报数量返回值, response: {}", JSON.toJSONString(countResponse));

        Long count = countResponse.getData() == null || countResponse.getData().getNumFound() == null ? 0L : countResponse.getData().getNumFound();
        logger.info("同步研究员达成率count, count：{}", count);
        count = count % LIMIT == 0 ? count / LIMIT : count / LIMIT + 1;
        logger.info("同步研究员达成率页数, page：{}", count);
        List<Integer> pageList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            pageList.add(i);
        }

        for (Integer page : pageList) {
            saveFixedThreadPool.execute(() -> {
                try {
                    save(dateStr, page);
                } catch (Exception e1) {
                    // 防止数据库连接失效
                    try {
                        save(dateStr, page);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        logger.error("save analyst target from table investnew_trader_mark_analyst_target fail, date: {}, page: {}, error: {}", dateStr, page, e2);
                    }
                }
            });
        }

        logger.info("synchronize yesterday mail report analyst target end");
    }

    private void save(String dateStr, Integer page) throws Exception {
        Map<String, TraderMarkAnalystTargetModel> saveMap = new HashMap<>();

        AnalystReportRequest analystReportRequest = new AnalystReportRequest();
        analystReportRequest.setEndDate(dateStr);
        analystReportRequest.setPageIndex(page);
        analystReportRequest.setPageSize(LIMIT);
        analystReportRequest.setParentId(null);
        analystReportRequest.setSourceType(ReportSearchConstant.NEW_SEARCH_TYPE_MAIL);
        analystReportRequest.setStartDate(dateStr);

        logger.info("获取邮件透视中存在目标价的研报集合参数, param:{}", JSON.toJSONString(analystReportRequest));
        Response<AnalystReportResponse> response = investReportClient.relatedAnalystReport(analystReportRequest);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从研报云端获取邮件透视中存在目标价的研报集合失败, 返回值:" + JSON.toJSONString(response));
        }
        logger.info("获取邮件透视中存在目标价的研报返回值, response: {}", JSON.toJSONString(response));
        List<AnalystReportItemResponse> mailReportTextList =  response.getData() == null || response.getData().getData() == null ? null : response.getData().getData();
        if (mailReportTextList != null && mailReportTextList.size() > 0) {
            for(AnalystReportItemResponse item : mailReportTextList) {
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
                BigDecimal closePrice = stockPriceResponse.getData().get(secUniCode).getClose_price();
                Date tradeDate = stockPriceResponse.getData().get(secUniCode).getTrade_date();

                Boolean update = (tradeDate != null && tradeDate.getTime() >= targetDate.getTime()) && closePrice != null &&
                        ((targetValue.compareTo(targetStockValue) >= 0 && closePrice.compareTo(targetValue) >= 0) ||
                                (targetValue.compareTo(targetStockValue) < 0 && closePrice.compareTo(targetValue) <= 0));

                for (String analyst : analystNameList) {
                    if (StringUtil.isEmpty(analyst)) {
                        continue;
                    }
                    String key = broker + "-" + analyst + "-" + secUniCode + "-" + stockCode + "-" + stockName + "-" + targetValue + "-" + publishDateStr;
                    if (saveMap.containsKey(key)) {
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
                    if (update) {
                        analystTargetModel.setTargetCompDate(stockPriceResponse.getData().get(secUniCode).getTrade_date());
                        analystTargetModel.setTargetCompStockValue(closePrice);
                        Long targetCompDays = (tradeDate.getTime() - targetDate.getTime()) / (1000 * 3600 * 24);
                        analystTargetModel.setTargetCompDays(Integer.valueOf(targetCompDays.toString()));
                    }
                    Date now = new Date();
                    analystTargetModel.setCreateTime(now);
                    analystTargetModel.setUpdateTime(now);

                    TraderMarkAnalystTargetModel existModel = traderMarkAnalystTargetMapper.selectByCondition(reportParentId, broker, analyst, secUniCode, stockCode, stockName, targetValue, targetDate);
                    if (existModel != null) {
                        continue;
                    }
                    saveMap.put(key, analystTargetModel);
                }
            }
        }

        logger.info("save table investnew_trader_mark_analyst_target begin");
        Collection<TraderMarkAnalystTargetModel> saveList = saveMap.values();
        logger.info("save list : {}", JSON.toJSONString(saveList));
        if (!saveMap.isEmpty()) {
            analystTargetTransactionService.insertBatch(saveList);
        }
        logger.info("save table investnew_trader_mark_analyst_target end");
    }
}
