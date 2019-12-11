package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.RoadShowBrokerAnalystModel;
import la.niub.abcapi.invest.seller.model.RoadShowModel;
import la.niub.abcapi.invest.seller.model.RoadShowToBuyerModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowAddVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowBuyerVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowCompanyVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowSellerVo;
import la.niub.abcapi.invest.seller.service.IRoadShowManageTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : ppan
 * @description : 路演管理增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-02-11 16:37
 */
@Service
public class RoadShowManageTransactionServiceImpl implements IRoadShowManageTransactionService {

    @Autowired
    private RoadShowMapper roadShowMapper;

    @Autowired
    private RoadShowToBuyerMapper roadShowToBuyerMapper;

    @Autowired
    private RoadShowToCompanyMapper roadShowToCompanyMapper;

    @Autowired
    private RoadShowBrokerAnalystMapper roadShowBrokerAnalystMapper;

    @Autowired
    private RoadShowToSellerMapper roadShowToSellerMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int save(RoadShowModel roadShowModel, RoadShowAddVo roadShowAddVo) throws Exception {
        int result1 = roadShowMapper.insertSelective(roadShowModel);
        if (!CollectionUtils.isEmpty(roadShowAddVo.getBuyers())) {
            int result2 = roadShowToBuyerMapper.insertBatch(roadShowModel.getId(), roadShowAddVo.getBuyers());
            result1 += result2;
        }
        if (!CollectionUtils.isEmpty(roadShowAddVo.getCompanys())) {
            int result3 = roadShowToCompanyMapper.insertBatch(roadShowModel.getId(), roadShowAddVo.getCompanys());
            result1 += result3;
        }

        if (!CollectionUtils.isEmpty(roadShowAddVo.getSellers())) {
            int result4 = roadShowToSellerMapper.insertBatch(roadShowModel.getId(), roadShowAddVo.getSellers(), roadShowAddVo.getSellerCompanyId(), roadShowAddVo.getSellerCompanyName());
            result1 += result4;
        }
        return result1;
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int joinTrip(RoadShowToBuyerModel roadShowToBuyerModel) throws Exception {
        return roadShowToBuyerMapper.insertSelective(roadShowToBuyerModel);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int cancelTrip(Long roadShowId, String userId) throws Exception {
        return roadShowToBuyerMapper.deleteByRoadShowIdAndUserId(roadShowId, userId);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int update(RoadShowModel roadShowModel, List<RoadShowBuyerVo> buyers, List<RoadShowCompanyVo> companys, List<RoadShowSellerVo> sellers) throws Exception {
        int result1 = roadShowMapper.updateByPrimaryKeySelective(roadShowModel);
        int result2 = roadShowToBuyerMapper.deleteByRoadShowId(roadShowModel.getId());
        int result3 = 0;
        if (!CollectionUtils.isEmpty(buyers)) {
            result3 = roadShowToBuyerMapper.insertBatch(roadShowModel.getId(), buyers);
        }
        int result4 = roadShowToCompanyMapper.deleteByRoadShowId(roadShowModel.getId());
        int result6 = roadShowToSellerMapper.deleteByRoadShowId(roadShowModel.getId());
        int result7 = 0;
        if (!CollectionUtils.isEmpty(sellers)) {
            result7 = roadShowToSellerMapper.insertBatch(roadShowModel.getId(), sellers, roadShowModel.getSellerCompanyId(), roadShowModel.getSellerCompanyName());
        }
        result1 = result1 + result2 + result3 + result4 + result6 + result7;
        if (!CollectionUtils.isEmpty(companys)) {
            int result5 = roadShowToCompanyMapper.insertBatch(roadShowModel.getId(), companys);
            result1 += result5;
        }

        return result1;
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int delete(Long roadShowId) throws Exception {
        int result1 = roadShowMapper.deleteByPrimaryKey(roadShowId);
        int result2 = roadShowToBuyerMapper.deleteByRoadShowId(roadShowId);
        int result3 = roadShowToCompanyMapper.deleteByRoadShowId(roadShowId);
        int result4 = roadShowToSellerMapper.deleteByRoadShowId(roadShowId);
        return result1 + result2 + result3 + result4;
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int updateRoadShowById(RoadShowModel roadShowDomain) throws Exception {
        return roadShowMapper.updateByPrimaryKeySelective(roadShowDomain);
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int saveOrUpdateSellerAnalyst(RoadShowBrokerAnalystModel roadShowBrokerAnalystModel) throws Exception {
        if (roadShowBrokerAnalystModel.getId() != null) {
            return roadShowBrokerAnalystMapper.updateByPrimaryKeySelective(roadShowBrokerAnalystModel);
        } else {
            return roadShowBrokerAnalystMapper.insertSelective(roadShowBrokerAnalystModel);
        }
    }

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public int insertBatchRoadShowBrokerAnalyst(String parentId, List<RoadShowBrokerAnalystModel> brokerAnalystModelList) throws Exception {
        int result1 = roadShowBrokerAnalystMapper.deleteByParentIdAndSource(parentId, 1);
        int result2 = roadShowBrokerAnalystMapper.insertBatch(brokerAnalystModelList);
        return result1 + result2;
    }
}
