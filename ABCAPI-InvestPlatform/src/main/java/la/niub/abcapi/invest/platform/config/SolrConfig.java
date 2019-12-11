//package la.niub.abcapi.invest.platform.config;
//
//import org.apache.solr.client.solrj.SolrQuery;
//
///**
// * solr配置
// */
//public class SolrConfig {
//
//    /**
//     * 默认延迟提交时间，毫秒
//     */
//    public static final Integer SOLR_DELAY_COMMIT = 1000*5;
//
//    /**
//     * 默认操作的集合
//     */
//    public static final String SOLR_COLLECTION_QUERY = "autocomplete_core";
//
//    /**
//     * 用户记录的集合
//     * @deprecated
//     */
//    public static final String SOLR_COLLECTION_QUERYLOG = "querylog_core";
//
//    /**
//     * 个股的集合
//     */
//    public static final String SOLR_COLLECTION_STOCK = "stock_core";
//
//    /**
//     * 个股第二版（自选股项目用到）
//     */
//    public static final String SOLR_COLLECTION_STOCK_V2 = "stock_v2_core";
//
//    /**
//     * 行业的集合
//     */
//    public static final String SOLR_COLLECTION_INDUSTRY = "industry_core";
//
//    /**
//     * 概念的集合
//     */
//    public static final String SOLR_COLLECTION_PLATE = "plate_core";
//
//    /**
//     * query+实体的集合
//     */
//    public static final String SOLR_COLLECTION_RECOMMEND = "recommend_core";
//
//    /**
//     * 默认权重
//     */
//    public static final Integer SOLR_DEFAULT_WEIGHT = 999999;
//    public static final Integer SOLR_DEFAULT_WEIGHT_V2 = 55000;
//
//    /**
//     * 查询默认返回数量
//     */
//    public static final Integer SOLR_DEFAULT_LIMIT = CommonConfig.SEARCH_DEFAULT_LIMIT;
//
//    /**
//     * 后台默认的user_id
//     */
//    public static final String SOLR_ADMIN_USERID = "0";
//
//    /**
//     * 后台默认的type
//     */
//    public static final Integer SOLR_SEARCH_DEFAULT_TYPE = 0;
//
//    /**
//     * 默认排序的字段
//     */
//    public static final String SOLR_SORT_DEFAULT_FIELD = "weight";
//
//    /**
//     * 默认排序的方向
//     */
//    public static final SolrQuery.ORDER SOLR_SORT_DEFAULT_ORDER = SolrQuery.ORDER.desc;
//
//    /**
//     * 默认spellcheck精度
//     */
//    public static final Float SOLR_SPELLCHECK_DEFAULT_ACCURACY = 0.7f;
//
//    /**
//     * 默认实体更新间隔
//     */
//    public static final Integer SOLR_DEFAULT_ENTITY_UPDATE_INTERVAL = 3600;
//}
