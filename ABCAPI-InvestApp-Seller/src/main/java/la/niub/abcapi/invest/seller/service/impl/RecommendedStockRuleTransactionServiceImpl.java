package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.RecommendedStockRuleMapper;
import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;
import la.niub.abcapi.invest.seller.service.IRecommendedStockRuleTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ppan
 * @description : 金股规则增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-01-19 14:58
 */
@Service
public class RecommendedStockRuleTransactionServiceImpl implements IRecommendedStockRuleTransactionService {

    @Autowired
    private RecommendedStockRuleMapper recommendedStockRuleMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer save(RecommendedStockRuleModel ruleModel) throws Exception {
        return recommendedStockRuleMapper.insertSelective(ruleModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer update(RecommendedStockRuleModel ruleModel) throws Exception {
        return recommendedStockRuleMapper.updateByPrimaryKeySelective(ruleModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer delete(Long id) throws Exception {
        return recommendedStockRuleMapper.deleteByPrimaryKey(id);
    }
}
