package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.TraderMarkAnalystTargetMapper;
import la.niub.abcapi.invest.seller.model.TraderMarkAnalystTargetModel;
import la.niub.abcapi.invest.seller.service.IAnalystTargetTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author : ppan
 * @description : 研究员平均达成天数增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-03-01 15:54
 */
@Service
public class AnalystTargetTransactionServiceImpl implements IAnalystTargetTransactionService {

    @Autowired
    private TraderMarkAnalystTargetMapper traderMarkAnalystTargetMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer insertBatch(Collection<TraderMarkAnalystTargetModel> analystTargetModelList) {
        return traderMarkAnalystTargetMapper.insertBatch(analystTargetModelList);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer updateBatch(List<TraderMarkAnalystTargetModel> updateList) throws Exception {
        return traderMarkAnalystTargetMapper.updateBatch(updateList);
    }
}
