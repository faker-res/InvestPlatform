package la.niub.abcapi.invest.seller.service;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.model.vo.MailReportSearchVo;

import java.util.List;
import java.util.Map;

public interface ISellerReportService {
    JSONObject mailReport(JSONObject jsonObject) throws Exception;

    List<String> getSelfStockList(String userId) throws Exception;

    Map<String, Object> mailFilter(String userId) throws Exception;

    List<Map<String, Object>> getHotIndustry(String userId) throws Exception;

    List<Map<String, Object>> getHotCompany(String userId) throws Exception;

    JSONObject getReportDetail(String reportId, String userId) throws Exception;

    JSONObject getRelatedReport(JSONObject jsonObject) throws Exception;

    JSONObject search(MailReportSearchVo mailReportSearchVo) throws Exception;

    Map<String, Object> getCharts(String reportId) throws Exception;

    void saveReadLog(String userId, String type, String reportId, String author) throws Exception;
}
