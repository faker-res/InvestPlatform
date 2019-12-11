/**
 * 
 */
package la.niub.abcapi.invest.report.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memfactory.utils.DateUtil;
import com.memfactory.utils.HttpUtil;

import la.niub.abcapi.invest.report.component.api.PlatformService;
import la.niub.abcapi.invest.report.config.configuration.JsonTemplate;
import la.niub.abcapi.invest.report.config.configuration.JsonTemplateFactory;
import la.niub.abcapi.invest.report.constant.ReportConstant;
import la.niub.abcapi.invest.report.model.so.CompanySo;
import la.niub.abcapi.invest.report.model.vo.BucketInput;
import la.niub.abcapi.invest.report.model.vo.DetailVo;
import la.niub.abcapi.invest.report.model.vo.HotBucket;
import la.niub.abcapi.invest.report.model.vo.HotQueryInput;
import la.niub.abcapi.invest.report.model.vo.HotQueryOutput;
import la.niub.abcapi.invest.report.model.vo.ItemQueryInput;
import la.niub.abcapi.invest.report.service.ReportBusinessService;

/**
 * @author zhairp createDate: 2019-04-08
 */
@Service
public class ReportBusinessServiceImpl implements ReportBusinessService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private PlatformService platformService;

	@Value("${spring.data.solr.host}")
	private String solrHost;

	/**
	 * @author zhairp createDate: 2019-04-08
	 * @param input
	 * @return
	 */
	@Override
	public Map<String, Object> getItems(ItemQueryInput input) {
		Map<String, Object> result = new HashMap<String, Object>();
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		// 添加来源类型
		if (!StringUtils.isEmpty(input.getSourceType())) {
			query.addFilterQuery("report_source_type:" + input.getSourceType());
		}
		// 添加用户所在公司
		CompanySo company = platformService.getCompany(input.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			query.addFilterQuery("report_parent_id:" + company.getParentId());
		}
		// 添加文件类型
		if (!StringUtils.isEmpty(input.getReportFileType())) {
			query.addFilterQuery("report_file_type:" + input.getReportFileType());
		}
		query.setRows(0);
		query.setFacet(true);
		query.setFacetMinCount(1);
		query.setFacetLimit(100);
		query.setFacetMissing(false);
		query.setFacetSort(FacetParams.FACET_SORT_COUNT);
		String[] facetFields = input.getFilter().split(",");
		query.addFacetField(facetFields);
		log.info("query:{}", query.toString());
		try {
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

	public void dealFacetFieldValue(Map<String, Object> result, QueryResponse response, String facetField) {
		FacetField field = response.getFacetField(facetField);
		if (field.getValueCount() > 0) {
			List<Map<String, Object>> values = new ArrayList<>();
			int i = 1;
			for (Count c : field.getValues()) {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("name", c.getName());
//				value.put("count", c.getCount());
				value.put("order", i++);
				values.add(value);
			}
			result.put(facetField, values);
		}
	}

	/**
	 * @author zhairp createDate: 2019-04-08
	 * @param input
	 * @return
	 */
	@Override
	public List<HotQueryOutput> getHotItem(HotQueryInput input) {
		List<HotQueryOutput> result = new ArrayList<HotQueryOutput>();
		// 默认近七天时间
		List<String> dateList = new ArrayList<String>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = input.getLatelyDays(); i >= 1; i--) {
			dateList.add(dateFormat.format(DateUtil.getLatelyDayOfDay(i)));
		}
		BucketInput bucketInput = new BucketInput();
		bucketInput.setUserId(input.getUserId());
		bucketInput.setSourceType(input.getSourceType());
		bucketInput.setStartDate(dateList.get(0));
		bucketInput.setEndDate(dateList.get(dateList.size() - 1));
		bucketInput.setHotItem(input.getHotItem());
		bucketInput.setReportFileType(input.getReportFileType());
		List<HotBucket> hotBuckets = getBuckets(bucketInput);

		if (!CollectionUtils.isEmpty(hotBuckets)) {
			hotBuckets.forEach(hotBucket_ -> {
				HotQueryOutput hotQueryOutput = new HotQueryOutput();
				hotQueryOutput.setName(hotBucket_.getVal());
				hotQueryOutput.setReport_total(hotBucket_.getReportNum());
				hotQueryOutput.setOrg_total(hotBucket_.getPublishNum());
				List<DetailVo> details = new ArrayList<DetailVo>();
				dateList.forEach(date -> {
					BucketInput subBucketInput = new BucketInput();
					subBucketInput.setUserId(input.getUserId());
					subBucketInput.setSourceType(input.getSourceType());
					subBucketInput.setStartDate(date);
					subBucketInput.setEndDate(date);
					subBucketInput.setHotItem("report_publish_date");
					if ("report_industry_type".equals(input.getHotItem())) {
						subBucketInput.setIndustryType(hotQueryOutput.getName());
					} else {
						subBucketInput.setCompany(hotQueryOutput.getName());
					}
					List<HotBucket> subHotBuckets = getBuckets(subBucketInput);
					DetailVo detailVo = new DetailVo();
					if (CollectionUtils.isEmpty(subHotBuckets)) {
						detailVo.setDate(date);
					} else {
						detailVo.setDate(date);
						detailVo.setReport(subHotBuckets.get(0).getReportNum());
						detailVo.setOrg(subHotBuckets.get(0).getPublishNum());
					}
					details.add(detailVo);
				});
				hotQueryOutput.setDeail(details);
				result.add(hotQueryOutput);
			});
		}
		return result;
	}

	public List<HotBucket> getBuckets(BucketInput input) {
		String jsonUrl = solrHost + "/invest_report_solr/query";
		JSONObject params = JSONObject.parseObject("{}");
		params.put("query", "*:*");
		params.put("limit", 0);
		List<String> filter = new ArrayList<String>();
		// 添加员工所在公司
		CompanySo company = platformService.getCompany(input.getUserId());
		if (null != company && !StringUtils.isEmpty(company.getParentId())) {
			filter.add("report_parent_id:" + company.getParentId());
		}
		// 添加来源类型
		if (!StringUtils.isEmpty(input.getSourceType())) {
			filter.add("report_source_type:" + input.getSourceType());
		}
		// 添加时间
		if (!StringUtils.isEmpty(input.getStartDate()) && !StringUtils.isEmpty(input.getEndDate())) {
			filter.add("report_publish_date:[" + input.getStartDate() + " TO " + input.getEndDate() + "]");
		}
		// 添加行业
		if (!StringUtils.isEmpty(input.getIndustryType())) {
			filter.add("report_industry_type:" + input.getIndustryType());
		}
		// 添加公司
		if (!StringUtils.isEmpty(input.getCompany())) {
			filter.add("report_security_name:" + input.getCompany());
		}
		// 添加文件类型
		if (!StringUtils.isEmpty(input.getReportFileType())) {
			filter.add("report_file_type:" + input.getReportFileType());
		}
		if (!CollectionUtils.isEmpty(filter)) {
			params.put("filter", filter);
		}
		JSONObject facet = JSONObject.parseObject("{}");
		JSONObject hotItem = JSONObject.parseObject("{}");
		hotItem.put("type", "terms");
		hotItem.put("field", input.getHotItem());
		hotItem.put("limit", 5);
		hotItem.put("sort", "reportNum desc");
		JSONObject nestedFacet = JSONObject.parseObject("{}");
		nestedFacet.put("publishNum", "unique(report_publish_name)");
		nestedFacet.put("reportNum", "unique(id)");
		hotItem.put("facet", nestedFacet);
		facet.put("hotItem", hotItem);
		params.put("facet", facet);
		log.info("jsonUrl:{},input:{}", jsonUrl, JSON.toJSONString(params));
		String result = HttpUtil.doPost(jsonUrl, (Object) params);
		log.info("output:{}", result);
		if (!StringUtils.isEmpty(result)) {
			JSONObject jo = JSON.parseObject(result);
			JSONObject facets = jo.getJSONObject("facets");
			if (null != facets) {
				Integer count = facets.getInteger("count");
				if (null != count && count.intValue() > 0) {
					JSONObject hotItem_ = facets.getJSONObject("hotItem");
					JSONArray buckets = hotItem_.getJSONArray("buckets");
					List<HotBucket> hotBuckets = new ArrayList<HotBucket>();
					for (int i = 0; i < buckets.size(); i++) {
						JSONObject bucket = buckets.getJSONObject(i);
						HotBucket hotBucket = new HotBucket();
						hotBucket.setVal(bucket.getString("val"));
						hotBucket.setPublishNum(bucket.getInteger("publishNum"));
						hotBucket.setReportNum(bucket.getInteger("reportNum"));
						hotBuckets.add(hotBucket);
					}
					return hotBuckets;
				}
			}
		}
		return null;
	}

	/**
	 * @author zhairp createDate: 2019-04-20
	 * @param input
	 * @return
	 */
	@Override
	public Map<String, Object> getItems2(ItemQueryInput input) {
		CompanySo company = platformService.getCompany(input.getUserId());
		if (null == company || StringUtils.isEmpty(company.getParentId())) {
			throw new RuntimeException("请维护账户" + input.getUserId() + "的父账号");
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("report_industry_type", JSONArray.parseArray("[]"));
		result.put("report_type", JSONArray.parseArray("[]"));
		String jsonUrl = solrHost + "/invest_report_solr/query";
		JSONObject params1 = JsonTemplateFactory.createJSONObj(new JsonTemplate() {
			@Override
			public String dealJson(String json) {
				return json.replace("$parentId", company.getParentId()).replace("$sourceType", "mail").replace("$fileType", "pdf");
//				return json.replace("$parentId", company.getParentId()).replace("$sourceType", "mail");
			}
		}, "json/getReportIndustry.json");

		log.info("params1:{}", params1.toJSONString());
		String response1 = HttpUtil.doPost(jsonUrl, (Object) params1);
		log.info("response1:{}", response1);
		getJsonResult(result, response1, "report_industry_type");
		JSONObject params2 = JsonTemplateFactory.createJSONObj(new JsonTemplate() {
			@Override
			public String dealJson(String json) {
				return json.replace("$parentId", company.getParentId()).replace("$sourceType", "mail").replace("$fileType", "pdf");
//				return json.replace("$parentId", company.getParentId()).replace("$sourceType", "mail");
			}
		}, "json/getReportType.json");

		log.info("params2:{}", params2.toJSONString());
		String response2 = HttpUtil.doPost(jsonUrl, (Object) params2);
		log.info("response2:{}", response2);
		getJsonResult(result, response2, "report_type");
		return result;
	}

	/**
	 * @author zhairp createDate: 2019-04-20
	 * @param result
	 * @param response
	 */
	private void getJsonResult(Map<String, Object> result, String response, String key) {
		if (!StringUtils.isEmpty(response)) {
			JSONObject res = JSONObject.parseObject(response);
			JSONObject facets = res.getJSONObject("facets");
			Integer count = facets.getInteger("count");
			if (null != count && count.intValue() > 0) {
				JSONObject hotItem = facets.getJSONObject("hotItem");
				JSONArray buckets = hotItem.getJSONArray("buckets");
				if (null != buckets && buckets.size() > 0) {
					List<Map<String, Object>> mapData = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < buckets.size(); i++) {
						JSONObject bucket = buckets.getJSONObject(i);
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("name", bucket.getString("val"));
						data.put("order", i + 1);
						mapData.add(data);
					}
					result.put(key, mapData);
				}
			}
		}
	}

}
