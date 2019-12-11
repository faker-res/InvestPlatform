/**
 * 
 */
package la.niub.abcapi.invest.report.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import la.niub.abcapi.invest.report.constant.ReportStatusConstant;
import la.niub.abcapi.invest.report.constant.SourceType;
import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.response.Response;
import la.niub.abcapi.invest.report.model.vo.ReportQuery;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportManageService;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * 
 * 研报管理
 * 
 * @author zhairp createDate: 2019-02-21
 */
@RestController
@RequestMapping("reportManage")
public class ReportManageController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportManageService reportManageService;

	@Autowired
	private ResearchTaskDomainMapper researchTaskDomainMapper;

	/**
	 * 研报上传
	 * 
	 * @author zhairp createDate: 2019-04-22
	 * @param file
	 * @param userId
	 * @return
	 */
	@PostMapping("upload")
	public Response upload(@RequestParam(required = true) MultipartFile file, @RequestParam(required = true) String userId) {
		String fileName = file.getOriginalFilename();
		log.info("upload userId:{},file:{},fileName:{}", userId, file, fileName);
		ResearchTaskDomain record = new ResearchTaskDomain();
		try {
			String filePath = reportService.uploadReport(fileName, file.getInputStream());
			// 存原始文件名字,用作根据名字筛选
			record.setTitle(fileName);
			record.setFileUrl(filePath);
			record.setUserId(userId);
			record.setSourceType(SourceType.upload.toString());
			record.setStatus(ReportStatusConstant.COLLECT_INIT);
			researchTaskDomainMapper.insertSelective(record);
		} catch (IOException e1) {
			log.info(fileName + "上传失败,原因:{}", e1.getMessage());
			e1.printStackTrace();
		}
		return new Response("研报[" + record.getId() + "]已上传");
	}

	/**
	 * 研报类型统计
	 * 
	 * @author zhairp createDate: 2019-04-22
	 * @param userId
	 * @return
	 */
	@GetMapping("statisticsByReportType")
	public Response statisticsByReportType(String userId) {
		return new Response(reportManageService.statisticsReportNums(userId, "report_type"));
	}

	/**
	 * 研报年份统计
	 * 
	 * @author zhairp createDate: 2019-04-22
	 * @param userId
	 * @return
	 */
	@GetMapping("statisticsByReportYear")
	public Response statisticsByReportYear(String userId) {
		return new Response(reportManageService.statisticsReportNums(userId, "report_year"));
	}

	/**
	 * 研报列表
	 * 
	 * @author zhairp createDate: 2019-04-23
	 * @param reportQuery
	 * @return
	 */
	@PostMapping("reportList")
	public Response reportList(@RequestBody ReportQuery reportQuery) {
		return new Response(reportManageService.reportList(reportQuery));
	}

}
