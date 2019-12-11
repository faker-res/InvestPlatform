/**
 * 
 */
package la.niub.abcapi.invest.report.controller;

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

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.model.response.Response;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.ReportFilter;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * 搜索服务
 * 
 * @author zhairp createDate: 2019-03-14
 */
@RestController
@RequestMapping("search")
public class ReportSearchController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReportService reportService;

	@Autowired
	private PlatformService platformService;

	/**
	 * 查询研报筛选项
	 * 
	 * @author zhairp createDate: 2019-03-14
	 * @param reportFilter
	 * @return
	 */
	@PostMapping("getReportFilterItems")
	public Response getReportFilterItems(@RequestBody ReportFilter reportFilter) {
		log.info("getReportFilterItems input:{}", reportFilter.toString());
		CompanySo company = platformService.getCompany(reportFilter.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			reportFilter.setParentId(company.getParentId());
		}
		reportFilter.setUserId(null);
		return new Response(reportService.getReportFilterItems(reportFilter));
	}

	/**
	 * 查询研报列表
	 * 
	 * @author zhairp createDate: 2019-04-10
	 * @param reportFilter
	 * @return
	 */
	@PostMapping("queryReport")
	public Response queryReport(@RequestBody ReportFilter reportFilter) {
		log.info("queryReport input:{}", reportFilter.toString());
		CompanySo company = platformService.getCompany(reportFilter.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			reportFilter.setParentId(company.getParentId());
		}
		reportFilter.setUserId(null);
		return new Response(reportService.basicQueryReport(reportFilter));
	}

	/**
	 * 查询研报详情
	 * 
	 * @author zhairp createDate: 2019-03-19
	 * @param id
	 * @return
	 */
	@GetMapping("getReportDetail")
	public Response getReportDetail(@RequestParam String id) {
		log.info("getReportDetail input:{}", id);
		return new Response(reportService.getReportDetail(id));
	}

}
