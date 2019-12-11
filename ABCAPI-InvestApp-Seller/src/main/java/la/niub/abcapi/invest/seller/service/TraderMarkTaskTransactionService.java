package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.TraderMarkStatisticsDetailModel;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveItemVo;

import java.util.List;

public interface TraderMarkTaskTransactionService {
    Integer saveScore(Long taskId, String userId, List<TraderMarkTaskSaveItemVo> scoreList, List<TraderMarkStatisticsDetailModel> statisticsDetailModelList) throws Exception;
}
