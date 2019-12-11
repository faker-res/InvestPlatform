/**
 * 
 */
package la.niub.abcapi.invest.seller.controller;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import la.niub.abcapi.invest.seller.dao.invest.TraderMarkObjectiveDataDetailMapper;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.ITraderMarkCountService;

/**
 * 券商统计
 * 
 * @author zhairp createDate: 2019-01-30
 */
@RestController
@RequestMapping("tradermark/count")
@Validated
public class TraderMarkCountController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TraderMarkObjectiveDataDetailMapper traderMarkObjectiveDataDetailMapper;

	@Autowired
	private ITraderMarkCountService traderMarkCountService;

	/**
	 * 客观数据
	 * 
	 * @author zhairp createDate: 2019-01-30
	 * @param        quota:report_count,read_count,road_show_count,attain_days,attain_probability
	 * @param taskId
	 * @return
	 */
	@GetMapping("getObjectiveData")
	public Response getObjectiveData(@NotBlank(message = "参数[quota]不能为空") String quota,@NotNull(message = "参数[taskId]不能为空") Long taskId) {
		log.info("quota:{},taskId:{}", quota, taskId);
		return new Response(traderMarkObjectiveDataDetailMapper.getObjectiveData(quota, taskId));
	}

	/**
	 * 主观指标
	 * 
	 * @author zhairp createDate: 2019-01-31
	 * @return
	 */
	@GetMapping("getSubjectIndexs")
	public Response getSubjectIndexs(@NotNull(message = "参数[taskId]不能为空") Long taskId) {
		return new Response(traderMarkCountService.getSubjectIndexs(taskId));
	}

	/**
	 * 主观数据
	 * 
	 * @author zhairp createDate: 2019-01-31
	 * @param        quota:债券研究,公司研究,股票研究,行业研究
	 * @param taskId
	 * @return
	 */
	@GetMapping("getSubjectData")
	public Response getSubjectData(@NotBlank(message = "参数[quota]不能为空") String quota,@NotNull(message = "参数[taskId]不能为空") Long taskId) {
		return new Response(traderMarkObjectiveDataDetailMapper.getSubjectData(quota, taskId));
	}

	/**
	 * 券商得分搜索
	 * @param userId
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/score/rank")
	public Response getBrokerScoreRank(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword) throws Exception {
		return new Response(traderMarkCountService.getBrokerScoreRank(userId, keyword));
	}

	/**
	 * 券商研报搜索
	 * @param userId
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/report/rank")
	public Response getBrokerReportRank(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword) throws Exception {
		return new Response(traderMarkCountService.getBrokerReportRank(userId, keyword));
	}

	/**
	 * 券商排名
	 * @param userId
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/rank")
	public Response getBrokerRank(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[taskId]不能为空") Long taskId) throws Exception {
		return new Response(traderMarkCountService.getBrokerRank(userId, taskId));
	}
	
		/**
	 * 季度任务列表
	 *@author zhairp createDate: 2019-02-13
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("getTaskList")
	public Response getTaskList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
		return new Response(traderMarkCountService.getTaskList(userId));
	}

	/**
	 * 研报tab-图表
	 * @param userId
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/report/chart")
	public Response getBrokerReportChart(@NotBlank(message = "参数[userId]不能为空") String userId, @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
		return new Response(traderMarkCountService.getBrokerReportChart(userId, broker));
	}

	/**
	 * 得分tab-图表
	 * @param userId
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/score/chart")
	public Response getBrokerScoreChart(@NotBlank(message = "参数[userId]不能为空") String userId, @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
		return new Response(traderMarkCountService.getBrokerScoreChart(userId, broker));
	}
	
	/**
	 * 券商行业列表
	 * 
	 * @author zhairp createDate: 2019-02-14
	 * @return
	 */
	@GetMapping("getIndustries")
	public Response getIndustries(@NotNull(message = "参数[taskId]不能为空") Long taskId, @NotBlank(message = "参数[broker]不能为空") String broker) {
		return new Response(traderMarkObjectiveDataDetailMapper.getIndustries(broker, taskId));
	}

	/**
	 * 获取券商行业研究员数量
	 * @param taskId
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/industry")
	public Response getBrokerIndustryInfo(@NotNull(message = "参数[taskId]不能为空") Long taskId, @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
		return new Response(traderMarkCountService.getBrokerIndustryAnalystCount(taskId, broker));
	}

	/**
	 * 获取券商研究员得分详情
	 * @param taskId
	 * @param broker
	 * @param industry
	 * @param analysts 多个用逗号隔开
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/detail")
	public Response getBrokerDetail(@NotNull(message = "参数[taskId]不能为空") Long taskId, @NotBlank(message = "参数[broker]不能为空") String broker, String industry, String analysts) throws Exception {
		return new Response(traderMarkCountService.getBrokerDetail(taskId, broker, industry, analysts));
	}

	/**
	 * 获取券商研究员
	 * @param taskId
	 * @param broker
	 * @param industry
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/analyst")
	public Response getBrokerAnalyst(@NotNull(message = "参数[taskId]不能为空") Long taskId, @NotBlank(message = "参数[broker]不能为空") String broker, String industry, String keyword) throws Exception {
		return new Response(traderMarkCountService.getBrokerAnalyst(taskId, broker, industry, keyword));
	}

	/**
	 * 券商路演搜索
	 * @param userId
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/roadshow/rank")
	public Response getBrokerRoadShowRank(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword) throws Exception {
		return new Response(traderMarkCountService.getBrokerRoadShowRank(userId, keyword));
	}

	/**
	 * 券商路演图表
	 * @param userId
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/roadshow/chart")
	public Response getBrokerRoadShowChart(@NotBlank(message = "参数[userId]不能为空") String userId, @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
		return new Response(traderMarkCountService.getBrokerRoadShowChart(userId, broker));
	}

	/**
	 * 券商推票图表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/broker/attaindays")
	public Response getBrokerAttainDaysChart(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
		return new Response(traderMarkCountService.getBrokerAttainDaysChart(userId));
	}
	
}
