/**
 * 
 */
package la.niub.abcapi.invest.report.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.constant.ReportConstant;
import la.niub.abcapi.invest.report.constant.SortType;
import la.niub.abcapi.invest.report.model.response.SolrQueryResponse;
import la.niub.abcapi.invest.report.model.vo.InvestDocInput;
import la.niub.abcapi.invest.report.model.vo.InvestDocOutput;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocInput;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocOutput;
import la.niub.abcapi.invest.report.model.vo.ReportFilter;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import la.niub.abcapi.invest.report.service.ReportService;

/**
 * @author zhairp createDate: 2019-03-04
 */
@Component
public class ReportServiceImpl implements ReportService {
	private Logger log = LoggerFactory.getLogger(getClass());

	public String[] facetFields = new String[] { "report_type", "report_topic", "report_security_id", "report_security_name", "report_publish_name", "report_analyst_name", "report_page_count", "report_file_type", "report_rating", "report_industry_type" };

	@Value("${oss.bucketName}")
	private String bucketName;

	@Value("${oss.envFolder}")
	private String envFolder;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private OSSClient ossClient;

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private PlatformService platformService;

	@Override
	public void addReportsToMongodb(List<InvestDocInput> docs) {
		mongoTemplate.insert(docs, ReportConstant.MONGO_FILE_COLLECTION);
	}

	/**
	 * 文件上传
	 * 
	 * @author zhairp createDate: 2019-03-08
	 * @param fileName 文件名称
	 * @param input    文件输入流
	 * @return 文件在OSS上的访问地址
	 */
	@Override
	public String uploadReport(String fileName, InputStream input) {
		log.info("########uploadReport fileName:{},input:{}", fileName, input);
		// 若存储空间不存在,则创建
		if (!ossClient.doesBucketExist(bucketName)) {
			ossClient.createBucket(bucketName);
		}
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 一套业务系统对应一个bucket,然后通过在bucket创建子目录来隔离不同环境的文件
		StringBuffer key = new StringBuffer(envFolder);
		key.append("/").append(UUID.randomUUID()).append(suffixName);
		ossClient.putObject(bucketName, key.toString(), input);
		log.info("########putObject fileName:{}", key.toString());
		// 设置文件访问权限为公共读(文件的拥有者和授权用户有该文件的读写权限，其他用户只有文件的读权限)
		ossClient.setObjectAcl(bucketName, key.toString(), CannedAccessControlList.PublicRead);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 100);
		// 设置文件有效期,定个小目标-100年
		Date expiration = calendar.getTime();
		URL url = ossClient.generatePresignedUrl(bucketName, key.toString(), expiration);
		String originalPath = url.toString();
		log.info("########generatePresignedUrl originalPath:{}", originalPath);
		String newPath = originalPath.substring(0, originalPath.indexOf("?"));
		log.info("########generatePresignedUrl newPath:{}", newPath);
		return newPath;
	}

	@Override
	public List<ResearchTaskDomain> getResearchTasks(Integer status, Integer limit) {
		Query query = new Query();
		query.limit(limit);
		query.addCriteria(Criteria.where("status").is(status));
		return mongoTemplate.find(query, ResearchTaskDomain.class, ReportConstant.MONGO_FILE_COLLECTION);
	}

	@Override
	public void batchUpdateResearchTaskStatus(List<Long> ids, Integer status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateMulti(query, update, ReportConstant.MONGO_FILE_COLLECTION);
	}

	@Override
	public void emptyMongo() {
		mongoTemplate.findAllAndRemove(null, ReportConstant.MONGO_FILE_COLLECTION);
	}

	@Override
	public void addReportsToSolr(List<InvestSolrDocInput> tasks) {
		try {
			solrClient.addBeans(ReportConstant.SOLR_COLLECTION, tasks);
			solrClient.commit(ReportConstant.SOLR_COLLECTION);
		} catch (SolrServerException | IOException e) {
			log.error("入solr库失败:{}", e);
		}
	}

	@Override
	public void emptySolr() {
		try {
			String query = "*:*";
			solrClient.deleteByQuery(ReportConstant.SOLR_COLLECTION, query);
			solrClient.commit(ReportConstant.SOLR_COLLECTION);
		} catch (SolrServerException | IOException e) {
			log.error("清空solr数据异常:{}", e);
		}
	}

	@Override
	public InvestDocOutput getInvestDocInputById(Long id, String collectionName) {
		return mongoTemplate.findById(id, InvestDocOutput.class, collectionName);
	}

	/**
	 * @author zhairp createDate: 2019-03-14
	 * @param reportFilter
	 * @return
	 */
	@Override
	public Map<String, Object> getReportFilterItems(ReportFilter reportFilter) {
		Map<String, Object> result = new HashMap<String, Object>();
		SolrQuery query = new SolrQuery();
		if (StringUtils.isEmpty(reportFilter.getKeyword())) {
			query.setQuery("*:*");
		} else {
			query.setQuery("report_keyword:" + reportFilter.getKeyword());
		}
		try {
			// 添加过滤条件
			// 添加用户所在公司
			if (!StringUtils.isEmpty(reportFilter.getParentId())) {
				query.addFilterQuery("report_parent_id:" + reportFilter.getParentId());
			}
			// 添加用户
			if (!StringUtils.isEmpty(reportFilter.getUserId())) {
				query.addFilterQuery("report_user_id:" + reportFilter.getUserId());
			}
			// 添加主题
			if (!CollectionUtils.isEmpty(reportFilter.getReportTopics())) {
				query.addFilterQuery("report_topic:(" + StringUtils.join(reportFilter.getReportTopics(), " OR ") + ")");
			}
			// 添加公司编码
			if (!CollectionUtils.isEmpty(reportFilter.getReportSecurityIds())) {
				query.addFilterQuery("report_security_id:(" + StringUtils.join(reportFilter.getReportSecurityIds(), " OR ") + ")");
			}
			// 添加公司名称
			if (!CollectionUtils.isEmpty(reportFilter.getReportSecurityNames())) {
				query.addFilterQuery("report_security_name:(" + StringUtils.join(reportFilter.getReportSecurityNames(), " OR ") + ")");
			}
			// 添加机构
			if (!CollectionUtils.isEmpty(reportFilter.getReportPublishNames())) {
				query.addFilterQuery("report_publish_name:(" + StringUtils.join(reportFilter.getReportPublishNames(), " OR ") + ")");
			}
			// 添加分析师
			if (!CollectionUtils.isEmpty(reportFilter.getReportAnalystNames())) {
				query.addFilterQuery("report_analyst_name:(" + StringUtils.join(reportFilter.getReportAnalystNames(), " OR ") + ")");
			}
			// 添加文件类型
			if (!CollectionUtils.isEmpty(reportFilter.getReportFileTypes())) {
				query.addFilterQuery("report_file_type:(" + StringUtils.join(reportFilter.getReportFileTypes(), " OR ") + ")");
			}
			// 添加评级
			if (!CollectionUtils.isEmpty(reportFilter.getReportRatings())) {
				query.addFilterQuery("report_rating:(" + StringUtils.join(reportFilter.getReportRatings(), " OR ") + ")");
			}
			// 添加行业
			if (!CollectionUtils.isEmpty(reportFilter.getReportIndustryTypes())) {
				query.addFilterQuery("report_industry_type:(" + StringUtils.join(reportFilter.getReportIndustryTypes(), " OR ") + ")");
			}
			// 添加研报类型
			if (!CollectionUtils.isEmpty(reportFilter.getReportTypes())) {
				query.addFilterQuery("report_type:(" + StringUtils.join(reportFilter.getReportTypes(), " OR ") + ")");
			}
			// 添加来源类型
			if (!StringUtils.isEmpty(reportFilter.getSourceType())) {
				query.addFilterQuery("report_source_type:" + reportFilter.getSourceType());
			}
			// 添加开始时间和结束时间
			if (!StringUtils.isEmpty(reportFilter.getStartDate()) && !StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[" + reportFilter.getStartDate() + " TO " + reportFilter.getEndDate() + "]");
			}
			if (!StringUtils.isEmpty(reportFilter.getStartDate()) && StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[" + reportFilter.getStartDate() + " TO *]");
			}
			if (StringUtils.isEmpty(reportFilter.getStartDate()) && !StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[* TO " + reportFilter.getEndDate() + "]");
			}

			query.setRows(0);
			query.setFacet(true);
			query.setFacetMinCount(1);
			query.setFacetLimit(100);
			query.setFacetMissing(false);
			query.setFacetSort(FacetParams.FACET_SORT_COUNT);
			query.addFacetField(facetFields);
			log.info("SolrQuery>>{}", query.toString());
			QueryResponse response = solrClient.query(ReportConstant.SOLR_COLLECTION, query);
			for (String facetField : facetFields) {
				dealFacetFieldValue(result, response, facetField);
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 分组字段处理
	 * 
	 * @author zhairp createDate: 2019-03-14
	 * @param result
	 * @param response
	 * @param facetField
	 */
	public void dealFacetFieldValue(Map<String, Object> result, QueryResponse response, String facetField) {
		FacetField field = response.getFacetField(facetField);
		if (field.getValueCount() > 0) {
			List<Map<String, Object>> values = new ArrayList<>();
			for (Count c : field.getValues()) {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("name", c.getName());
				value.put("count", c.getCount());
				values.add(value);
			}
			result.put(facetField, values);
		}
	}

	/**
	 * 通用的功能 搜索研报
	 * 
	 * @author zhairp createDate: 2019-03-15
	 * @param reportFilter
	 * @return
	 */
	@Override
	public <T> SolrQueryResponse<T> basicQueryReport(ReportFilter reportFilter) {

		SolrQuery query = new SolrQuery();
		if (StringUtils.isEmpty(reportFilter.getKeyword())) {
			query.setQuery("*:*");
		} else {
			query.setQuery("report_keyword:" + reportFilter.getKeyword());
		}
		try {
			// 添加过滤条件
			// 添加用户所在公司
			if (!StringUtils.isEmpty(reportFilter.getParentId())) {
				query.addFilterQuery("report_parent_id:" + reportFilter.getParentId());
			}
			// 添加用户
			if (!StringUtils.isEmpty(reportFilter.getUserId())) {
				query.addFilterQuery("report_user_id:" + reportFilter.getUserId());
			}
			// 添加主题
			if (!CollectionUtils.isEmpty(reportFilter.getReportTopics())) {
				query.addFilterQuery("report_topic:(" + StringUtils.join(reportFilter.getReportTopics(), " OR ") + ")");
			}
			// 添加公司编码
			if (!CollectionUtils.isEmpty(reportFilter.getReportSecurityIds())) {
				query.addFilterQuery("report_security_id:(" + StringUtils.join(reportFilter.getReportSecurityIds(), " OR ") + ")");
			}
			// 添加公司名称
			if (!CollectionUtils.isEmpty(reportFilter.getReportSecurityNames())) {
				query.addFilterQuery("report_security_name:(" + StringUtils.join(reportFilter.getReportSecurityNames(), " OR ") + ")");
			}
			// 添加机构
			if (!CollectionUtils.isEmpty(reportFilter.getReportPublishNames())) {
				query.addFilterQuery("report_publish_name:(" + StringUtils.join(reportFilter.getReportPublishNames(), " OR ") + ")");
			}
			// 添加分析师
			if (!CollectionUtils.isEmpty(reportFilter.getReportAnalystNames())) {
				query.addFilterQuery("report_analyst_name:(" + StringUtils.join(reportFilter.getReportAnalystNames(), " OR ") + ")");
			}
			// 添加文件类型
			if (!CollectionUtils.isEmpty(reportFilter.getReportFileTypes())) {
				query.addFilterQuery("report_file_type:(" + StringUtils.join(reportFilter.getReportFileTypes(), " OR ") + ")");
			}
			// 添加评级
			if (!CollectionUtils.isEmpty(reportFilter.getReportRatings())) {
				query.addFilterQuery("report_rating:(" + StringUtils.join(reportFilter.getReportRatings(), " OR ") + ")");
			}
			// 添加行业
			if (!CollectionUtils.isEmpty(reportFilter.getReportIndustryTypes())) {
				query.addFilterQuery("report_industry_type:(" + StringUtils.join(reportFilter.getReportIndustryTypes(), " OR ") + ")");
			}
			// 添加研报类型
			if (!CollectionUtils.isEmpty(reportFilter.getReportTypes())) {
				query.addFilterQuery("report_type:(" + StringUtils.join(reportFilter.getReportTypes(), " OR ") + ")");
			}
			// 添加来源类型
			if (!StringUtils.isEmpty(reportFilter.getSourceType())) {
				query.addFilterQuery("report_source_type:" + reportFilter.getSourceType());
			}
			// 添加开始时间和结束时间
			if (!StringUtils.isEmpty(reportFilter.getStartDate()) && !StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[" + reportFilter.getStartDate() + " TO " + reportFilter.getEndDate() + "]");
			}
			if (!StringUtils.isEmpty(reportFilter.getStartDate()) && StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[" + reportFilter.getStartDate() + " TO *]");
			}
			if (StringUtils.isEmpty(reportFilter.getStartDate()) && !StringUtils.isNotEmpty(reportFilter.getEndDate())) {
				query.addFilterQuery("report_publish_date:[* TO " + reportFilter.getEndDate() + "]");
			}

			// 追加条件 最低价格限制
			if (null != reportFilter.getReportTargetPriceLow()) {
				query.addFilterQuery("report_target_price_low:[" + reportFilter.getReportTargetPriceLow() + " TO *]");
			}

			// 添加排序规则
			if (SortType.compositive.equals(reportFilter.getSort())) {
				// 综合排序
				query.addSort("score", ORDER.desc);
				query.addSort("report_publish_time", ORDER.desc);
			} else if (SortType.timeDesc.equals(reportFilter.getSort())) {
				// 时间降序
				query.addSort("report_publish_time", ORDER.desc);
				query.addSort("score", ORDER.desc);
			} else {
				// 时间升序
				query.addSort("report_publish_time", ORDER.asc);
				query.addSort("score", ORDER.desc);
			}

			// 添加分页
			Integer pageIndex = 1;
			Integer pageSize = 10;
			if (reportFilter.getPageIndex() != null && reportFilter.getPageIndex().intValue() > 0) {
				pageIndex = reportFilter.getPageIndex();
			}
			if (reportFilter.getPageSize() != null && reportFilter.getPageSize().intValue() > 0) {
				pageSize = reportFilter.getPageSize();
			}
			query.setStart((pageIndex - 1) * pageSize);
			query.setRows(pageSize);
			log.info("SolrQuery>>{}", query.toString());
			QueryResponse response = solrClient.query(ReportConstant.SOLR_COLLECTION, query);
			long numFound = response.getResults().getNumFound();
			List<InvestSolrDocOutput> data = response.getBeans(InvestSolrDocOutput.class);
			return new SolrQueryResponse(numFound, data);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * @author zhairp createDate: 2019-03-19
	 * @param id
	 * @return
	 */
	@Override
	public InvestSolrDocOutput getReportDetail(String id) {
		try {
			SolrDocument doc = solrClient.getById(ReportConstant.SOLR_COLLECTION, id);
			InvestSolrDocOutput output = new InvestSolrDocOutput();
			Field[] fields = output.getClass().getDeclaredFields();
			for (Field field : fields) {
				org.apache.solr.client.solrj.beans.Field f = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
				String name = f.value();
				Object value = doc.get(name);
				log.info("name:{},value:{}", field.getName(), value);
				try {
					BeanUtils.setProperty(output, field.getName(), value);
				} catch (IllegalAccessException | InvocationTargetException e) {
					log.error("属性值复制异常:{}", e);
				}
			}
			return output;
		} catch (SolrServerException | IOException e) {
			log.error("Solr异常:{}", e);
		}
		return null;
	}

}
