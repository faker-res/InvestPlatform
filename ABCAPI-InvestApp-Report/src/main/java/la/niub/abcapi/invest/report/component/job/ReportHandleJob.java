/**
 * 
 */
package la.niub.abcapi.invest.report.component.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.constant.ReportConstant;
import la.niub.abcapi.invest.report.constant.ReportStatusConstant;
import la.niub.abcapi.invest.report.dao.invest.InvestnewResolvedTaskMapper;
import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.InvestDocFileData;
import la.niub.abcapi.invest.report.model.vo.InvestDocInput;
import la.niub.abcapi.invest.report.model.vo.InvestDocOutput;
import la.niub.abcapi.invest.report.model.vo.InvestDocTagAnalyst;
import la.niub.abcapi.invest.report.model.vo.InvestDocTagData;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocInput;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * 研报处理
 * 
 * @author zhairp createDate: 2019-03-14
 */
@Component
public class ReportHandleJob {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${algorithmCenter.callbackUrl}")
	private String callbackUrl;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ResearchTaskDomainMapper researchTaskDomainMapper;

	@Autowired
	private InvestnewResolvedTaskMapper investnewResolvedTaskMapper;

	@Autowired
	private PlatformService platformService;

	/**
	 * 批量更新研报
	 * 
	 * @author zhairp createDate: 2019-04-09
	 */
	@Scheduled(fixedRate = 1000 * 20)
	public void batcgUpdateReports() {
		List<Map<String, Object>> tasks = researchTaskDomainMapper.getResearchTasks(ReportStatusConstant.COLLECT_INIT, ReportConstant.REPORT_NUM);
		if (!CollectionUtils.isEmpty(tasks)) {
			log.info("批量更新研报开始 start");
			List<ResearchTaskDomain> reports = JSON.parseArray(JSON.toJSONString(tasks), ResearchTaskDomain.class);
			for (ResearchTaskDomain report : reports) {
				CompanySo company = platformService.getCompany(report.getUserId());
				if (null != company && !StringUtils.isEmpty(company.getParentId())) {
					// 更新研报父账号
					report.setParentId(company.getParentId());
					String fileUrl = report.getFileUrl();
					if (!StringUtils.isEmpty(fileUrl)) {
						// 添加文件类型
						report.setFileType(fileUrl.substring(fileUrl.lastIndexOf(".") + 1));
					}
					report.setStatus(ReportStatusConstant.UPDATE_REPORT_SUCCESS);
					researchTaskDomainMapper.updateReportParentId(report);
				} else {
					List<Long> ids = new ArrayList<Long>();
					ids.add(report.getId());
					researchTaskDomainMapper.batchUpdateStatus(ids, ReportStatusConstant.UPDATE_REPORT_FAILURE);
				}
			}
			log.info("批量更新研报结束 end");
		}
	}

	/**
	 * 同步数据到mongodb
	 * 
	 * @author zhairp createDate: 2019-03-13
	 */
	@Scheduled(fixedRate = 1000 * 30)
	public void synDataToMongoDB() {
		List<Map<String, Object>> tasks = researchTaskDomainMapper.getResearchTasks(ReportStatusConstant.UPDATE_REPORT_SUCCESS, ReportConstant.REPORT_NUM);
		if (!CollectionUtils.isEmpty(tasks)) {
			log.info("mongodb同步数据开始 start");
			List<ResearchTaskDomain> reports = JSON.parseArray(JSON.toJSONString(tasks), ResearchTaskDomain.class);
			List<Long> ids = new ArrayList<Long>();
			List<InvestDocInput> docs = new ArrayList<InvestDocInput>();
			reports.forEach(action -> {
				ids.add(action.getId());
				InvestDocInput doc = new InvestDocInput();
				doc.setId(action.getId());
				doc.setFile_url(action.getFileUrl());
				doc.setFile_type(action.getFileType());
				doc.setCallback_url(callbackUrl);
				doc.setTime(action.getCreateTime());
				doc.setUpdate_time(action.getCreateTime());
				InvestDocFileData fileData = new InvestDocFileData();
				fileData.setAccount_id(action.getUserId());
				doc.setFile_data(fileData);
				docs.add(doc);
			});
			reportService.addReportsToMongodb(docs);
			researchTaskDomainMapper.batchUpdateStatus(ids, ReportStatusConstant.PUSH_MONGODB);
			log.info("mongodb同步数据开始 end");
		}
	}

	/**
	 * 同步数据到solr
	 * 
	 * @author zhairp createDate: 2019-03-13
	 */
	@Scheduled(fixedRate = 1000 * 40)
	public void synDataToSolr() {
		List<ResearchTaskDomain> tasks = researchTaskDomainMapper.getResolvedTask(ReportConstant.REPORT_NUM, ReportStatusConstant.ANALYSIS_INIT);
		if (!CollectionUtils.isEmpty(tasks)) {
			log.info("solr同步数据开始 start");
			List<Long> sourceIds = new ArrayList<Long>();
			List<InvestSolrDocInput> solrDocs = new ArrayList<InvestSolrDocInput>();
			for (ResearchTaskDomain task : tasks) {
				InvestDocOutput doc = reportService.getInvestDocInputById(task.getId(), ReportConstant.MONGO_TEXT_COLLECTION);
				log.info("doc:{}", doc);
				if (null != doc && null != doc.getAlg_document_tags()) {

					try {
						InvestDocTagData tag = doc.getAlg_document_tags();
						InvestDocTagAnalyst analyst = new InvestDocTagAnalyst();
						if (!CollectionUtils.isEmpty(tag.getAlg_analyst_info())) {
							analyst = tag.getAlg_analyst_info().get(0);
						}
						InvestSolrDocInput solrDoc = new InvestSolrDocInput();
						solrDoc.setId(task.getId());
						solrDoc.setReportFileUrl(task.getFileUrl());
						solrDoc.setReportUserId(task.getUserId());
						solrDoc.setReportParentId(task.getParentId());
						solrDoc.setReportSourceType(task.getSourceType());
						Date createTime = task.getCreateTime();
						if (null != createTime) {
							solrDoc.setReportPublishTime(createTime);
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							// 添加发布日期
							solrDoc.setReportPublishDate(df.format(createTime));

							// 添加研报年份,用于统计功能
							DateFormat df2 = new SimpleDateFormat("yyyy");
							solrDoc.setReportYear(Integer.valueOf(df2.format(createTime)));
						}

						// 文件类型更改取值逻辑：由算法解析更改为从数据库查询
//						solrDoc.setReportFileType(tag.getAlg_file_type());
						solrDoc.setReportFileType(task.getFileType());

						solrDoc.setReportAnalystName(analyst.getAnalyst_name());
						solrDoc.setReportCertId(analyst.getCert_id());
						solrDoc.setReportEmail(analyst.getEmail());
						solrDoc.setReportTel(analyst.getTel());
						solrDoc.setReportPageCount(tag.getAlg_page_count());
						solrDoc.setReportPublishName(tag.getAlg_publish_name());
						solrDoc.setReportRating(tag.getAlg_rating());
						solrDoc.setReportSecurityId(tag.getAlg_security_id());
						solrDoc.setReportSecurityName(tag.getAlg_security_name());
						solrDoc.setReportSummary(tag.getAlg_summary());
						solrDoc.setReportTargetPriceHigh(tag.getAlg_target_price_high());
						if (!StringUtils.isEmpty(tag.getAlg_target_price_low())) {
							solrDoc.setReportTargetPriceLow(Double.parseDouble(tag.getAlg_target_price_low()));
						}
						solrDoc.setReportTitle(tag.getAlg_title());
						solrDoc.setReportTopic(tag.getAlg_topic());
						solrDoc.setReportIndustryType(tag.getAlg_industry_type());
						// 添加研报类型
						solrDoc.setReportType(tag.getAlg_report_type_two());
						solrDocs.add(solrDoc);
						// 收集需要更新的已解析的文档状态
						sourceIds.add(task.getId());
					} catch (Exception ex) {
						log.error("solr同步数据异常 id:[{}],{}", task.getId(), ex);
						ex.printStackTrace();
					}

				} else {
					// 处理解析失败的研报
					List<Long> failedSourceIds = new ArrayList<Long>();
					failedSourceIds.add(task.getId());
					investnewResolvedTaskMapper.batchUpdateResolvedTaskStatus(failedSourceIds, ReportStatusConstant.ANALYSIS_FAILURE);
				}

			}

			if (!CollectionUtils.isEmpty(solrDocs)) {
				reportService.addReportsToSolr(solrDocs);
				investnewResolvedTaskMapper.batchUpdateResolvedTaskStatus(sourceIds, ReportStatusConstant.PUSH_SOLR);
			}
			log.info("solr同步数据开始 end");
		}
	}

}
