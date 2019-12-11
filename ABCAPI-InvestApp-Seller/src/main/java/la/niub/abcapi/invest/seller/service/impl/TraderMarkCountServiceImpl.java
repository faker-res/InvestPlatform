package la.niub.abcapi.invest.seller.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkObjectiveDataBrokerMapper;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkObjectiveDataDetailMapper;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkStatisticsDetailMapper;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkTaskMapper;
import la.niub.abcapi.invest.seller.model.TraderMarkObjectiveDataBrokerModel;
import la.niub.abcapi.invest.seller.model.TraderMarkStatisticsDetailModel;
import la.niub.abcapi.invest.seller.model.TraderMarkTaskModel;
import la.niub.abcapi.invest.seller.service.ITraderMarkCountService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;

/**
 * 券商评分统计service
 * 
 * @author zhairp createDate: 2019-02-13
 */
@Service
public class TraderMarkCountServiceImpl implements ITraderMarkCountService {

	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private TraderMarkObjectiveDataDetailMapper traderMarkObjectiveDataDetailMapper;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private TraderMarkStatisticsDetailMapper traderMarkStatisticsDetailMapper;

	@Autowired
	private TraderMarkObjectiveDataBrokerMapper traderMarkObjectiveDataBrokerMapper;

	@Autowired
	private TraderMarkTaskMapper traderMarkTaskMapper;

	@Override
	public List<Map<String, Object>> getBrokerRank(String userId, Long taskId) throws Exception {
		String parentId = userInfoService.getUserParentId(userId);
		List<Map<String, Object>> list = traderMarkStatisticsDetailMapper.getBrokerRankByTaskId(taskId);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			map.put("rownum", i + 1);
			String broker = map.get("broker").toString();
			List<Map<String, Object>> brokerScoreList = traderMarkStatisticsDetailMapper.getBrokerSevenTotalScoreByBroker(parentId, broker);
			Collections.reverse(brokerScoreList);

			BigDecimal totalScore = new BigDecimal(map.get("totalScore").toString());
			BigDecimal comparePrevious = totalScore;
			Long recentTaskId = null;
			if (brokerScoreList.size() >= 2) {
				BigDecimal previousScore = new BigDecimal(brokerScoreList.get(brokerScoreList.size() - 2).get("totalScore").toString());
				comparePrevious = totalScore.subtract(previousScore).setScale(2, BigDecimal.ROUND_HALF_UP);

				recentTaskId = Long.valueOf(brokerScoreList.get(brokerScoreList.size() - 2).get("taskId").toString());
			}

			List<Map<String, Object>> currentDimensionScoreList = traderMarkStatisticsDetailMapper.getDimensionScoreByTaskIdAndBroker(taskId, broker, true);
			List<Map<String, Object>> recentDimensionScoreList = traderMarkStatisticsDetailMapper.getDimensionScoreByTaskIdAndBroker(recentTaskId, broker, false);
			Map<String, BigDecimal> recentDimensionScoreMap = new HashMap<>();
			recentDimensionScoreList.forEach(item -> recentDimensionScoreMap.put(item.get("dimension").toString(), new BigDecimal(item.get("score").toString())));
			currentDimensionScoreList.forEach(item -> {
				String dimension = item.get("dimension").toString();
				BigDecimal currentDimensionScore =  new BigDecimal(item.get("score").toString());
				BigDecimal recentDimensionScore = recentDimensionScoreMap.getOrDefault(dimension, new BigDecimal("0"));
				BigDecimal compare = currentDimensionScore.subtract(recentDimensionScore);
				item.put("compare", compare);
			});

			Map<String,Object> comprehensiveMap = new HashMap<>();
			comprehensiveMap.put("charts", brokerScoreList);
			comprehensiveMap.put("comparePrevious", comparePrevious);
			comprehensiveMap.put("dimensionScores", currentDimensionScoreList);

			map.put("comprehensive", comprehensiveMap);
		}
		return list;
	}

	@Cacheable(value = "indexCache")
	@Override
	public List<String> getSubjectIndexs(Long taskId) {
		return traderMarkObjectiveDataDetailMapper.getSubjectIndexs(taskId);
	}

	@CacheEvict(value = "indexCache")
	@Override
	public void reload() {
	}

	@Override
	public List<Map<String, Object>> getTaskList(String userId) throws Exception {
		String parentId = userInfoService.getUserParentId(userId);
		List<Map<String, Object>> result = traderMarkObjectiveDataDetailMapper.getTaskList(parentId);
		BigInteger lastestTaskId = traderMarkObjectiveDataDetailMapper.getLastestTask(parentId);
		if (!CollectionUtils.isEmpty(result)) {
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> index = result.get(i);
				BigInteger taskId = (BigInteger) index.get("taskId");
				if (taskId.equals(lastestTaskId)) {
					index.put("default", 1);
					break;
				}
			}
			Calendar calendar = Calendar.getInstance();
			int nowYear = calendar.get(Calendar.YEAR);
			Map<String, Object> lastestTask = result.get(result.size() - 1);
			int lastestTaskYeak = (int) lastestTask.get("year");
			int lastestTaskQuarter = (int) lastestTask.get("quarter");
			if (nowYear > lastestTaskYeak) {
				for (int i = lastestTaskQuarter + 1; i <= 4; i++) {
					Map<String, Object> newIndex=new HashMap<String, Object>();
					newIndex.put("taskId", null);
					newIndex.put("year", lastestTaskYeak);
					newIndex.put("quarter", i);
					newIndex.put("status", 0);
					result.add(newIndex);
				}
				for (int j = 1; j <= 4; j++) {
					Map<String, Object> newIndex2=new HashMap<String, Object>();
					newIndex2.put("taskId", null);
					newIndex2.put("year", nowYear);
					newIndex2.put("quarter", j);
					newIndex2.put("status", 0);
					result.add(newIndex2);
				}
			} else {
				for (int k = lastestTaskQuarter + 1; k <= 4; k++) {
					Map<String, Object> newIndex3=new HashMap<String, Object>();
					newIndex3.put("taskId", null);
					newIndex3.put("year", lastestTaskYeak);
					newIndex3.put("quarter", k);
					newIndex3.put("status", 0);
					result.add(newIndex3);
				}
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerIndustryAnalystCount(Long taskId, String broker) throws Exception {
		return traderMarkStatisticsDetailMapper.getBrokerIndustryAnalystCount(taskId, broker);
	}

	@Override
	public Map<String, Object> getBrokerDetail(Long taskId, String broker, String industry, String analysts) throws Exception {
		List<String> analystList = new ArrayList<>();
		if (!StringUtil.isEmpty(analysts)) {
			analystList = Arrays.asList(analysts.split(","));
		}
		List<TraderMarkStatisticsDetailModel> list = traderMarkStatisticsDetailMapper.getBrokerDetail(taskId, broker, industry, analystList);

		Set<String> dimensionList = new LinkedHashSet<>();
		List<Map<String, Object>> analystScoreList = new ArrayList<>();
		Map<String, Map<String, BigDecimal>> analystScoreMap = new LinkedHashMap<>();
		Map<String, Integer> dimensionCalculateStatusMap = new HashMap<>();
		list.forEach(item -> {
			dimensionList.add(item.getDimension());
			dimensionCalculateStatusMap.put(item.getDimension(), item.getCalculateStatus());

			String key = item.getIndustry() + "-" + item.getAnalyst();
			Map<String, BigDecimal> dimensionScoreMap = analystScoreMap.getOrDefault(key, new LinkedHashMap<>());
			dimensionScoreMap.put(item.getDimension(), item.getScore());
			analystScoreMap.put(key, dimensionScoreMap);

		});
		analystScoreMap.forEach((key, value) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("analyst", key.split("-")[1]);
			map.put("dimensionScore", value);

			BigDecimal totalScore = new BigDecimal("0");
			for (Map.Entry<String, BigDecimal> entry : value.entrySet()) {
				if (dimensionCalculateStatusMap.get(entry.getKey()) != null && dimensionCalculateStatusMap.get(entry.getKey()) == 0) {
					totalScore = totalScore.add(entry.getValue());
				}
			}
			map.put("totalScore", totalScore);

			analystScoreList.add(map);
		});

		Map<String, Object> result = new HashMap<>();
		result.put("dimensionList", dimensionList);
		result.put("analystScoreList", analystScoreList);
		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerAnalyst(Long taskId, String broker, String industry, String keyword) throws Exception {
		return traderMarkStatisticsDetailMapper.getBrokerAnalyst(taskId, broker, industry, keyword);
	}

	@Override
	public List<Map<String, Object>> getBrokerRoadShowRank(String userId, String keyword) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<String> brokerList = traderMarkObjectiveDataBrokerMapper.getBrokerListByParentIdAndKeyword(parentId, keyword);
		if (brokerList == null || brokerList.isEmpty()) {
			return result;
		}
		Long recentTaskId = traderMarkTaskMapper.getRecentFinishedTaskIdByParentId(parentId);
		List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerList = traderMarkObjectiveDataBrokerMapper.getBrokerInfoListByTaskId(recentTaskId);
		Map<String, Integer> objectiveDataBrokerMap = objectiveDataBrokerList.stream().collect(Collectors.toMap(TraderMarkObjectiveDataBrokerModel::getBroker, TraderMarkObjectiveDataBrokerModel::getRoadShowCount, (k1, k2) -> k2));

		brokerList.forEach(broker -> {
			Map<String, Object> map = new HashMap<>();
			map.put("broker", broker);
			map.put("roadShowCount", objectiveDataBrokerMap.getOrDefault(broker, 0));

			result.add(map);
		});

		result.sort((o1, o2) -> Integer.valueOf(o2.get("roadShowCount").toString()).compareTo(Integer.valueOf(o1.get("roadShowCount").toString())));
		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerRoadShowChart(String userId, String broker) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<TraderMarkTaskModel> taskList = traderMarkTaskMapper.getRecentSevenTaskIdByParentId(parentId);
		if (taskList == null || taskList.isEmpty()) {
			return result;
		}
		Collections.reverse(taskList);
		List<Long> taskIdList = new ArrayList<>();
		taskList.forEach(item -> taskIdList.add(item.getId()));

		List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerList = traderMarkObjectiveDataBrokerMapper.getBrokerInfoListByTaskIdListAndBroker(taskIdList, broker);
		Map<Long, Integer> brokerRoadShowCountMap = objectiveDataBrokerList.stream().collect(Collectors.toMap(TraderMarkObjectiveDataBrokerModel::getTaskId, TraderMarkObjectiveDataBrokerModel::getRoadShowCount, (k1, k2) -> k2));
		taskList.forEach(item -> {
			Map<String, Object> map = new HashMap<>();
			map.put("year", item.getYear());
			map.put("quarter", item.getQuarter());
			map.put("roadShowCount", brokerRoadShowCountMap.getOrDefault(item.getId(), 0));

			result.add(map);
		});

		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerAttainDaysChart(String userId) throws Exception {
		String parentId = userInfoService.getUserParentId(userId);
		return traderMarkObjectiveDataBrokerMapper.getBrokerRecentAttainDaysInfoByParentId(parentId);
	}

	@Override
	public List<Map<String, Object>> getBrokerReportRank(String userId, String keyword) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<String> brokerList = traderMarkObjectiveDataBrokerMapper.getBrokerListByParentIdAndKeyword(parentId, keyword);
		if (brokerList == null || brokerList.isEmpty()) {
			return result;
		}
		Long recentTaskId = traderMarkTaskMapper.getRecentFinishedTaskIdByParentId(parentId);
		List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerList = traderMarkObjectiveDataBrokerMapper.getBrokerInfoListByTaskId(recentTaskId);
		Map<String, Integer> objectiveDataBrokerMap = objectiveDataBrokerList.stream().collect(Collectors.toMap(TraderMarkObjectiveDataBrokerModel::getBroker, TraderMarkObjectiveDataBrokerModel::getReportCount, (k1, k2) -> k2));

		brokerList.forEach(broker -> {
			Map<String, Object> map = new HashMap<>();
			map.put("broker", broker);
			map.put("reportCount", objectiveDataBrokerMap.getOrDefault(broker, 0));

			result.add(map);
		});

		result.sort((o1, o2) -> Integer.valueOf(o2.get("reportCount").toString()).compareTo(Integer.valueOf(o1.get("reportCount").toString())));
		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerReportChart(String userId, String broker) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<TraderMarkTaskModel> taskList = traderMarkTaskMapper.getRecentSevenTaskIdByParentId(parentId);
		if (taskList == null || taskList.isEmpty()) {
			return result;
		}
		Collections.reverse(taskList);
		List<Long> taskIdList = new ArrayList<>();
		taskList.forEach(item -> taskIdList.add(item.getId()));

		List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerList = traderMarkObjectiveDataBrokerMapper.getBrokerInfoListByTaskIdListAndBroker(taskIdList, broker);
		Map<Long, String> brokerReportMap = new HashMap<>();
		objectiveDataBrokerList.forEach(item -> brokerReportMap.put(item.getTaskId(), item.getReportCount() + "-" + item.getReadCount()));
		taskList.forEach(item -> {
			Map<String, Object> map = new HashMap<>();
			map.put("year", item.getYear());
			map.put("quarter", item.getQuarter());
			String value = brokerReportMap.getOrDefault(item.getId(), "0-0");
			map.put("reportCount", Integer.valueOf(value.split("-")[0]));
			map.put("readCount", Integer.valueOf(value.split("-")[1]));

			result.add(map);
		});

		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerScoreRank(String userId, String keyword) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<String> brokerList = traderMarkObjectiveDataBrokerMapper.getBrokerListByParentIdAndKeyword(parentId, keyword);
		if (brokerList == null || brokerList.isEmpty()) {
			return result;
		}
		Long recentTaskId = traderMarkTaskMapper.getRecentFinishedTaskIdByParentId(parentId);

		List<Map<String, Object>> brokerScoreList = traderMarkStatisticsDetailMapper.getBrokerScoreByTaskId(recentTaskId);
		Map<String, BigDecimal> brokerScoreMap = new HashMap<>();
		brokerScoreList.forEach(item -> brokerScoreMap.put(item.get("broker").toString(), new BigDecimal(item.get("totalScore").toString())));

		brokerList.forEach(broker -> {
			Map<String, Object> map = new HashMap<>();
			map.put("broker", broker);
			map.put("totalScore", brokerScoreMap.getOrDefault(broker, new BigDecimal("0")));

			result.add(map);
		});

		result.sort((o1, o2) -> new BigDecimal(o2.get("totalScore").toString()).compareTo(new BigDecimal(o1.get("totalScore").toString())));
		return result;
	}

	@Override
	public List<Map<String, Object>> getBrokerScoreChart(String userId, String broker) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();

		String parentId = userInfoService.getUserParentId(userId);
		List<TraderMarkTaskModel> taskList = traderMarkTaskMapper.getRecentSevenTaskIdByParentId(parentId);
		if (taskList == null || taskList.isEmpty()) {
			return result;
		}
		Collections.reverse(taskList);
		List<Long> taskIdList = new ArrayList<>();
		taskList.forEach(item -> taskIdList.add(item.getId()));

		List<Map<String, Object>> brokerScoreList = traderMarkStatisticsDetailMapper.getBrokerScoreByTaskIdListAndBroker(taskIdList, broker);
		List<Map<String, Object>> taskAverageScoreList = traderMarkStatisticsDetailMapper.getTaskAverageScoreByTaskIdList(taskIdList);
		Map<Long, BigDecimal> brokerScoreMap = new HashMap<>();
		brokerScoreList.forEach(item -> brokerScoreMap.put(Long.valueOf(item.get("taskId").toString()), new BigDecimal(item.get("totalScore").toString())));
		Map<Long, BigDecimal> taskAverageScoreMap = new HashMap<>();
		taskAverageScoreList.forEach(item -> taskAverageScoreMap.put(Long.valueOf(item.get("taskId").toString()), new BigDecimal(item.get("totalScore").toString())));
		taskList.forEach(item -> {
			Map<String, Object> map = new HashMap<>();
			map.put("year", item.getYear());
			map.put("quarter", item.getQuarter());
			map.put("totalScore", brokerScoreMap.getOrDefault(item.getId(), new BigDecimal("0")));
			map.put("averageScore", taskAverageScoreMap.getOrDefault(item.getId(), new BigDecimal("0")));

			result.add(map);
		});

		return result;
	}
}
