//package la.niub.abcapi.invest.platform.component.util;
//
//import la.niub.abcapi.invest.platform.config.SolrConfig;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.request.UpdateRequest;
//import org.apache.solr.client.solrj.response.Group;
//import org.apache.solr.client.solrj.response.GroupCommand;
//import org.apache.solr.client.solrj.response.GroupResponse;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.client.solrj.response.SpellCheckResponse;
//import org.apache.solr.client.solrj.response.UpdateResponse;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//import org.apache.solr.common.params.CommonParams;
//import org.apache.solr.common.params.DisMaxParams;
//import org.apache.solr.common.params.GroupParams;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * solr操作
// * @author xwu.abcft
// */
//@Component
//public class SolrUtil {
//
//    static Logger logger = LogManager.getLogger(SolrUtil.class);
//
//    @Autowired
//    private SolrClient solrClient;
////    private ConcurrentUpdateSolrClient solrClient;
//
//    public void setSolrClient(SolrClient solrClient) {
//        this.solrClient = solrClient;
//    }
//
//    //请求字串
//    private ThreadLocal<String> query_string = new ThreadLocal<>();
//
//    public String getQuery_string() {
//        return query_string.get();
//    }
//
//    public void setQuery_string(String query_string) {
//        this.query_string.set(query_string);
//    }
//
//    /**
//     * 生成ID
//     * @return
//     */
//    private String generateId(){
//        return RandomUtil.generateId();
//    }
//
//    /**
//     * 预处理keyword
//     * @param keyword
//     * @return
//     */
//    private String handleKeyword(String keyword){
//        if ((keyword.indexOf("*") < 0) && (keyword.indexOf("?") < 0)){
//            return "\""+keyword+"\"";
//        }
//        return keyword;
//    }
//
//    /**
//     * 添加数据
//     * @param data
//     * @return 添加行数
//     */
//    public <T> Integer addObject(String collection,T data){
//        logger.info("SolrUtil begin add one item for collection ["+collection+"] from Bean data "+data);
//        List<T> datas = new ArrayList<>();
//        datas.add(data);
//        return addObject(collection,datas);
//    }
//
//    /**
//     * 批量添加数据对象
//     * @param data
//     * @return
//     */
//    public <T> Integer addObject(String collection,List<T> data){
//        if (data.isEmpty()){
//            logger.info("SolrUtil begin add total "+data.size()+" items for collection ["+collection+"] from List<Bean> empty!");
//            return 0;
//        }
//        logger.info("SolrUtil begin add total "+data.size()+" items for collection ["+collection+"] from List<Bean> data "+data.get(0));
//
//        for (T row : data){
//            try {
//                Field field = row.getClass().getDeclaredField("id");
//                field.setAccessible(true);
//                if (field.get(row) == null){
//                    field.set(row,generateId());
//                }
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            logger.info("SolrUtil start adding");
//            UpdateResponse result = solrClient.addBeans(collection,data,1);
////            UpdateResponse result = solrClient.commit(collection,false,false);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil add items success");
//                return data.size();
//            }else{
//                logger.info("SolrUtil commit fail");
//            }
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//
//        logger.error("SolrUtil add items fail");
//        return 0;
//    }
//
//    /**
//     * 添加数据
//     * @param data
//     * @return 添加行数
//     */
//    public Integer addMap(String collection,Map<String,Object> data){
//        logger.info("SolrUtil begin add one item for collection ["+collection+"] from Map data "+data);
//
//        List<Map<String,Object>> datas = new ArrayList<>();
//        datas.add(data);
//        return addMap(collection,datas);
//    }
//
//    /**
//     * 批量添加数据
//     * @param data
//     * @return 添加行数
//     */
//    public Integer addMap(String collection,List<Map<String,Object>> data){
//        if (data.isEmpty()){
//            logger.info("SolrUtil begin add total "+data.size()+" items for collection ["+collection+"] from List<Map> empty!");
//            return 0;
//        }
//        logger.info("SolrUtil begin add total "+data.size()+" items for collection ["+collection+"] from List<Map> data "+data.get(0));
//
//        UpdateRequest request = new UpdateRequest();
//
//        for (Map<String,Object> row : data){
//            SolrInputDocument doc = new SolrInputDocument();
//            for (Map.Entry<String,Object> entry : row.entrySet()){
//                doc.setField(entry.getKey(), entry.getValue());
//            }
//            if (!doc.containsKey("id")){
//                doc.setField("id", generateId());
//            }
//
//            request.add(doc);
//        }
//
//        try {
//            logger.info("SolrUtil start adding");
//            request.setCommitWithin(SolrConfig.SOLR_DELAY_COMMIT);
//            UpdateResponse result = request.process(solrClient,collection);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil add items success");
//                return data.size();
//            }else{
//                logger.info("SolrUtil commit fail");
//            }
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//
//        logger.error("SolrUtil add items fail");
//        return 0;
//    }
//
//    /**
//     * 获取集合总记录数
//     * @return
//     */
//    public Boolean delete(String collection,String query){
//        logger.info("SolrUtil delete begin for collection ["+collection+"], query: "+query);
//        if (query == "*:*"){
//            logger.warn("SolrUtil delete not allow for *:*");
//            return false;
//        }
//        UpdateResponse result = null;
//        try {
//            result = solrClient.deleteByQuery(collection,query,SolrConfig.SOLR_DELAY_COMMIT);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil delete ok");
//                return true;
//            }else{
//                logger.warn("SolrUtil delete fail");
//            }
//        } catch (SolrServerException | IOException e) {
//            logger.warn("SolrUtil delete error");
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 搜索单个
//     * @param collection
//     * @param query_str
//     * @return
//     */
//    public <T> T searchOne(String collection, Class<T> clz, String query_str){
//        List<T> result = search(collection,clz,query_str,null,0,1);
//        if (result.size() == 0){
//            return null;
//        }else if (result.size() > 1){
//            logger.warn("SolrUtil searchOne result size is more than one");
//        }
//        return result.get(0);
//    }
//
//    /**
//     * 根据搜索字串和排序字串搜索结果
//     * @param query_str 搜索字串
//     * @param sort_str 排序字串
//     * @param start 位移
//     * @param rows 每页数目
//     * @return
//     */
//    public <T> List<T> search(String collection, Class<T> clz, String query_str, String sort_str,Integer start,Integer rows){
//        logger.info("SolrUtil begin search for collection: ["+collection+"]"
//                + ", query_str: " + query_str
//                + ", sorts: " + sort_str
//                + ", start: " + start
//                + ", rows: " + rows);
//
//        SolrQuery query = new SolrQuery();
//        query.setQuery(query_str);
//        if (sort_str != null && !sort_str.isEmpty()){
//            query.set(CommonParams.SORT, sort_str);
//        }
//        query.setStart(start);
//        query.setRows(rows);
//
//        setQuery_string(query_str+" sort [ "+sort_str+" ] from collection [ "+collection+" ]");
//
//        List<T> resultList = new ArrayList<>();
//        try {
//            logger.info("SolrUtil start querying");
//            QueryResponse result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil search fail");
//                return resultList;
//            }
//
//            logger.info("SolrUtil getting result documents");
//            resultList = solrClient.getBinder().getBeans(clz, result.getResults());
//            if (resultList == null || resultList.isEmpty()) {
//                logger.warn("SolrUtil search result is empty");
//                return resultList;
//            }
//
//            logger.info("SolrUtil success find "+resultList.size()+" items");
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//            logger.error("SolrUtil search fail");
//        }
//
//        logger.info("SolrUtil search first result: {}",resultList.get(0));
//        return resultList;
//    }
//
//    /**
//     * 根据单个条件语句和排序搜索
//     * @param field
//     * @param keyword
//     * @return
//     */
//    public <T> List<T> search(String collection, Class<T> clz,String field, String keyword){
//        logger.info("SolrUtil begin search field ["+field+"] for keyword ["+keyword+"]");
//        return search(collection,clz,field + ":(" + handleKeyword(keyword) + ")",SolrConfig.SOLR_SORT_DEFAULT_FIELD + " " + SolrConfig.SOLR_SORT_DEFAULT_ORDER,0,SolrConfig.SOLR_DEFAULT_LIMIT);
//    }
//
//    /**
//     * 获取集合总记录数
//     * @param collection
//     * @return
//     */
//    public Long count(String collection){
//        return count(collection,"*:*","");
//    }
//    public Long count(String collection,String query_str,String filtering_query){
//        logger.info("SolrUtil begin count for collection: ["+collection+"]"
//                + ", query_str: " + query_str);
//
//        SolrQuery query = new SolrQuery();
//        query.setQuery(query_str);
//        if (StringUtils.isNotEmpty(filtering_query)){
//            query.setFilterQueries(filtering_query);
//        }
//        QueryResponse result = null;
//        try {
//            result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil count fail");
//                return null;
//            }
//            SolrDocumentList documentList = result.getResults();
//            return documentList.getNumFound();
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 高级搜索排序
//     * @param query_values 查询值
//     * @param query_fields 查询字段和打分权重
//     * @param filtering_query 查询后过滤条件
//     * @param boosting_functions 打分函数
//     * @param rows 返回数
//     * @return
//     */
//    public <T> List<T> seniorSearch(String collection, Class<T> clz,String query_values,String query_fields, String sort_str,String filtering_query,String boost_query,String boosting_functions,Integer rows){
//        logger.info("SolrUtil begin seniorSearch for collection: ["+collection+"]"
//                + ", query_values: " + query_values
//                + ", query_fields: " + query_fields
//                + ", sort_str: " + sort_str
//                + ", filtering_query: " + filtering_query
//                + ", boost_query: " + boost_query
//                + ", boosting_functions: " + boosting_functions
//                + ", rows: " + rows);
//
//        SolrQuery query = new SolrQuery();
//        query.setQuery(query_values);
//        query.set("defType","edismax");
//        query.set(DisMaxParams.QF,query_fields);//"title^1 price^0.1"
//        query.set(CommonParams.DF,"_text_");
////        query.set(GroupParams.GROUP,true);
////        query.set(GroupParams.GROUP_FIELD,"content");
//        if (sort_str != null && !sort_str.isEmpty()){
//            query.set(CommonParams.SORT, sort_str);
//        }
//        if (filtering_query != null && !filtering_query.isEmpty()){
//            query.set(CommonParams.FQ, filtering_query);
//        }
//        if (boost_query != null && !boost_query.isEmpty()){
//            query.set(DisMaxParams.BQ, boost_query);
//        }
//        if (boosting_functions != null && !boosting_functions.isEmpty()){
//            query.set(DisMaxParams.BF, boosting_functions);//"sum(abs(price))"
//        }
//        query.setRows(rows);
//
//        setQuery_string("[ "+query_values+" ] field [ "+query_fields+" ] sort [ "+sort_str+" ] filter [ "+filtering_query+" ] boost_query [ "+boost_query+" ] boosting [ "+boosting_functions+" ] from collection [ "+collection+" ]");
//
//        List<T> resultList = new ArrayList<>();
//        try {
//            logger.info("SolrUtil start querying");
//            QueryResponse result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil seniorSearch fail");
//                return resultList;
//            }
//
//            logger.info("SolrUtil getting result documents");
//            resultList = solrClient.getBinder().getBeans(clz, result.getResults());
//            if (resultList == null || resultList.isEmpty()) {
//                logger.warn("SolrUtil seniorSearch result is empty");
//                return resultList;
//            }
//
//            logger.info("SolrUtil success find "+resultList.size()+" items");
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//            logger.error("SolrUtil seniorSearch fail");
//        }
//
//        logger.info("SolrUtil seniorSearch first result: {}",resultList.get(0));
//        return resultList;
//    }
//
//    /**
//     * 搜索推荐
//     * @param keyword
//     * @param limit
//     * @return
//     */
//    public List<String> suggest(String collection,String keyword,Integer limit){
//        logger.info("SolrUtil suggest begin collection ["+collection+"] for keyword ["+keyword+"] limit ["+limit+"]");
//
//        SolrQuery query = new SolrQuery();
//        query.set(CommonParams.QT,"/suggest");
//        query.set("suggest.q", keyword);
//        query.set("suggest.count", limit);
//
//        List<String> resultList = new ArrayList<>();
//        try {
//            logger.info("SolrUtil suggest start querying");
//            QueryResponse result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil suggest search fail");
//                return resultList;
//            }
//
//            logger.info("SolrUtil suggest getting result items");
//            Map<String, List<String>> suggested_terms = result.getSuggesterResponse().getSuggestedTerms();
//            resultList = suggested_terms.get("default");
//            if (resultList == null || resultList.isEmpty()) {
//                logger.warn("SolrUtil suggest search result is empty");
//                return resultList;
//            }
//
//            logger.info("SolrUtil suggest success find "+resultList.size()+" items");
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//            logger.error("SolrUtil suggest search fail");
//        }
//
//        if (resultList.isEmpty()){
//            logger.warn("SolrUtil suggest search result is empty");
//        }
//        logger.info("SolrUtil suggest search result: "+resultList.toString());
//        return resultList;
//    }
//
//    /**
//     * 拼写检查
//     * @param keyword
//     * @param limit
//     * @return
//     */
//    public List<String> spellcheck(String collection,String keyword,Integer limit,Float accuracy){
//        logger.info("SolrUtil spellcheck begin collection ["+collection+"] for keyword ["+keyword+"] limit ["+limit+"]");
//
//        SolrQuery query = new SolrQuery();
//        query.set(CommonParams.QT,"/spellcheck");
//        query.set("spellcheck.q", keyword);
//        query.set("spellcheck.count", limit);
//        query.set("spellcheck.accuracy", String.valueOf(accuracy));
//
//        List<String> resultList = new ArrayList<>();
//        try {
//            logger.info("SolrUtil spellcheck start querying");
//            QueryResponse result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil spellcheck search fail");
//                return resultList;
//            }
//
//            logger.info("SolrUtil spellcheck getting result items");
//            List<SpellCheckResponse.Suggestion> spellcheck_suggests = result.getSpellCheckResponse().getSuggestions();
//            if (spellcheck_suggests.isEmpty()) {
//                logger.warn("SolrUtil spellcheck search result is empty");
//                return resultList;
//            }
//            SpellCheckResponse.Suggestion spellcheck_suggest = spellcheck_suggests.get(0);
//            resultList = spellcheck_suggest.getAlternatives();
//
//            logger.info("SolrUtil spellcheck success find "+resultList.size()+" items");
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//            logger.error("SolrUtil spellcheck search fail");
//        }
//
//        if (resultList.isEmpty()){
//            logger.warn("SolrUtil spellcheck search result is empty");
//        }
//        logger.info("SolrUtil spellcheck search result: "+resultList.toString());
//        return resultList;
//    }
//
//    /**
//     * 优化集合索引
//     * @param collection
//     * @throws SolrServerException
//     * @throws IOException
//     */
//    public void optimize(String collection) throws SolrServerException, IOException {
//        logger.info("SolrUtil optimize begin for collection "+collection);
//        Long start = System.currentTimeMillis();
//        solrClient.optimize(collection);
//        Long end = System.currentTimeMillis();
//        logger.info("spent time "+(end - start)/1000+" seconds");
//        logger.info("SolrUtil optimize end");
//    }
//
//    /**
//     * 高级分组搜索排序
//     * @param query_values 查询值
//     * @param query_fields 查询字段和打分权重
//     * @param filtering_query 查询后过滤条件
//     * @param boosting_functions 打分函数
//     * @param rows 返回数
//     * @return
//     */
//    public <T> Map<String, List<T>> seniorGroupSearch(String collection, Class<T> clz,String group_field,String query_values,String query_fields, String sort_str,String filtering_query,String boost_query,String boosting_functions,Integer rows){
//        logger.info("SolrUtil begin seniorGroupSearch for collection: ["+collection+"]"
//                + ", query_values: " + query_values
//                + ", query_fields: " + query_fields
//                + ", group_field: " + group_field
//                + ", sort_str: " + sort_str
//                + ", filtering_query: " + filtering_query
//                + ", boost_query: " + boost_query
//                + ", boosting_functions: " + boosting_functions
//                + ", rows: " + rows);
//
//        SolrQuery query = new SolrQuery();
//        query.setQuery(query_values);
//        query.set("defType","edismax");
//        query.set(DisMaxParams.QF,query_fields);//"title^1 price^0.1"
//        query.set(CommonParams.DF,"_text_");
//        query.set(GroupParams.GROUP,true);
//        query.set(GroupParams.GROUP_FIELD,group_field);
//        query.set(GroupParams.GROUP_LIMIT,rows);
//        if (sort_str != null && !sort_str.isEmpty()){
//            query.set(CommonParams.SORT, sort_str);
//        }
//        if (filtering_query != null && !filtering_query.isEmpty()){
//            query.set(CommonParams.FQ, filtering_query);
//        }
//        if (boost_query != null && !boost_query.isEmpty()){
//            query.set(DisMaxParams.BQ, boost_query);
//        }
//        if (boosting_functions != null && !boosting_functions.isEmpty()){
//            query.set(DisMaxParams.BF, boosting_functions);//"sum(abs(price))"
//        }
////        query.setRows(rows);
//
//        setQuery_string("[ "+query_values+" ] field [ "+query_fields+" ] group_field [ "+group_field+" ] sort [ "+sort_str+" ] filter [ "+filtering_query+" ] boost_query [ "+boost_query+" ] boosting [ "+boosting_functions+" ] from collection [ "+collection+" ]");
//
//        Map<String, List<T>> resultMap = new LinkedHashMap<>();
//        try {
//            logger.info("SolrUtil start querying");
//            QueryResponse result = solrClient.query(collection,query);
//            if (result.getStatus() == 0){
//                logger.info("SolrUtil query ok");
//            }else{
//                logger.warn("SolrUtil seniorGroupSearch fail");
//                return resultMap;
//            }
//
//            logger.info("SolrUtil getting result documents");
//            GroupResponse groupResponse = result.getGroupResponse();
//            Integer commandNum = 0;
//            Integer groupNum = 0;
//            Integer resultNum = 0;
//            if(groupResponse != null) {
//                List<GroupCommand> groupList = groupResponse.getValues();
//                commandNum += groupList.size();
//                for(GroupCommand groupCommand : groupList) {
//                    List<Group> groups = groupCommand.getValues();
//                    groupNum += groups.size();
//                    for(Group group : groups) {
//                        resultMap.put(group.getGroupValue(), solrClient.getBinder().getBeans(clz, group.getResult()));
//                        resultNum += (int)group.getResult().getNumFound();
//                    }
//                }
//            }else{
//                logger.warn("SolrUtil seniorGroupSearch result is empty");
//            }
//
//            logger.info("SolrUtil success find group commandNum: "+commandNum+" groupNum: "+groupNum+" resultNum: "+resultNum+" items");
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//            logger.error("SolrUtil seniorGroupSearch fail");
//        }
//
//        logger.info("SolrUtil seniorGroupSearch result: {}",resultMap);
//        return resultMap;
//    }
//}
