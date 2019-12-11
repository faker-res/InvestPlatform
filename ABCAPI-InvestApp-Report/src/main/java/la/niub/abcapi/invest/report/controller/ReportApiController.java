/**
 * 
 */
package la.niub.abcapi.invest.report.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.constant.ReportStatusConstant;
import la.niub.abcapi.invest.report.dao.invest.InvestnewResolvedTaskMapper;
import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.response.Response;
import la.niub.abcapi.invest.report.model.response.SolrQueryResponse;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.CallbackInputVo;
import la.niub.abcapi.invest.report.model.vo.ExternalReportVo;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocOutput;
import la.niub.abcapi.invest.report.model.vo.RelatedAnalystInput;
import la.niub.abcapi.invest.report.model.vo.ReportFilter;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * 对外提供服务-openapi
 * 
 * @author zhairp createDate: 2019-03-14
 */
@RestController
@RequestMapping("api")
public class ReportApiController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResearchTaskDomainMapper researchTaskDomainMapper;

	@Autowired
	private InvestnewResolvedTaskMapper investnewResolvedTaskMapper;

	@Autowired
	private ReportService reportService;

	@Autowired
	private PlatformService platformService;

	/**
	 * 邮件透视
	 * 
	 * @author zhairp createDate: 2019-03-14
	 * @param input
	 * @return
	 */
	@PostMapping("external")
	public Response external(@RequestBody @Valid ExternalReportVo input) {
		log.info("external:{}", input.toString());
		ResearchTaskDomain record = new ResearchTaskDomain();
		record.setUserId(input.getUserId());
		record.setFileUrl(input.getFileUrl());
		record.setSourceType(input.getSourceType());
		record.setStatus(ReportStatusConstant.COLLECT_INIT);
		researchTaskDomainMapper.insertSelective(record);
		return new Response("外部研报接收成功!");
	}

	/**
	 * 邮件透视批量操作
	 * 
	 * @author zhairp createDate: 2019-03-27
	 * @param requestObj
	 * @return
	 */
	@PostMapping("batchExternal")
	public Response batchExternal(@RequestBody String requestObj) {
		log.info("batchExternal:{}", requestObj);
		if (!StringUtils.isEmpty(requestObj)) {
			List<ResearchTaskDomain> records = JSON.parseArray(requestObj, ResearchTaskDomain.class);
			if (!CollectionUtils.isEmpty(records)) {
				for (ResearchTaskDomain record : records) {
					if (!StringUtils.isEmpty(record.getUserId()) && !StringUtils.isEmpty(record.getFileUrl()) && !StringUtils.isEmpty(record.getSourceType())) {
						record.setStatus(ReportStatusConstant.COLLECT_INIT);
						researchTaskDomainMapper.insertSelective(record);
					}
				}
			}
		}
		return new Response("外部研报接收成功!");
	}

	/**
	 * 回调接口 表单提交
	 * 
	 * @author zhairp createDate: 2019-03-05
	 * @param sourceId     文档ID
	 * @param processError 结果
	 * @return
	 */
	@PostMapping("callback")
	public Response callback(@RequestParam(name = "fileId") Long sourceId, @RequestParam(name = "process_error") String processError) {
		log.info("callback sourceId:{},processError:{}", sourceId, processError);
		investnewResolvedTaskMapper.saveResolvedTask(sourceId, processError);
		return new Response("回调成功!");
	}

	/**
	 * 回调接口 JSON格式
	 * 
	 * @author zhairp createDate: 2019-03-12
	 * @param requestObj
	 * @return
	 */
	@Deprecated
	@PostMapping("callback1")
	public Response callback1(@RequestBody String requestObj) {
		log.info("callback requestObj:{}", requestObj);
		CallbackInputVo input = JSON.parseObject(requestObj, CallbackInputVo.class);
		log.info("callback input:{}", input);
		investnewResolvedTaskMapper.saveResolvedTask(input.getSourceId(), input.getProcessError());
		return new Response("回调成功!");
	}

	/**
	 * 卖方研报筛选项-综合搜索
	 * 
	 * @author zhairp createDate: 2019-04-23
	 * @param reportFilter
	 * @return
	 */
	@PostMapping("mailFilterItems")
	public Response mailFilterItems(@RequestBody ReportFilter reportFilter) {
		log.info("mailFilterItems input:{}", reportFilter.toString());
		reportFilter.setSourceType("mail");
		List<String> fileTypes = new ArrayList<String>();
		fileTypes.add("pdf");
		reportFilter.setReportFileTypes(fileTypes);
		CompanySo company = platformService.getCompany(reportFilter.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			reportFilter.setParentId(company.getParentId());
		}
		reportFilter.setUserId(null);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("selected", JSONArray.parseArray("[]"));
		String selected = reportFilter.getSelected();
		if (!StringUtils.isEmpty(selected)) {
			String[] selectedItems = selected.split(";");
			JSONArray ja = JSONArray.parseArray("[]");
			for (String item : selectedItems) {
				// 字段名称,字段值
				String[] itemArr = item.split(",");
				JSONObject jo = JSONObject.parseObject("{}");
				jo.put("type", itemArr[0]);
				jo.put("name", itemArr[1]);
				ja.add(jo);
			}
			result.put("selected", ja);
		}
		Map<String, Object> oldOption = reportService.getReportFilterItems(reportFilter);
		List<Map<String, Object>> option = new ArrayList<Map<String, Object>>();
		String[] keys = new String[] { "report_topic", "report_security_name", "report_publish_name", "report_type", "report_analyst_name", "report_rating", "report_industry_type" };
		String[] values = new String[] { "reportTopics", "reportSecurityNames", "reportPublishNames", "reportTypes", "reportAnalystNames", "reportRatings", "reportIndustryTypes" };
		String[] names = new String[] { "主题", "公司", "机构", "类型", "分析师", "评级", "行业" };
		if (null != oldOption) {
			for (int i = 0; i < keys.length; i++) {
				Map<String, Object> element = new HashMap<String, Object>();
				element.put("type", values[i]);
				element.put("showname", names[i]);
				element.put("multiValued", true);
				Object item = oldOption.get(keys[i]);
				if (null == item) {
					item = JSONArray.parseArray("[]");
				}
				element.put("item", item);
				option.add(element);
			}
		}
		result.put("option", option);
		return new Response(result);
	}

	/**
	 * 卖方研报列表-综合搜索
	 * 
	 * @author zhairp createDate: 2019-04-23
	 * @param reportFilter
	 * @return
	 */
	@PostMapping("mailReportList")
	public Response mailReportList(@RequestBody ReportFilter reportFilter) {
		log.info("mailReportList input:{}", reportFilter.toString());
		reportFilter.setSourceType("mail");
		List<String> fileTypes = new ArrayList<String>();
		fileTypes.add("pdf");
		reportFilter.setReportFileTypes(fileTypes);
		CompanySo company = platformService.getCompany(reportFilter.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			reportFilter.setParentId(company.getParentId());
		}
		reportFilter.setUserId(null);
		return new Response(reportService.basicQueryReport(reportFilter));
	}

	/**
	 * 卖方研报详情
	 * 
	 * @author zhairp createDate: 2019-04-24
	 * @param id
	 * @return
	 */
	@GetMapping("mailReportDetail")
	public Response mailReportDetail(@RequestParam String id) {
		log.info("mailReportDetail input:{}", id);
		return new Response(reportService.getReportDetail(id));
	}

	/**
	 * 获取目标价不为null的研报列表
	 * 
	 * @author zhairp createDate: 2019-04-25
	 * @param reportFilter
	 * @return
	 */
	@PostMapping("relatedAnalystReport")
	public Response relatedAnalystReport(@RequestBody RelatedAnalystInput input) {
		log.info("relatedAnalystReport input:{}", input.toString());
		ReportFilter reportFilter = new ReportFilter();
		reportFilter.setStartDate(input.getStartDate());
		reportFilter.setEndDate(input.getEndDate());
		reportFilter.setPageIndex(input.getPageIndex());
		reportFilter.setPageSize(input.getPageSize());
		reportFilter.setSourceType(input.getSourceType());
		reportFilter.setParentId(input.getParentId());
		reportFilter.setReportTargetPriceLow(Double.valueOf(0));
		return new Response(reportService.basicQueryReport(reportFilter));
	}

	/**
	 * 获取研究员研报数量接口
	 * 
	 * @author zhairp createDate: 2019-04-25
	 * @param input
	 * @return
	 */
	@PostMapping("relatedAnalystReportNum")
	public Response relatedAnalystReportNum(@RequestBody RelatedAnalystInput input) {
		log.info("relatedAnalystReportNum input:{}", input.toString());
		ReportFilter reportFilter = new ReportFilter();
		reportFilter.setParentId(input.getParentId());
		reportFilter.setStartDate(input.getStartDate());
		reportFilter.setEndDate(input.getEndDate());
		reportFilter.setSourceType(input.getSourceType());
		List<String> reportPublishNames = new ArrayList<String>();
		List<String> reportAnalystNames = new ArrayList<String>();
		if (!StringUtils.isEmpty(input.getAnalystName())) {
			reportAnalystNames.add(input.getAnalystName());
		}
		if (!StringUtils.isEmpty(input.getPublishName())) {
			reportPublishNames.add(input.getPublishName());
		}
		reportFilter.setReportAnalystNames(reportAnalystNames);
		reportFilter.setReportPublishNames(reportPublishNames);
		SolrQueryResponse<InvestSolrDocOutput> result = reportService.basicQueryReport(reportFilter);
		return new Response(result.getNumFound());
	}

}
