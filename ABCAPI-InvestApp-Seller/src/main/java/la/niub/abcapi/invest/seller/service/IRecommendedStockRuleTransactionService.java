package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;

public interface IRecommendedStockRuleTransactionService {
    Integer save(RecommendedStockRuleModel ruleModel) throws Exception;

    Integer update(RecommendedStockRuleModel ruleModel) throws Exception;

    Integer delete(Long id) throws Exception;
}
