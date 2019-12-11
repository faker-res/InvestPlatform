package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleAddVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleUpdateVo;

import java.util.List;
import java.util.Map;

public interface IRecommendedStockRuleService {
    List<Map<String, String>> getBrokerList(String userId, String keyword, Integer limit) throws Exception;

    List<RecommendedStockRuleModel> getRuleList(String userId, String brokers) throws Exception;

    void save(RecommendedStockRuleAddVo ruleAddVo) throws Exception;

    void update(RecommendedStockRuleUpdateVo ruleUpdateVo) throws Exception;

    void delete(String userId, Long id) throws Exception;

    RecommendedStockRuleModel getRuleDetail(String userId, Long id) throws Exception;
}
