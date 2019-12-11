package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.TraderMarkAnalystTargetModel;

import java.util.Collection;
import java.util.List;

public interface IAnalystTargetTransactionService {
    Integer insertBatch(Collection<TraderMarkAnalystTargetModel> analystTargetModelList) throws Exception;

    Integer updateBatch(List<TraderMarkAnalystTargetModel> updateList) throws Exception;
}
