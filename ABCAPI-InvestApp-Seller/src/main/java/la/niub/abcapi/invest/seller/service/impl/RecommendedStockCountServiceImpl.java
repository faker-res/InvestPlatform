package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.dao.invest.RecommendedStockMapper;
import la.niub.abcapi.invest.seller.dao.invest.RecommendedStockRuleMapper;
import la.niub.abcapi.invest.seller.model.bo.StockMonthReturnRateBO;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockBrokerRateVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockCountVo;
import la.niub.abcapi.invest.seller.service.IRecommendedStockCountService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : ppan
 * @description : 金股统计service
 * @date : 2019-01-15 17:37
 */
@Service
public class RecommendedStockCountServiceImpl implements IRecommendedStockCountService {

    @Autowired
    private RecommendedStockMapper recommendedStockMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private RecommendedStockRuleMapper recommendedStockRuleMapper;

    @Override
    public Map<String, Object> getIndustryStatistics(String userId, String date) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        Integer total = recommendedStockMapper.getCountByParentIdAndDate(parentId, date);
        List<Map<String, Object>> items = new ArrayList<>();
        if (total > 0) {
            items = recommendedStockMapper.getIndustryStockCountByParentIdAndDate(parentId, date);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("items", items);
        return result;
    }

    @Override
    public List<Map<String, Object>> getStockList(String userId, String date, String keyword) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        return recommendedStockMapper.getStockListByParentIdAndDateAndKeyword(parentId, date, keyword);
    }

    @Override
    public Map<String, Object> getMonthStockStatistics(RecommendedStockCountVo recommendedStockCountVo) throws Exception {
        String parentId = userInfoService.getUserParentId(recommendedStockCountVo.getUserId());

        List<String> keywordList = null;
        if (!StringUtil.isEmpty(recommendedStockCountVo.getKeyword())) {
            keywordList = Arrays.asList(recommendedStockCountVo.getKeyword().split(","));
        }

        String date = recommendedStockCountVo.getDate();
        Integer total = recommendedStockMapper.getDistinctStockCountByParentIdAndDateAndKeyword(parentId, date, keywordList);

        List<Map<String, Object>> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;

        Integer queryYear = Integer.valueOf(date.split("-")[0]);
        Integer queryMonth = Integer.valueOf(date.split("-")[1]);
        String goldRateMonth;
        if (queryYear < currentYear || (queryYear.equals(currentYear) && queryMonth < currentMonth)) {
            goldRateMonth = date;
        } else {
            goldRateMonth = TimeUtil.getLastMonth(new Date(), "yyyy-MM");
        }

        if (total > 0) {
            //String lastMonth = TimeUtil.getLastMonth(date, "yyyy-MM");
            Integer offset = recommendedStockCountVo.getOffset() == null
                    || recommendedStockCountVo.getOffset() <= 0 ? 1 : recommendedStockCountVo.getOffset();
            Integer limit = recommendedStockCountVo.getLimit() == null ||
                    recommendedStockCountVo.getLimit() <= 0 ? 12 : recommendedStockCountVo.getLimit();
            offset = (offset - 1) * limit;
            items = recommendedStockMapper.getDistinctStockListByParentIdAndDateAndKeyword(parentId, date, keywordList, goldRateMonth, offset, limit);
        }

        for (Map<String, Object> item : items) {
            if (item == null) {
                continue;
            }

            if (item.get("goldRate") == null || item.get("industryRate") == null) {
                Response<Map<Long, StockMonthReturnRateBO>> response = apiPlatFormClient.getMonthReturnRate(item.get("secUniCode").toString(), goldRateMonth);
                if (response == null || response.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票月盈利失败,返回值：" + JSON.toJSONString(response));
                } else if (response.getData() == null || response.getData().isEmpty() || response.getData().get(Long.valueOf(item.get("secUniCode").toString())) == null) {
                    throw new RuntimeException("从平台获取股票月盈利成功,但是该secUniCode:" + item.get("secUniCode").toString() + "无数据,返回值：" + JSON.toJSONString(response));
                }
                StockMonthReturnRateBO stockMonthReturnRateBO = response.getData().get(Long.valueOf(item.get("secUniCode").toString()));
                item.put("goldRate", stockMonthReturnRateBO.getGoldRate());
                item.put("industryRate", stockMonthReturnRateBO.getIndustryRate());
                item.put("excessRate", stockMonthReturnRateBO.getGoldRate().subtract(stockMonthReturnRateBO.getIndustryRate()));
            }

            // 查询推荐理由
            List<Map<String, Object>> recommendedReasons = null;
            if (item.get("stockCode") != null) {
                recommendedReasons = recommendedStockMapper.getRecommendedReasonByParentIdAndDateAndStockCode(parentId, date, item.get("stockCode").toString());
            }
            item.put("recommendedReasons", recommendedReasons);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("items", items);
        return result;
    }

    @Override
    public List<Map<String, Object>> getBrokerList(String userId, String date, String keyword) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        return recommendedStockMapper.getBrokerListByParentIdAndDateAndKeyword(parentId, date, keyword);
    }

    @Override
    public Map<String, Object> getBrokerStockStatistics(RecommendedStockCountVo recommendedStockCountVo) throws Exception {
        String parentId = userInfoService.getUserParentId(recommendedStockCountVo.getUserId());

        List<String> brokerList = new ArrayList<>();
        Integer total;
        String date = recommendedStockCountVo.getDate();
        if (!StringUtil.isEmpty(recommendedStockCountVo.getKeyword())) {
            brokerList = Arrays.asList(recommendedStockCountVo.getKeyword().split(","));
            total = brokerList.size();
        } else {
            List<Map<String, Object>> brokerResultList = recommendedStockMapper.getBrokerListByParentIdAndDateAndKeyword(parentId, date, null);
            for (Map<String, Object> map : brokerResultList) {
                if (map != null && map.get("broker") != null && !StringUtil.isEmpty(map.get("broker").toString())) {
                    brokerList.add(map.get("broker").toString());
                }
            }
            total = brokerResultList.size();
        }

        List<Map<String, Object>> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;

        Integer queryYear = Integer.valueOf(date.split("-")[0]);
        Integer queryMonth = Integer.valueOf(date.split("-")[1]);
        String goldRateMonth;
        if (queryYear < currentYear || (queryYear.equals(currentYear) && queryMonth < currentMonth)) {
            goldRateMonth = date;
        } else {
            goldRateMonth = TimeUtil.getLastMonth(new Date(), "yyyy-MM");
        }

        if (total > 0) {
            //String lastMonth = TimeUtil.getLastMonth(date, "yyyy-MM");
            Integer offset = recommendedStockCountVo.getOffset() == null || recommendedStockCountVo.getOffset() <= 0
                    || recommendedStockCountVo.getOffset() > total ? 1 : recommendedStockCountVo.getOffset();
            String broker = brokerList.get(offset - 1);
            items = recommendedStockMapper.getAllStockListByParentIdAndDateAndKeyword(parentId, date, broker, goldRateMonth);

            if (items == null || items.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("broker", broker);
                map.put("brokerStockCount", 1);
                map.put("secUniCode", null);
                map.put("stockCode", null);
                map.put("stock_name", null);
                map.put("industry", null);
                map.put("goldRate", null);
                map.put("industryRate", null);
                map.put("excessRate", null);
                map.put("recommendedReason", null);

                items = new ArrayList<>();
                items.add(map);
                Map<String, Object> result = new HashMap<>();
                result.put("total", total);
                result.put("items", items);
                return result;
            }
        }

        Map<String, Integer> brokerStockCountMap = new HashMap<>();
        for (Map<String, Object> item : items) {
            if (item == null) {
                continue;
            }

            if (item.get("goldRate") == null || item.get("industryRate") == null) {
                Response<Map<Long, StockMonthReturnRateBO>> response = apiPlatFormClient.getMonthReturnRate(item.get("secUniCode").toString(), goldRateMonth);
                if (response == null || response.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票月盈利失败,返回值：" + JSON.toJSONString(response));
                } else if (response.getData() == null || response.getData().isEmpty() || response.getData().get(Long.valueOf(item.get("secUniCode").toString())) == null) {
                    throw new RuntimeException("从平台获取股票月盈利成功,但是该secUniCode:" + item.get("secUniCode").toString() + "无数据,返回值：" + JSON.toJSONString(response));
                }
                StockMonthReturnRateBO stockMonthReturnRateBO = response.getData().get(Long.valueOf(item.get("secUniCode").toString()));
                item.put("goldRate", stockMonthReturnRateBO.getGoldRate());
                item.put("industryRate", stockMonthReturnRateBO.getIndustryRate());
                item.put("excessRate", stockMonthReturnRateBO.getGoldRate().subtract(stockMonthReturnRateBO.getIndustryRate()));
            }

            // 统计出当前页相同券商的股票数,给前端合并单元格使用
            String broker = item.get("broker").toString();
            if (brokerStockCountMap.containsKey(broker)) {
                brokerStockCountMap.put(broker, brokerStockCountMap.get(broker) + 1);
            } else {
                brokerStockCountMap.put(broker, 1);
            }
        }

        for (Map<String, Object> item : items) {
            if (item == null) {
                continue;
            }

            String broker = item.get("broker").toString();
            Integer brokerStockCount = brokerStockCountMap.get(broker) == null ? 0 : brokerStockCountMap.get(broker);
            item.put("brokerStockCount", brokerStockCount);
            brokerStockCountMap.remove(broker);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("items", items);
        return result;
    }

    @Override
    public List<Map<String, Object>> countBrokerRate(RecommendedStockBrokerRateVo brokerRateVo) throws Exception {
        String parentId = userInfoService.getUserParentId(brokerRateVo.getUserId());
        brokerRateVo.setParentId(parentId);
        /*DateFormat sf = new SimpleDateFormat("yyyy-MM");
        String currentMonth = sf.format(Calendar.getInstance().getTime());
        if (currentMonth.equals(brokerRateVo.getPushMonth())) {
            brokerRateVo.setLastPushMonth(TimeUtil.getLastMonth(brokerRateVo.getPushMonth(), "yyyy-MM"));
        }else{
            brokerRateVo.setLastPushMonth(brokerRateVo.getPushMonth());
        }*/
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;

        String[] stringArray = brokerRateVo.getPushMonth().split("-");
        Integer queryYear = Integer.valueOf(stringArray[0]);
        Integer queryMonth = Integer.valueOf(stringArray[1]);
        String goldRateMonth;
        if (queryYear < currentYear || (queryYear.equals(currentYear) && queryMonth < currentMonth)) {
            goldRateMonth = brokerRateVo.getPushMonth();
        } else {
            goldRateMonth = TimeUtil.getLastMonth(new Date(), "yyyy-MM");
        }
        brokerRateVo.setLastPushMonth(goldRateMonth);

        List<Map<String, Object>> result = recommendedStockMapper.countBrokerRateV2(brokerRateVo);

        Map<String, List<Object>> stocksGroupByBroker = new HashMap<>();
        for (Map<String, Object> item : result){
            String broker = (String) item.get("broker");
            List<Object> stocks = stocksGroupByBroker.getOrDefault(broker,new ArrayList<>());
            if (stocks.size() >= 10){
                continue;
            }

            BigDecimal goldRate = item.get("gold_rate") == null ? null : (BigDecimal) item.get("gold_rate");
            if (goldRate == null) {
                Response<Map<Long, StockMonthReturnRateBO>> response = apiPlatFormClient.getMonthReturnRate(item.get("sec_uni_code").toString(), goldRateMonth);
                if (response == null || response.getCode() != 200) {
                    throw new RuntimeException("从平台获取股票月盈利失败,返回值：" + JSON.toJSONString(response));
                } else if (response.getData() == null || response.getData().isEmpty() || response.getData().get(Long.valueOf(item.get("sec_uni_code").toString())) == null) {
                    throw new RuntimeException("从平台获取股票月盈利成功,但是该secUniCode:" + item.get("sec_uni_code").toString() + "无数据,返回值：" + JSON.toJSONString(response));
                }
                StockMonthReturnRateBO stockMonthReturnRateBO = response.getData().get(Long.valueOf(item.get("sec_uni_code").toString()));
                goldRate = stockMonthReturnRateBO.getGoldRate();

            }
            stocks.add(goldRate);
            stocksGroupByBroker.put(broker,stocks);
        }

        List<Map<String, Object>> combinedResult = new ArrayList<>();
        for (Map.Entry<String, List<Object>> entry : stocksGroupByBroker.entrySet()){
            Double sum = 0D;
            for (Object item : entry.getValue()){
                sum += Double.valueOf(item.toString());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("broker", entry.getKey());
            Double divide = sum/entry.getValue().size();
            map.put("combinedRate", new BigDecimal(divide).setScale(8, BigDecimal.ROUND_HALF_UP));
            combinedResult.add(map);
        }
        Collections.sort(combinedResult, (o1, o2) -> Double.valueOf(o2.get("combinedRate").toString()).compareTo(Double.valueOf(o1.get("combinedRate").toString())));

        List<String> brokerList = recommendedStockRuleMapper.getBrokerByParentId(parentId);
        if (brokerList != null && !brokerList.isEmpty()) {
            brokerList.forEach(broker -> {
                if (!stocksGroupByBroker.containsKey(broker)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("broker", broker);
                    map.put("combinedRate", new BigDecimal("0"));
                    combinedResult.add(map);
                }
            });
        }
        return combinedResult;
    }
}
