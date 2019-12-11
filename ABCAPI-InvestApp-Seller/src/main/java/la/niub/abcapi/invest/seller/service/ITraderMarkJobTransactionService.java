package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.*;

import java.util.List;
import java.util.Map;

public interface ITraderMarkJobTransactionService {
    void save(List<TraderMarkTaskModel> taskModelList,
              Map<String, List<TraderMarkTaskPersonnelModel>> taskPersonnelMap,
              Map<String, List<TraderMarkTaskDetailModel>> taskDetailMap,
              Map<String, List<TraderMarkObjectiveDataDetailModel>> objectiveDataDetailMap,
              Map<String, List<TraderMarkObjectiveDataBrokerModel>> objectiveDataBrokerMap) throws Exception;
}
