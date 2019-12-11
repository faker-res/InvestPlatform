package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.*;

import java.util.List;

public interface ITraderMarkConfigTransactionService {
    Integer saveMode(TraderMarkConfigModeModel configModeModel) throws Exception;

    Integer updateMode(TraderMarkConfigModeModel configModeModel) throws Exception;

    Integer saveDimension(TraderMarkConfigDimensionModel configDimensionModel) throws Exception;

    Integer updateDimension(TraderMarkConfigDimensionModel configDimensionModel) throws Exception;

    Integer deleteDimension(Long id) throws Exception;

    Integer saveOrUpdateObject(List<TraderMarkConfigObjectModel> configObjectModelList, String parentId, Integer option) throws Exception;

    Integer saveWeight(TraderMarkConfigWeightModel configWeightModel) throws Exception;

    Integer updateWeight(TraderMarkConfigWeightModel configWeightModel) throws Exception;

    Integer deleteWeight(Long id) throws Exception;
}
