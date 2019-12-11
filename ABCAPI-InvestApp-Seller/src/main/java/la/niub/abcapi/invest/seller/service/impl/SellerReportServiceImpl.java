package la.niub.abcapi.invest.seller.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;
import la.niub.abcapi.invest.seller.component.util.RedisUtil;
import la.niub.abcapi.invest.seller.component.util.ReportUtil;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.ReadArticleTypeEnum;
import la.niub.abcapi.invest.seller.constant.RedisConstant;
import la.niub.abcapi.invest.seller.constant.ReportSearchConstant;
import la.niub.abcapi.invest.seller.model.ReadLogModel;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.client.*;
import la.niub.abcapi.invest.seller.model.vo.MailReportSearchVo;
import la.niub.abcapi.invest.seller.model.vo.SearchWordVo;
import la.niub.abcapi.invest.seller.service.ISellerReportService;
import la.niub.abcapi.invest.seller.service.ISellerReportTransactionService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : ppan
 * @description : 卖方研报service
 * @date : 2019-02-18 15:12
 */
@Service
public class SellerReportServiceImpl implements ISellerReportService {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IApiPlatFormClient apiPlatFormClient;

    @Autowired
    private ISellerReportTransactionService sellerReportTransactionService;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public JSONObject mailReport(JSONObject jsonObject) throws Exception {
        String parentUserId = jsonObject.getString("parent_user_id");
        String userId = jsonObject.getString("userId");
        String allUserIdStr = "";
        if (StringUtils.isNotEmpty(userId)){
            if (StringUtils.isNotEmpty(parentUserId)){
                allUserIdStr = String.format("%s OR %s", userId, parentUserId);
            } else {
                allUserIdStr = userId;
            }
        } else {
            if (StringUtils.isNotEmpty(parentUserId)){
                allUserIdStr = parentUserId;
            }
        }

        JSONObject param = new JSONObject();
        param.put("alg_report_type", jsonObject.getOrDefault("category",""));
        param.put("offset", (Integer) jsonObject.get("offset") * (Integer) jsonObject.get("limit"));
        param.put("limit", jsonObject.get("limit"));
        param.put("owner_id", allUserIdStr);
        param.put("prior", jsonObject.get("prior"));
        param.put("type", jsonObject.getString("source_type"));
        param.put("start_time", 0);
        param.put("keyword", jsonObject.getOrDefault("keyword", ""));
        param.put("alg_industry_type", "");
        param.put("alg_author_name", "");
        param.put("alg_publish_name", "");
        param.put("alg_rating", "");
        param.put("alg_security_name", jsonObject.getOrDefault("stock",""));
        param.put("end_time", System.currentTimeMillis()/1000);
        param.put("file_type", jsonObject.getOrDefault("file_type", ""));
        param.put("from", "");
        param.put("receivers", "");

        Response<MailReportSearchResponse> response = apiPlatFormClient.mailReportSearch(param.toJSONString());
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台搜索卖方研报失败, 返回值：" + JSON.toJSONString(response) + ", param:" + param.toJSONString());
        }

        return getData(response.getData(), jsonObject);
    }

    /**
     * TODO 获取用户自选股列表 未接入自选股
     * @param userId
     * @returns
     * @throws Exception
     */
    @Override
    public List<String> getSelfStockList(String userId) throws Exception {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> mailFilter(String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        String allUserIdStr = userId;
        if(StringUtils.isNotEmpty(parentId)){
            allUserIdStr = String.format("%s,%s", userId, parentId);
        }

        Response<Map<String, List<String>>> response = apiPlatFormClient.mailReportSearchFacet(ReportSearchConstant.SEARCH_TYPE_MAIL, allUserIdStr);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的主要行业和报告类型失败,返回值：" + JSON.toJSONString(response) + ", userId:" + userId);
        }
        Map<String, List<String>> map = response.getData() == null ? new HashMap<>() : response.getData();

        Map<String, Object> result = new HashMap<>();
        List<String> mainIndustryList = map.get("main_industry");
        List<String> reportTypeList = map.get("report_type");
        List<Map<String, Object>> industry = new ArrayList<>();
        List<Map<String, Object>> category = new ArrayList<>();
        if (mainIndustryList != null) {
            for (int i = 0; i < mainIndustryList.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", null);
                item.put("filter", null);
                item.put("name", mainIndustryList.get(i));
                item.put("order", i + 1);
                item.put("createAt", null);
                item.put("updateAt", null);
                item.put("status", null);
                industry.add(item);
            }
        }
        if (reportTypeList != null) {
            for (int i = 0; i < reportTypeList.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", null);
                item.put("filter", null);
                item.put("code", "");
                item.put("name", reportTypeList.get(i));
                item.put("order", i + 1);
                item.put("app", null);
                item.put("createAt", null);
                item.put("updateAt", null);
                item.put("status", null);
                category.add(item);
            }
        }

        result.put("industry",industry);
        result.put("category",category);
        return result;
    }

    @Override
    public List<Map<String, Object>> getHotIndustry(String userId) throws Exception {
        String redisKey = RedisConstant.SELLER_REPORT_HOT_INDUSTRY + "_" + userId;
        String redisVal = redisUtil.get(redisKey);
        if(redisVal != null && !"[]".equals(redisVal)){
            return JSONArray.parseObject(redisVal, List.class);
        }
        String parentId = userInfoService.getUserParentId(userId);
        String allUserIdStr = userId;
        if(StringUtils.isNotEmpty(parentId)){
            allUserIdStr = String.format("%s,%s", userId, parentId);
        }

        Response<List<Map<String, Object>>> response = apiPlatFormClient.getMailReportHotIndustry(ReportSearchConstant.SEARCH_TYPE_MAIL, allUserIdStr);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的热门行业失败,返回值：" + JSON.toJSONString(response) + ", userId:" + userId);
        }

        List<Map<String, Object>> hotIndustryList = response.getData() == null ? new ArrayList<>() : response.getData();
        List<Map<String, Object>> result = new ArrayList<>();
        hotIndustryList.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", item.get("industry_name"));
            map.put("org_total", item.get("organization_count"));
            map.put("report_total", item.get("report_count"));
            map.put("deail", item.get("deail"));
            result.add(map);
        });

        redisUtil.set(redisKey, JSON.toJSONString(result), TimeUtil.msToAM1(), TimeUnit.MILLISECONDS);

        return result;
    }

    @Override
    public List<Map<String, Object>> getHotCompany(String userId) throws Exception {
        String redisKey = RedisConstant.SELLER_REPORT_HOT_COMPANY + "_" + userId;
        String redisVal = redisUtil.get(redisKey);
        if(redisVal != null && !"[]".equals(redisVal)){
            return JSONArray.parseObject(redisVal, List.class);
        }
        String parentId = userInfoService.getUserParentId(userId);
        String allUserIdStr = userId;
        if(StringUtils.isNotEmpty(parentId)){
            allUserIdStr = String.format("%s,%s", userId, parentId);
        }

        Response<List<Map<String, Object>>> response = apiPlatFormClient.getMailReportHotCompany(ReportSearchConstant.SEARCH_TYPE_MAIL, allUserIdStr);
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的热门公司失败,返回值：" + JSON.toJSONString(response) + ", userId:" + userId);
        }

        List<Map<String, Object>> hotCompanyList = response.getData() == null ? new ArrayList<>() : response.getData();
        List<Map<String, Object>> result = new ArrayList<>();
        hotCompanyList.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", item.get("company_name"));
            map.put("org_total", item.get("organization_count"));
            map.put("report_total", item.get("report_count"));
            map.put("deail", item.get("deail"));
            result.add(map);
        });

        redisUtil.set(redisKey, JSON.toJSONString(result), TimeUtil.msToAM1(), TimeUnit.MILLISECONDS);

        return result;
    }

    @Override
    public JSONObject getReportDetail(String reportId, String userId) throws Exception {
        Response<MailReportResponse> responseDetail = apiPlatFormClient.getMailReportDetail(reportId);
        if (responseDetail == null || responseDetail.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的详情失败,返回值：" + JSON.toJSONString(responseDetail) + ", reportId:" + reportId + ", userId:" + userId);
        }
        MailReportResponse reportResponse = responseDetail.getData();

        Map<String, Object> param = new HashMap<>();
        param.put("id", reportId);
        JSONObject result = new JSONObject();
        if (reportResponse == null) {
            result.put("search_query", param.toString());
            return result;
        }

        result = (JSONObject) JSON.toJSON(reportResponse);
        long time = result.getLong("time");
        result.put("time", time / 1000);
        result.put("industry_name", ReportUtil.getRedIndustryType(result));
        result.put("publish", result.getString("alg_publish_name"));
        result.put("report_type", ReportUtil.getRedReportType(result));
        result.put("rating", result.getString("alg_rating"));
        result.put("company", result.getString("alg_security_name"));
        String fileUrl = result.getString("file_url");

        String fromAddress = result.getString("from_address");
        String fromName = result.getString("from_name");
        result.put("from", convertFromAddress(fromAddress, fromName));
        JSONArray toAddressArr = result.getJSONArray("receivers_address");
        JSONArray toNameArr = result.getJSONArray("receivers_address");
        result.put("receivers", convertToAddress(toAddressArr, toNameArr));

        result.put("file_url", StringUtils.isEmpty(fileUrl) ? "" : fileUrl.replace("-internal",""));
        JSONArray authorArr = result.getJSONArray("alg_author_name");
        String author = getAuthorStr(authorArr, ",");
        result.put("author", author);
        result.put("search_query", param.toString());

        // 获取目标价 solr没有该字段，去解析的研报库获取
        Response<MailReportTextResponse> reportText = apiPlatFormClient.getMailReportText(reportId);
        if (reportText == null || reportText.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报解析后的文本失败,返回值：" + JSON.toJSONString(reportText));
        }
        result.put("targetPrice", reportText.getData() == null || reportText.getData().getAlgDocumentTags() == null ? null : reportText.getData().getAlgDocumentTags().getTargetPriceLow());

        // 记录阅读日志
        saveReadLog(userId, ReadArticleTypeEnum.MAIL_REPORT.getType(), reportId, author);

        return result;
    }

    @Override
    public JSONObject getRelatedReport(JSONObject jsonObject) throws Exception {
        String parentUserId = jsonObject.getString("parent_user_id");
        String userId = jsonObject.getString("userId");
        String allUserIdStr = "";
        if (StringUtils.isNotEmpty(userId)){
            if (StringUtils.isNotEmpty(parentUserId)){
                allUserIdStr = String.format("%s OR %s", userId, parentUserId);
            } else {
                allUserIdStr = userId;
            }
        } else {
            if (StringUtils.isNotEmpty(parentUserId)){
                allUserIdStr = parentUserId;
            }
        }

        JSONObject param = new JSONObject();
        param.put("alg_report_type", jsonObject.getOrDefault("category",""));
        param.put("offset",(Integer) jsonObject.get("offset") * (Integer) jsonObject.get("limit"));
        param.put("limit", jsonObject.get("limit"));
        param.put("owner_id", allUserIdStr);
        param.put("prior", jsonObject.get("prior"));
        param.put("type", jsonObject.getString("source_type"));
        param.put("start_time", 0);
        param.put("keyword", "");
        if (!StringUtil.isEmpty(jsonObject.get("stockName"))) {
            param.put("alg_security_name", jsonObject.get("stockName"));
        } else if (!StringUtil.isEmpty(jsonObject.get("industry"))) {
            param.put("alg_industry_type", jsonObject.get("industry"));
        } else {
            return null;
        }
        param.put("alg_author_name", "");
        param.put("alg_publish_name", "");
        param.put("alg_rating", "");
        param.put("end_time", System.currentTimeMillis()/1000);
        param.put("file_type", jsonObject.get("file_type"));
        param.put("from", "");
        param.put("receivers", "");

        Response<MailReportSearchResponse> response = apiPlatFormClient.mailReportSearch(param.toJSONString());
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台搜索卖方研报失败,返回值：" + JSON.toJSONString(response) + ", param:" + param.toJSONString());
        }
        return getData(response.getData(), jsonObject);
    }

    @Override
    public JSONObject search(MailReportSearchVo mailReportSearchVo) throws Exception {
        SearchWordVo searchWord = mailReportSearchVo.getSearchWord();

        JSONObject param = new JSONObject();
        param.put("keyword", searchWord.getKeywordStr());
        param.put("start_time", searchWord.getStartTime());
        // 获取两结束时间最小者
        param.put("end_time", TimeUtil.getMinEndTime(mailReportSearchVo.getEndTime(), searchWord.getEndTime()));

        String parentUserId = mailReportSearchVo.getParentId();
        String userId = mailReportSearchVo.getUserId();
        String allUserIdStr = userId;
        if(StringUtils.isNotEmpty(parentUserId)){
            allUserIdStr = String.format("%s OR %s", userId, parentUserId);
        }
        param.put("owner_id", allUserIdStr);

        param.put("offset", mailReportSearchVo.getPageNum() * mailReportSearchVo.getPageSize());
        param.put("limit", mailReportSearchVo.getPageSize());
        param.put("type", ReportSearchConstant.SEARCH_TYPE_MAIL);
        param.put("from", searchWord.getFromStr());
        param.put("receivers", searchWord.getToStr());
        param.put("prior", mailReportSearchVo.getPriorStr());
        param.put("alg_industry_type", searchWord.getIndustryStr());
        param.put("alg_report_type", searchWord.getReportTypeStr());
        param.put("alg_author_name", searchWord.getAnalystStr());
        param.put("alg_publish_name", searchWord.getOrganizationStr());
        param.put("alg_rating", searchWord.getRateStr());
        param.put("alg_security_name", searchWord.getCompanyStr());
        param.put("file_type", mailReportSearchVo.getFile_type());

        Response<MailReportSearchResponse> response = apiPlatFormClient.mailReportSearch(param.toJSONString());
        if (response == null || response.getCode() != 200) {
            throw new RuntimeException("从平台搜索卖方研报失败,返回值：" + JSON.toJSONString(response) + ", param:" + param.toJSONString());
        }
        MailReportSearchResponse reportSearchResponse = response.getData();
        JSONObject result = new JSONObject();
        if (reportSearchResponse == null) {
            result.put("search_query", param.toString());
            return result;
        }

        result = (JSONObject) JSON.toJSON(reportSearchResponse);
        JSONArray jsonArray = result.getJSONArray("item");
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                long time = jsonItem.getLong("time");
                jsonItem.put("time", time / 1000);
                jsonItem.put("rating", jsonItem.getString("alg_rating"));
                jsonItem.put("industry_name", ReportUtil.getRedIndustryType(jsonItem));
                jsonItem.put("publish", jsonItem.getString("alg_publish_name"));
                jsonItem.put("report_type", ReportUtil.getRedReportType(jsonItem));
                jsonItem.put("author", jsonItem.getJSONArray("alg_author_name"));

                String fromAddress = jsonItem.getString("from_address");
                String fromName = jsonItem.getString("from_name");
                jsonItem.put("from", convertFromAddress(fromAddress, fromName));
                JSONArray toAddressArr = jsonItem.getJSONArray("receivers_address");
                JSONArray toNameArr = jsonItem.getJSONArray("receivers_address");
                jsonItem.put("receivers", convertToAddress(toAddressArr, toNameArr));
                String fileUrl = jsonItem.getString("file_url");
                jsonItem.put("file_url", StringUtils.isEmpty(fileUrl) ? "" : fileUrl.replace("-internal",""));
            }
        }
        result.put("search_query", param.toString());
        return result;
    }

    @Override
    public Map<String, Object> getCharts(String reportId) throws Exception {
        Response<List<MailReportChartsResponse>> responseChart = apiPlatFormClient.getMailReportChart(reportId);
        if (responseChart == null || responseChart.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的图表失败,返回值：" + JSON.toJSONString(responseChart) + ", reportId:" + reportId);
        }
        List<MailReportChartsResponse> charts = responseChart.getData();

        Response<List<MailReportTablesResponse>> responseTable = apiPlatFormClient.getMailReportTable(reportId);
        if (responseTable == null || responseTable.getCode() != 200) {
            throw new RuntimeException("从平台获取卖方研报的表格失败,返回值：" + JSON.toJSONString(responseTable) + ", reportId:" + reportId);
        }
        List<MailReportTablesResponse> tables = responseTable.getData();

        Integer total = 0;
        if (charts != null) {
            total += charts.size();
        }
        if (tables != null) {
            total += tables.size();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("charts", charts);
        result.put("tables", tables);
        result.put("total", total);
        return result;
    }

    public JSONObject getData(MailReportSearchResponse reportSearchResponse, JSONObject reportObj) throws Exception {
        if (reportSearchResponse == null) {
            return null;
        }

        JSONObject result = (JSONObject) JSON.toJSON(reportSearchResponse);
        result.put("abcscore",0);
        JSONArray categories = new JSONArray();
        result.put("categories", categories);
        result.put("current_page", reportObj.get("page"));
        Integer total_count = (Integer) result.get("total_count");
        double limit = (int)reportObj.get("limit");
        double total_page = total_count / limit;
        result.put("total_page",(int)Math.ceil(total_page));
        result.put("keyword","");
        result.put("m_keyword","");
        result.put("request_id","");
        JSONArray source = new JSONArray();
        result.put("source",source);
        JSONArray jsonList = result.getJSONArray("item");
        int size = jsonList.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonItem = jsonList.getJSONObject(i);
            long time = jsonItem.getLong("time");
            long realTime = time + 60 * 60 * 8;
            jsonItem.put("time", realTime/1000);
            String publish_at = (String) jsonItem.get("alg_publish_date");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy.MM.dd");
            Date date = new Date();
            try {
                if (publish_at == null){
                    jsonItem.put("alg_publish_date",date.getTime() / 1000);
                } else if (publish_at.contains("年")){
                    date = simpleDateFormat2.parse(publish_at);
                } else if (publish_at.contains(".")){
                    date = simpleDateFormat3.parse(publish_at);
                } else if (publish_at.contains("-")){
                    date = simpleDateFormat1.parse(publish_at);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            jsonItem.putIfAbsent("alg_publish_date",date.getTime()/1000);
            if (jsonItem.getLong("time") == 60 * 60 * 8L){
                jsonItem.put("time",jsonItem.get("alg_publish_date"));
            }
            jsonItem.put("publish_at",jsonItem.get("time"));
            jsonItem.put("file_type",jsonItem.get("filetype"));
            jsonItem.remove("filetype");
            jsonItem.put("source",jsonItem.get("alg_publish_name"));
            jsonItem.put("author", jsonItem.get("alg_author_name"));
            JSONArray analyst_honor = new JSONArray();
            jsonItem.put("analyst_honor",analyst_honor);
            jsonItem.put("industry1",jsonItem.get("alg_industry_type"));
            JSONArray honor = new JSONArray();
            honor.add(jsonItem.get("honor"));
            jsonItem.put("honor",honor);
            jsonItem.put("full_match",0);
            jsonItem.put("url",jsonItem.get("file_url"));
            if (jsonItem.get("url") == null){
                jsonItem.put("url","");
            }
            jsonItem.put("summarynum",0);
            jsonItem.put("create_at",jsonItem.get("time"));
            String category = reportObj.getString("category");
            jsonItem.put("category",category != null?category:jsonItem.getOrDefault("alg_report_type_three",""));
            JSONObject categoryTree = new JSONObject();
            jsonItem.put("categoryTree",categoryTree);
            jsonItem.put("uploaduser","");
            JSONArray paragraph = new JSONArray();
            jsonItem.put("paragraph",paragraph);
            jsonItem.put("industry","");
            String share_author = "";
            if (ReportSearchConstant.SEARCH_TYPE_MAIL.equals(reportObj.getString("source_type"))){
                share_author = jsonItem.getString("from_name");
            } else if (ReportSearchConstant.SEARCH_TYPE_WECHAT.equals(reportObj.getString("source_type"))){
                share_author = jsonItem.getString("sender_name");
            }
            jsonItem.put("share_author",share_author == null ? "":share_author);
            String title = jsonItem.getString("title");
            if (title == null){
                jsonItem.put("title",jsonItem.getString("source_title"));
            }
            if (jsonItem.get("title") == null){
                jsonItem.put("title","");
            }
            String category_id = jsonItem.getString("category_id");
            if (category_id == null){
                jsonItem.put("category_id","");
            }
            if (jsonItem.get("file_size") == null){
                jsonItem.put("file_size",0);
            }
            if (jsonItem.get("industry_id") == null){
                jsonItem.put("industry_id","");
            }
            jsonItem.put("checkStock","false");
            // 阅读数
            jsonItem.put("read_times",0);
            jsonItem.put("comment_times",0);
            // 分享数
            jsonItem.put("share_times",0);
            // 收藏数
            jsonItem.put("favorite_times",0);
            // 推荐数
            jsonItem.put("recommend_times",0);
            JSONArray tag = new JSONArray();
            jsonItem.put("tag",tag);
            jsonItem.put("stars",0);
            jsonItem.put("score",-1);
            if (jsonItem.get("stockname") == null){
                jsonItem.put("stockname",jsonItem.getString("alg_security_name"));
            }
            if (jsonItem.get("stockcode") == null){
                jsonItem.put("stockcode",jsonItem.getString("alg_security_id"));
            }
            if (jsonItem.get("stockname") == null){
                jsonItem.put("stockname","");
            }
            if (jsonItem.get("stockcode") == null){
                jsonItem.put("stockcode","");
            }
            if (jsonItem.get("rating") == null){
                if (jsonItem.getString("alg_rating") == null){
                    jsonItem.put("rating","");
                }else {
                    jsonItem.put("rating",jsonItem.getString("alg_rating"));
                }
            }
            if (jsonItem.get("report_type") == null){
                jsonItem.put("report_type","");
            }
        }
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonList.size(); i++) {
            jsonValues.add(jsonList.getJSONObject(i));
        }
        Collections.sort( jsonValues, (a, b) -> {
            Long valA = null;
            Long valB = null;
            try {
                valA = a.getLong("publish_at");
                valB = b.getLong("publish_at");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -valA.compareTo(valB);
        });
        result.put("items",jsonList);
        result.remove("item");

        return result;
    }

    private JSONObject convertFromAddress(String address, String name) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", address);
        jsonObject.put("name", name);
        return jsonObject;
    }

    private JSONArray convertToAddress(JSONArray addressArr, JSONArray nameArr) throws Exception {
        JSONArray receiverArr = new JSONArray();
        if (addressArr == null || nameArr == null) {
            return new JSONArray();
        }
        int size = Math.min(nameArr.size(), addressArr.size());
        for (int i = 0; i < size; i++) {
            String address = addressArr.getString(i);
            String name = nameArr.getString(i);
            JSONObject mailAddress = new JSONObject();
            mailAddress.put("address", address);
            mailAddress.put("name", name);
            receiverArr.add(mailAddress);
        }
        return receiverArr;
    }

    private String getAuthorStr(JSONArray array, String separator) throws Exception {
        if (array == null || array.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        array.forEach(str -> stringBuffer.append(str).append(separator));
        return stringBuffer.toString();
    }

    @Override
    public void saveReadLog(String userId, String type, String reportId, String author) throws Exception {
        ReadLogModel readLogModel = new ReadLogModel();
        readLogModel.setUserId(userId);
        readLogModel.setParentId(userInfoService.getUserParentId(userId));
        readLogModel.setArticleType(type);
        readLogModel.setArticleId(reportId);
        readLogModel.setArticleAuthor(author);
        Date now = new Date();
        readLogModel.setReadTime(now);
        readLogModel.setCreateId(userId);
        readLogModel.setUpdateId(userId);
        readLogModel.setCreateTime(now);
        readLogModel.setUpdateTime(now);

        sellerReportTransactionService.saveReadLog(readLogModel);
    }
}
