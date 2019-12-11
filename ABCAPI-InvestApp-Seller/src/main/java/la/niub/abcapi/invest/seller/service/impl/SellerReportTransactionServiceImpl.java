package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.ReadLogMapper;
import la.niub.abcapi.invest.seller.model.ReadLogModel;
import la.niub.abcapi.invest.seller.service.ISellerReportTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ppan
 * @description : 卖方研报增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-02-22 09:46
 */
@Service
public class SellerReportTransactionServiceImpl implements ISellerReportTransactionService {

    @Autowired
    private ReadLogMapper readLogMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int saveReadLog(ReadLogModel readLogModel) throws Exception {
        return readLogMapper.insertSelective(readLogModel);
    }
}
