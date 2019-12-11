package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskBrokerResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskListResponse;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public interface ITraderMarkTaskService {
    Map<Integer, List<TraderMarkTaskListResponse>> getTaskList(String userId) throws Exception;

    List<TraderMarkTaskBrokerResponse> getTaskBroker(String userId, Long taskId, Integer status) throws Exception;

    Map<String, Object> getIndustryInfo(String userId, Long taskId, String broker) throws Exception;

    Map<String, Object> getTaskBrokerDetail(String userId, Long taskId, String broker, String industry) throws Exception;

    void saveScore(TraderMarkTaskSaveVo saveVo) throws Exception;

    String getNextIncompleteBroker(Long taskId, String userId) throws Exception;

    Map<String, Object> getBrokerRate(String userId, String broker, Long taskId) throws Exception;

    Map<String, Object> getBrokerTotalScoreChart(String userId, String broker) throws Exception;

    List<Map<String, Object>> getBrokerRoadShow(String userId, Long taskId, String broker, String industry, String analyst) throws Exception;

    Map<String, Object> getStockMarket(String userId, Long taskId, String stockCode, String stockName, String broker, String industry, String analyst) throws Exception;

    void download(ByteArrayOutputStream bos, String userId, Long taskId) throws Exception;

    void upload(MultipartFile file, String userId, Long taskId) throws Exception;
}
