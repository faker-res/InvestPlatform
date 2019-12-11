package la.niub.abcapi.invest.seller.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhairp createDate: 2019-02-13
 */
public interface ITraderMarkCountService {

	List<Map<String, Object>> getBrokerRank(String userId, Long taskId) throws Exception;

	List<String> getSubjectIndexs(Long taskId);

	void reload();

	List<Map<String, Object>> getTaskList(String userId) throws Exception;

	List<Map<String, Object>> getBrokerIndustryAnalystCount(Long taskId, String broker) throws Exception;

	Map<String, Object> getBrokerDetail(Long taskId, String broker, String industry, String analysts) throws Exception;

	List<Map<String, Object>> getBrokerAnalyst(Long taskId, String broker, String industry, String keyword) throws Exception;

	List<Map<String, Object>> getBrokerRoadShowRank(String userId, String keyword) throws Exception;

	List<Map<String, Object>> getBrokerRoadShowChart(String userId, String broker) throws Exception;

	List<Map<String, Object>> getBrokerAttainDaysChart(String userId) throws Exception;

	List<Map<String, Object>> getBrokerReportRank(String userId, String keyword) throws Exception;

	List<Map<String, Object>> getBrokerReportChart(String userId, String broker) throws Exception;

	List<Map<String, Object>> getBrokerScoreRank(String userId, String keyword) throws Exception;

	List<Map<String, Object>> getBrokerScoreChart(String userId, String broker) throws Exception;
}
