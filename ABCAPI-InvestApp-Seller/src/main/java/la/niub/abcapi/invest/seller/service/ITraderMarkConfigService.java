package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigDimensionResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigObjectResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigWeightResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigWeightUserInfoResponse;
import la.niub.abcapi.invest.seller.model.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ITraderMarkConfigService {
    Map<String, Object> getModeList(String userId) throws Exception;

    void saveOrUpdateMode(TraderMarkConfigModeAddVo configModeSaveVo) throws Exception;

    List<TraderMarkConfigDimensionResponse> getDimensionList(String userId) throws Exception;

    void saveDimension(TraderMarkConfigDimensionAddVo configDimensionAddVo) throws Exception;

    TraderMarkConfigDimensionResponse getDimensionDetail(String userId, Long id) throws Exception;

    void updateDimension(TraderMarkConfigDimensionUpdateVo configDimensionUpdateVo) throws Exception;

    void deleteDimension(String userId, Long id) throws Exception;

    void uploadObject(MultipartFile file, String userId) throws Exception;

    List<Map<String, Object>> getObjectBrokerList(String userId, String keyword, Integer limit) throws Exception;

    List<TraderMarkConfigObjectResponse> getObjectList(String userId, String brokers) throws Exception;

    List<TraderMarkConfigWeightResponse> getWeightList(String userId) throws Exception;

    List<TraderMarkConfigWeightUserInfoResponse> getUserInfoList(String userId, String keyword, Integer limit) throws Exception;

    void saveWeight(TraderMarkConfigWeightAddVo configWeightAddVo) throws Exception;

    TraderMarkConfigWeightResponse getWeightDetail(String userId, Long id) throws Exception;

    void updateWeight(TraderMarkConfigWeightUpdateVo configWeightUpdateVo) throws Exception;

    void deleteWeight(String userId, Long id) throws Exception;
}
