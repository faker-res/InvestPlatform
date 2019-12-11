package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.vo.RecommendedStockBrokerRateVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockCountVo;

import java.util.List;
import java.util.Map;

public interface IRecommendedStockCountService {
    Map<String, Object> getIndustryStatistics(String userId, String date) throws Exception;

    List<Map<String, Object>> getStockList(String userId, String date, String keyword) throws Exception;

    Map<String, Object> getMonthStockStatistics(RecommendedStockCountVo recommendedStockCountVo) throws Exception;

    List<Map<String, Object>> getBrokerList(String userId, String date, String keyword) throws Exception;

    Map<String, Object> getBrokerStockStatistics(RecommendedStockCountVo recommendedStockCountVo) throws Exception;

    List<Map<String, Object>> countBrokerRate(RecommendedStockBrokerRateVo brokerRateVo) throws Exception;
}
