/**
 * 
 */
package la.niub.abcapi.invest.report.component.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.constant.ReportConstant;
import la.niub.abcapi.invest.report.constant.ReportStatusConstant;
import la.niub.abcapi.invest.report.dao.invest.InvestnewResolvedTaskMapper;
import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.InvestDocOutput;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * @author zhairp createDate: 2019-04-16
 */
@Component
public class ReportRepairJob {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReportService reportService;

	@Autowired
	private ResearchTaskDomainMapper researchTaskDomainMapper;

	@Autowired
	private InvestnewResolvedTaskMapper investnewResolvedTaskMapper;

	@Autowired
	private PlatformService platformService;

	/**
	 * 批量修复研报
	 * 
	 * @author zhairp createDate: 2019-04-15
	 */
	@Scheduled(fixedRate = 1000 * 60 * 3)
	public void repairReports() {
		List<Map<String, Object>> tasks = researchTaskDomainMapper.getResearchTasks(ReportStatusConstant.UPDATE_REPORT_FAILURE, ReportConstant.REPORT_NUM);
		if (!CollectionUtils.isEmpty(tasks)) {
			log.info("批量修复研报开始 start");
			List<ResearchTaskDomain> reports = JSON.parseArray(JSON.toJSONString(tasks), ResearchTaskDomain.class);
			for (ResearchTaskDomain report : reports) {
				CompanySo company = platformService.getCompany(report.getUserId());
				if (null != company && !StringUtils.isEmpty(company.getParentId())) {
					report.setParentId(company.getParentId());
					report.setStatus(ReportStatusConstant.UPDATE_REPORT_SUCCESS);
					researchTaskDomainMapper.updateReportParentId(report);
				}
			}
			log.info("批量修复研报结束 end");
		}
	}

	/**
	 * 批量修复解析
	 * 
	 * @author zhairp createDate: 2019-04-16
	 */
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public void repairAnalysis() {
		List<ResearchTaskDomain> tasks = researchTaskDomainMapper.getResolvedTask(ReportConstant.REPORT_NUM, ReportStatusConstant.ANALYSIS_FAILURE);
		if (!CollectionUtils.isEmpty(tasks)) {
			log.info("批量修复解析开始 start");
			List<Long> sourceIds = new ArrayList<Long>();
			for (ResearchTaskDomain task : tasks) {
				InvestDocOutput doc = reportService.getInvestDocInputById(task.getId(), ReportConstant.MONGO_TEXT_COLLECTION);
				if (null != doc && null != doc.getAlg_document_tags()) {
					log.info("已解析修复doc:{}", doc);
					sourceIds.add(task.getId());
				}
			}
			if (!CollectionUtils.isEmpty(sourceIds)) {
				investnewResolvedTaskMapper.batchUpdateResolvedTaskStatus(sourceIds, ReportStatusConstant.ANALYSIS_INIT);
			}
			log.info("批量修复解析开始 end");
		}
	}

}
