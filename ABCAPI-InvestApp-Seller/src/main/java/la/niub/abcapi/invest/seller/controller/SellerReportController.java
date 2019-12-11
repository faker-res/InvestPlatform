package la.niub.abcapi.invest.seller.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import la.niub.abcapi.invest.seller.config.enums.ReadArticleTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.client.ReportApi;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.config.enums.OssType;
import la.niub.abcapi.invest.seller.constant.ReportSearchConstant;
import la.niub.abcapi.invest.seller.model.ReportFilter;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.vo.MailReportSearchVo;
import la.niub.abcapi.invest.seller.model.vo.SearchWordVo;
import la.niub.abcapi.invest.seller.service.ISellerReportService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import la.niub.abcapi.invest.seller.service.base.OssService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ppan
 * @description : 卖方研报
 * @date : 2019-02-18 15:06
 */
@RestController
@RequestMapping("/mail/report")
@Validated
public class SellerReportController {

	private Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private ISellerReportService sellerReportService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private OssService ossService;

	@Autowired
	private IApiPlatFormClient apiPlatFormClient;

	@Autowired
	private ReportApi reportApi;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 邮件研报分类展示
	 *
	 * @param category
	 * @param offset
	 * @param report_type
	 * @param userId
	 * @param page
	 * @param limit
	 * @param file_type
	 * @return
	 */
	/* 接口废弃  */
	@Deprecated
//	@GetMapping
	public Response mailReport(@RequestParam String category, @RequestParam Integer offset, @RequestParam String report_type, @RequestParam @NotBlank(message = "参数[userId]不为空") String userId, @RequestParam Integer page, @RequestParam Integer limit, @RequestParam(required = false) String file_type) throws Exception {
		/*JSONObject reportObj = new JSONObject();
		reportObj.put("category", category);
		reportObj.put("offset", offset);
		reportObj.put("limit", limit);
		reportObj.put("userId", userId);
		reportObj.put("page", page);
		reportObj.put("report_type", report_type);
		reportObj.put("source_type", ReportSearchConstant.SEARCH_TYPE_MAIL);
		reportObj.put("prior", ReportSearchConstant.SORT_SCORE);
		reportObj.put("parent_user_id", userInfoService.getUserParentId(userId));
		reportObj.put("file_type", file_type);
		JSONObject result = sellerReportService.mailReport(reportObj);

		if (result == null) {
			result = new JSONObject();
			result.put("abcscore", 0);
			JSONArray categories = new JSONArray();
			result.put("categories", categories);
			result.put("current_page", page);
			result.put("total_page", 0);
			result.put("total_count", 0);
			result.put("keyword", "");
			result.put("m_keyword", "");
			result.put("request_id", "");
			JSONArray source = new JSONArray();
			result.put("source", source);
			JSONArray items = new JSONArray();
			result.put("items", items);
		}
		return new Response(result);*/

		JSONObject result = JSONObject.parseObject("{}");
		result.put("current_page", page);
		result.put("total_count", 0);
		JSONArray items = JSONArray.parseArray("[]");
		result.put("items", items);
		JSONObject data = reportApi.queryReport(new String[] { category }, null, new String[] { "pdf" }, "mail", userId, page, limit, "timeAsc");
		if (null != data) {
			long numFound = data.getLong("numFound");
			if (numFound > 0) {
				result.put("total_count", numFound);
				JSONArray reportData = data.getJSONArray("data");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < reportData.size(); i++) {
					JSONObject oldObj = reportData.getJSONObject(i);
					JSONObject newObj = JSONObject.parseObject("{}");
					// 属性值复制 FIXME
					newObj.put("id", oldObj.getString("id"));
					newObj.put("file_url", oldObj.getString("reportFileUrl"));
					newObj.put("alg_report_type_two", oldObj.getString("reportType"));
					newObj.put("file_type", oldObj.getString("reportFileType"));
					newObj.put("source_type", oldObj.getString("reportSourceType"));
					newObj.put("source_title", oldObj.getString("reportTitle"));
					String time = oldObj.getString("reportPublishDate");
					Date d = df.parse(time);
					newObj.put("alg_publish_date", time);
					newObj.put("alg_title", oldObj.getString("reportTitle"));
					newObj.put("title", oldObj.getString("reportTitle"));
					newObj.put("summary", oldObj.getString("reportSummary"));
					newObj.put("url", oldObj.getString("reportFileUrl"));
					newObj.put("owner_id", oldObj.getString("reportUserId"));
					newObj.put("rating", oldObj.getString("reportRating"));
					newObj.put("alg_page_count", oldObj.getInteger("reportPageCount"));
					newObj.put("stockcode", oldObj.getString("reportSecurityId"));
					newObj.put("stockname", oldObj.getString("reportSecurityName"));

					// 无用的待确定的字段
					newObj.put("favorite_times", 0);
					newObj.put("file_path", null);
					newObj.put("alg_rating_change", null);
					newObj.put("alg_author_email", null);
					newObj.put("sender_name", null);
					newObj.put("source", "");
					newObj.put("source_url", null);
					newObj.put("summarynum", 0);
					newObj.put("score", -1);
					newObj.put("group_name", null);
					newObj.put("alg_document_tags", null);
					newObj.put("alg_report_type_one", oldObj.getString("reportType"));
					newObj.put("industry_id", "");
					newObj.put("alg_report_type_three", oldObj.getString("reportType"));
					newObj.put("typetitle", null);
					newObj.put("publish_at", d.getTime());
					newObj.put("time", d.getTime());
					newObj.put("create_at", d.getTime());
					items.add(newObj);
				}
				result.put("items", items);
			}
		}
		return new Response(result);
	}

	/**
	 * 查询研报列表
	 *@author zhairp createDate: 2019-04-19
	 * @param reportFilter
	 * @return
	 * @throws Exception
	 */
	@PostMapping("queryReport")
	public Response queryReport(@RequestBody ReportFilter reportFilter) throws Exception {
		logger.info("reportFilter:{}",reportFilter.toString());
		JSONObject result = JSONObject.parseObject("{}");
		result.put("current_page", reportFilter.getPageIndex());
		result.put("total_count", 0);
		JSONArray items = JSONArray.parseArray("[]");
		result.put("items", items);
		JSONObject data = reportApi.queryReport(reportFilter.getReportTypes(), reportFilter.getReportIndustryTypes(), new String[] { "pdf" }, "mail", reportFilter.getUserId(), reportFilter.getPageIndex(), reportFilter.getPageSize(), "timeDesc");
//		JSONObject data = reportApi.queryReport(reportFilter.getReportTypes(), reportFilter.getReportIndustryTypes(), null, "mail", reportFilter.getUserId(), reportFilter.getPageIndex(), reportFilter.getPageSize(), "timeDesc");
		if (null != data) {
			long numFound = data.getLong("numFound");
			if (numFound > 0) {
				result.put("total_count", numFound);
				JSONArray reportData = data.getJSONArray("data");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < reportData.size(); i++) {
					JSONObject oldObj = reportData.getJSONObject(i);
					JSONObject newObj = JSONObject.parseObject("{}");
					// 属性值复制 FIXME
					newObj.put("id", oldObj.getString("id"));
					newObj.put("file_url", oldObj.getString("reportFileUrl"));
					newObj.put("alg_report_type_two", oldObj.getString("reportType"));
					newObj.put("file_type", oldObj.getString("reportFileType"));
					newObj.put("source_type", oldObj.getString("reportSourceType"));
					newObj.put("source_title", oldObj.getString("reportTitle"));
					String time = oldObj.getString("reportPublishDate");
					Date d = df.parse(time);
					newObj.put("alg_publish_date", time);
					newObj.put("alg_title", oldObj.getString("reportTitle"));
					newObj.put("title", oldObj.getString("reportTitle"));
					newObj.put("summary", oldObj.getString("reportSummary"));
					newObj.put("url", oldObj.getString("reportFileUrl"));
					newObj.put("owner_id", oldObj.getString("reportUserId"));
					newObj.put("rating", oldObj.getString("reportRating"));
					newObj.put("alg_page_count", oldObj.getInteger("reportPageCount"));
					newObj.put("stockcode", oldObj.getString("reportSecurityId"));
					newObj.put("stockname", oldObj.getString("reportSecurityName"));

					// 无用的待确定的字段
					newObj.put("favorite_times", 0);
					newObj.put("file_path", null);
					newObj.put("alg_rating_change", null);
					newObj.put("alg_author_email", null);
					newObj.put("sender_name", null);
					newObj.put("source", "");
					newObj.put("source_url", null);
					newObj.put("summarynum", 0);
					newObj.put("score", -1);
					newObj.put("group_name", null);
					newObj.put("alg_document_tags", null);
					newObj.put("alg_report_type_one", oldObj.getString("reportType"));
					newObj.put("industry_id", "");
					newObj.put("alg_report_type_three", oldObj.getString("reportType"));
					newObj.put("typetitle", null);
					newObj.put("publish_at", d.getTime());
					newObj.put("time", d.getTime());
					newObj.put("create_at", d.getTime());
					items.add(newObj);
				}
				result.put("items", items);
			}
		}
		return new Response(result);
	}

	/**
	 * 自选股
	 *
	 * @param groupId
	 * @param limit
	 * @param offset
	 * @param groupType
	 * @param userId
	 * @return
	 */
	@GetMapping("/home/stock")
	public Response mailHomeStock(@RequestParam Integer groupId, @RequestParam Integer limit, @RequestParam Integer offset, @RequestParam Integer groupType, @RequestParam @NotBlank(message = "参数[userId]不为空") String userId, @RequestParam String token) throws Exception {
		JSONObject reportObj = new JSONObject();
		reportObj.put("groupId", groupId);
		reportObj.put("limit", limit);
		reportObj.put("offset", offset);
		reportObj.put("groupType", groupType);
		reportObj.put("userId", userId);
		reportObj.put("token", token);
		reportObj.put("source_type", ReportSearchConstant.SEARCH_TYPE_MAIL);
		reportObj.put("prior", ReportSearchConstant.SORT_SCORE);
		SearchWordVo searchWord = new SearchWordVo();
		// TODO 获取自选股 未接入自选股
		List<String> stockList = sellerReportService.getSelfStockList(userId);
		JSONObject result = new JSONObject();
		if (stockList.size() == 0) {
			result.put("abcscore", 0);
			JSONArray categories = new JSONArray();
			result.put("categories", categories);
			result.put("current_page", 1);
			result.put("total_page", 0);
			result.put("total_count", 0);
			result.put("keyword", "");
			result.put("m_keyword", "");
			result.put("request_id", "");
			JSONArray source = new JSONArray();
			result.put("source", source);
			JSONArray items = new JSONArray();
			result.put("items", items);
		} else {
			searchWord.setCompany(stockList);
			reportObj.put("stock", searchWord.getCompanyStr());
			reportObj.put("parent_user_id", userInfoService.getUserParentId(userId));
			result = sellerReportService.mailReport(reportObj);
			result = result == null ? new JSONObject() : result;
		}
		return new Response(result);
	}

	/**
	 * 主要行业和报告类型
	 *
	 * @param userId
	 * @return
	 */
	@GetMapping("/filter")
	public Response mailFilter(@NotBlank(message = "参数[userId]不为空") String userId) throws Exception {
//        return new Response(sellerReportService.mailFilter(userId));

		JSONObject result = JSONObject.parseObject("{}");
		result.put("industry", null);
		result.put("category", null);
		JSONObject data = reportApi.getItems("report_industry_type,report_type", userId, "mail", "pdf");
//		JSONObject data = reportApi.getItems("report_industry_type,report_type", userId, "mail", null);
		if (null != data) {
			JSONArray industries = data.getJSONArray("report_industry_type");
			JSONArray reportTypes = data.getJSONArray("report_type");
			result.put("industry", industries);
			result.put("category", reportTypes);
		}
		return new Response(result);
	}

	/**
	 * 从搜索中获取热门行业
	 *
	 * @return
	 */
	@GetMapping("/hot/industry")
	public Response getHotIndustry(@NotBlank(message = "参数[userId]不为空") String userId) throws Exception {
//		return new Response(sellerReportService.getHotIndustry(userId));

		return new Response(reportApi.getHotItem("report_industry_type", "mail", userId, 7, "pdf"));
//		return new Response(reportApi.getHotItem("report_industry_type", "mail", userId, 7, null));
	}

	/**
	 * 从搜索中获取热门公司
	 *
	 * @param userId
	 * @return
	 */
	@GetMapping("/hot/company")
	public Response getHotCompany(@NotBlank(message = "参数[userId]不为空") String userId) throws Exception {
//		return new Response(sellerReportService.getHotCompany(userId));

		return new Response(reportApi.getHotItem("report_security_name", "mail", userId, 7, "pdf"));
//		return new Response(reportApi.getHotItem("report_security_name", "mail", userId, 7, null));
	}

	/**
	 * 获取研报详情
	 *
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/detail")
	public Response getReportDetail(@NotBlank(message = "参数[reportId]不为空") String reportId, @NotBlank(message = "参数[userId]不为空") String userId) throws Exception {
//		return new Response(sellerReportService.getReportDetail(reportId, userId));

		JSONObject result = JSONObject.parseObject("{}");
		JSONObject data = reportApi.getReportDetail(reportId);
		if (null != data) {
			// FIXME 有多余的字段
			result.put("filetype", data.getString("reportFileType"));
			result.put("file_url", data.getString("reportFileUrl"));
			result.put("alg_report_type_three", data.getString("reportType"));
			result.put("owner_id", data.getString("reportUserId"));
			result.put("alg_title", data.getString("reportTitle"));
			result.put("alg_page_count", data.getInteger("reportPageCount"));
			result.put("title", data.getString("reportTitle"));
			result.put("alg_report_type_two", data.getString("reportType"));
			result.put("id", data.getString("id"));
			result.put("summary", data.getString("reportSummary"));
			result.put("source_type", data.getString("reportSourceType"));
			result.put("alg_publish_name", data.getString("reportSecurityName"));
			result.put("report_type", data.getString("reportType"));
			result.put("source_title", data.getString("reportTitle"));
			result.put("alg_publish_date", data.getString("reportPublishDate"));
			result.put("alg_report_type_one", data.getString("reportType"));

			String author = StringUtil.isEmpty(data.getString("reportAnalystName")) ? "" : data.getString("reportAnalystName");
			sellerReportService.saveReadLog(userId, ReadArticleTypeEnum.MAIL_REPORT.getType(), reportId, author);
		}
		return new Response(result);
	}

	/**
	 * 下载研报
	 *
	 * @param fileUrl
	 * @throws Exception
	 */
	@GetMapping(value = "/download")
	public ResponseEntity<Resource> downloadReport(@NotBlank(message = "参数[fileUrl]不为空") String fileUrl, @NotBlank(message = "参数[fileName]不为空") String fileName, @NotBlank(message = "参数[fileType]不为空") String fileType) throws Exception {
		if (!fileType.startsWith(".")) {
			fileType = "." + fileType;
		}

		fileName = fileName + fileType;
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}

		fileUrl = fileUrl.replace("https://", "").replace("http://", "");
		if (fileUrl.contains("?")) {
			fileUrl = fileUrl.substring(0, fileUrl.indexOf("?"));
		}
		String endPoint = fileUrl.substring(0, fileUrl.indexOf("."));
		String key = fileUrl.substring(fileUrl.indexOf("/") + 1, fileUrl.length());

		OssType ossType;
		if (endPoint.contains(OssType.MDRESCOURE.getOssType())) {
			ossType = OssType.MDRESCOURE;
		} else if (endPoint.contains(OssType.ABCCRAWLER.getOssType())) {
			ossType = OssType.ABCCRAWLER;
		} else if (endPoint.contains(OssType.ABCPARSING.getOssType())) {
			ossType = OssType.ABCPARSING;
		} else if (endPoint.contains(OssType.INVESTREPORT.getOssType())) {
			ossType = OssType.INVESTREPORT;
		} else {
			throw new RuntimeException("研报存储的文件空间找不到");
		}

		InputStream inputStream = ossService.getFile(key, ossType);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("charset", "utf-8");
		headers.add("Content-Disposition", "attachment;filename=" + fileName);
		Resource resource = new InputStreamResource(inputStream);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/force-download")).body(resource);
	}

	/**
	 * 获取相关研报
	 *
	 * @param userId
	 * @param limit
	 * @param fileType
	 * @param stockName
	 * @param industry
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/related")
	public Response getRelatedReport(@NotBlank(message = "参数[userId]不为空") String userId, @RequestParam(defaultValue = "5") Integer limit, @RequestParam(required = false) String fileType, String stockName, String industry) throws Exception {
		JSONObject result = null;
		if (StringUtil.isEmpty(stockName) && StringUtil.isEmpty(industry)) {
			return new Response(result);
		}

		JSONObject reportObj = new JSONObject();
		reportObj.put("offset", 0);
		reportObj.put("limit", limit);
		reportObj.put("userId", userId);
		reportObj.put("page", 1);
		reportObj.put("report_type", "");
		reportObj.put("source_type", ReportSearchConstant.SEARCH_TYPE_MAIL);
		reportObj.put("prior", ReportSearchConstant.SORT_TIME_DESC);
		reportObj.put("parent_user_id", userInfoService.getUserParentId(userId));
		reportObj.put("file_type", fileType);
		reportObj.put("stockName", stockName);
		reportObj.put("industry", industry);

		return new Response(sellerReportService.getRelatedReport(reportObj));
	}

    /**
     * 邮件研报搜索
	 *
     * @param requestObj
     * @return
     * @throws Exception
     */
    @PostMapping("/search")
    public Response search(@RequestBody String requestObj) throws Exception {
        logger.info("param:{}", requestObj);
        MailReportSearchVo mailReportSearchVo = JSON.parseObject(requestObj, MailReportSearchVo.class);
        mailReportSearchVo.setParentId(userInfoService.getUserParentId(mailReportSearchVo.getUserId()));

		JSONObject result = sellerReportService.search(mailReportSearchVo);
		return new Response(result);
	}

	/**
	 * 获取研报的图表
	 *
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/charts")
	public Response getCharts(@NotBlank(message = "参数[reportId]不为空") String reportId) throws Exception {
		return new Response(sellerReportService.getCharts(reportId));
	}

	/**
	 * 获取预置热词，热门搜索，热门推荐运营位配置接口
	 *
	 * @param type     默认是1，1、预置热词，2、热门推荐 3、热门搜索
	 * @param module   模块，指各个垂搜模块，按产品规则编码
	 *                 运营位说明（https://confluence.niub.la/pages/viewpage.action?pageId=2858047）
	 * @param cate     指公告、研报搜索中的类型，编码与module相同。在指定column下进行搜索。如：沪深股研究、美股研究
	 * @param terminal 0、web 1、app
	 * @param lan      0 中文 1 英文
	 * @param offset
	 * @param limit    获取条数
	 * @return
	 */
	@GetMapping("/operate-config/keyword-query")
	public Response getOperateConfigKeywordQuery(@RequestParam(value = "type", defaultValue = "2") Integer type, @RequestParam(value = "module", defaultValue = "30001") Integer module, @RequestParam(value = "cate", defaultValue = "30001") Integer cate, @RequestParam(value = "terminal", defaultValue = "0") Integer terminal, @RequestParam(value = "lan", defaultValue = "0") Integer lan, @RequestParam(value = "offset", defaultValue = "0") Integer offset, @RequestParam(value = "limit", defaultValue = "1") Integer limit) {
		return apiPlatFormClient.getOperateConfigKeywordQuery(type, module, cate, terminal, lan, offset, limit);
	}

}
