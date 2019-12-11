package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.component.exception.CustomException;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.dao.invest.RecommendedStockRuleMapper;
import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleAddVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleUpdateVo;
import la.niub.abcapi.invest.seller.service.IRecommendedStockRuleService;
import la.niub.abcapi.invest.seller.service.IRecommendedStockRuleTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : ppan
 * @description : 金股提取规则service
 * @date : 2019-01-16 15:53
 */
@Service
public class RecommendedStockRuleServiceImpl implements IRecommendedStockRuleService {

    @Autowired
    private RecommendedStockRuleMapper recommendedStockRuleMapper;

    @Autowired
    private IRecommendedStockRuleTransactionService recommendedStockRuleTransactionService;

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public List<Map<String, String>> getBrokerList(String userId, String keyword, Integer limit) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        return recommendedStockRuleMapper.getBrokerListByParentIdAndKeyword(parentId, keyword, limit);
    }

    @Override
    public List<RecommendedStockRuleModel> getRuleList(String userId, String brokers) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        List<String> brokerList = StringUtil.isEmpty(brokers) ? new ArrayList<>() : Arrays.asList(brokers.split(","));
        return recommendedStockRuleMapper.getRuleListByParentIdAndBrokerList(parentId, brokerList);
    }

    @Override
    public void save(RecommendedStockRuleAddVo ruleAddVo) throws Exception {
        String parentId = userInfoService.getUserParentId(ruleAddVo.getUserId());
        // 查询该券商的规则是否存在
        RecommendedStockRuleModel existRuleModel = recommendedStockRuleMapper.getByParentIdAndBrokerAndSheetName(parentId, ruleAddVo.getBroker(), ruleAddVo.getSheetName());
        if (existRuleModel != null) {
            throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
        } else {
            RecommendedStockRuleModel ruleModel = new RecommendedStockRuleModel();
            ruleModel.setBroker(ruleAddVo.getBroker());
            ruleModel.setSheetName(ruleAddVo.getSheetName());
            ruleModel.setParentId(parentId);
            Date now = new Date();
            ruleModel.setCreateTime(now);
            ruleModel.setUpdateTime(now);
            ruleModel.setCreateId(ruleAddVo.getUserId());
            ruleModel.setUpdateId(ruleAddVo.getUserId());

            recommendedStockRuleTransactionService.save(ruleModel);
        }
    }

    @Override
    public RecommendedStockRuleModel getRuleDetail(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        RecommendedStockRuleModel ruleModel = recommendedStockRuleMapper.selectByPrimaryKey(id);
        if (ruleModel == null || !ruleModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not update");
        }
        return ruleModel;
    }

    @Override
    public void update(RecommendedStockRuleUpdateVo ruleUpdateVo) throws Exception {
        String parentId = userInfoService.getUserParentId(ruleUpdateVo.getUserId());
        RecommendedStockRuleModel existRuleModel = recommendedStockRuleMapper.selectByPrimaryKey(ruleUpdateVo.getId());
        if (existRuleModel == null || !existRuleModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + ruleUpdateVo.getId() + ", no data or other company data not update");
        } else {
            existRuleModel = recommendedStockRuleMapper.getByParentIdAndBrokerAndSheetName(parentId, ruleUpdateVo.getBroker(), ruleUpdateVo.getSheetName());
            if (existRuleModel != null) {
                throw new CustomException(SystemEnumCodeConfig.ERROR_DATA_EXIST);
            }
            RecommendedStockRuleModel ruleModel = new RecommendedStockRuleModel();
            ruleModel.setId(ruleUpdateVo.getId());
            ruleModel.setBroker(ruleUpdateVo.getBroker());
            ruleModel.setSheetName(ruleUpdateVo.getSheetName());
            ruleModel.setUpdateTime(new Date());
            ruleModel.setUpdateId(ruleUpdateVo.getUserId());

            recommendedStockRuleTransactionService.update(ruleModel);
        }
    }

    @Override
    public void delete(String userId, Long id) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        RecommendedStockRuleModel existRuleModel = recommendedStockRuleMapper.selectByPrimaryKey(id);
        if (existRuleModel == null || !existRuleModel.getParentId().equals(parentId)) {
            throw new RuntimeException("id:" + id + ", no data or other company data not update");
        } else {
            recommendedStockRuleTransactionService.delete(id);
        }
    }
}
