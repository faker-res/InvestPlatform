package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.RoadShowBrokerAnalystModel;
import la.niub.abcapi.invest.seller.model.RoadShowModel;
import la.niub.abcapi.invest.seller.model.RoadShowToBuyerModel;
import la.niub.abcapi.invest.seller.model.vo.RoadShowAddVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowBuyerVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowCompanyVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowSellerVo;

import java.util.List;

public interface IRoadShowManageTransactionService {
    int joinTrip(RoadShowToBuyerModel roadShowToBuyerModel) throws Exception;

    int cancelTrip(Long roadShowId, String userId) throws Exception;

    int save(RoadShowModel roadShowModel, RoadShowAddVo roadShowAddVo) throws Exception;

    int update(RoadShowModel roadShowModel, List<RoadShowBuyerVo> buyers, List<RoadShowCompanyVo> companys, List<RoadShowSellerVo> sellers) throws Exception;

    int delete(Long roadShowId) throws Exception;

    int updateRoadShowById(RoadShowModel roadShowDomain) throws Exception;

    int saveOrUpdateSellerAnalyst(RoadShowBrokerAnalystModel roadShowBrokerAnalystModel) throws Exception;

    int insertBatchRoadShowBrokerAnalyst(String parentId, List<RoadShowBrokerAnalystModel> brokerAnalystModelList) throws Exception;
}
