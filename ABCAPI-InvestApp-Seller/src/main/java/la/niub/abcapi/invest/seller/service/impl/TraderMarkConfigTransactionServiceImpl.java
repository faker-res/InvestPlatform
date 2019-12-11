package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.service.ITraderMarkConfigTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : ppan
 * @description : 券商评分设置增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-01-19 15:53
 */
@Service
public class TraderMarkConfigTransactionServiceImpl implements ITraderMarkConfigTransactionService {

    @Autowired
    private TraderMarkConfigModeMapper traderMarkConfigModeMapper;

    @Autowired
    private TraderMarkConfigDimensionMapper traderMarkConfigDimensionMapper;

    @Autowired
    private TraderMarkConfigObjectMapper traderMarkConfigObjectMapper;

    @Autowired
    private TraderMarkConfigWeightMapper traderMarkConfigWeightMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer saveMode(TraderMarkConfigModeModel configModeModel) throws Exception {
        return traderMarkConfigModeMapper.insertSelective(configModeModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer updateMode(TraderMarkConfigModeModel configModeModel) throws Exception {
        return traderMarkConfigModeMapper.updateByPrimaryKeySelective(configModeModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer saveDimension(TraderMarkConfigDimensionModel configDimensionModel) throws Exception {
        return traderMarkConfigDimensionMapper.insertSelective(configDimensionModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer updateDimension(TraderMarkConfigDimensionModel configDimensionModel) throws Exception {
        return traderMarkConfigDimensionMapper.updateByPrimaryKeySelective(configDimensionModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer deleteDimension(Long id) throws Exception {
        return traderMarkConfigDimensionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer saveOrUpdateObject(List<TraderMarkConfigObjectModel> configObjectModelList, String parentId, Integer option) throws Exception {
        int result1 = 0;
        if (option == 1) {
            result1 = traderMarkConfigObjectMapper.deleteByParentId(parentId);
        }
        int result2 = 0;
        if (configObjectModelList != null && configObjectModelList.size() > 0) {
            result2 = traderMarkConfigObjectMapper.insertBatch(configObjectModelList);
        }
        return result1 + result2;
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer saveWeight(TraderMarkConfigWeightModel configWeightModel) throws Exception {
        return traderMarkConfigWeightMapper.insertSelective(configWeightModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer updateWeight(TraderMarkConfigWeightModel configWeightModel) throws Exception {
        return traderMarkConfigWeightMapper.updateByPrimaryKeySelective(configWeightModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer deleteWeight(Long id) throws Exception {
        return traderMarkConfigWeightMapper.deleteByPrimaryKey(id);
    }
}
