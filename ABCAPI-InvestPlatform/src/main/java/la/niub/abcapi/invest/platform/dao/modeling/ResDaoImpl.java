//package la.niub.abcapi.invest.platform.dao.modeling;
//
//import com.mongodb.WriteResult;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import la.niub.abcapi.invest.platform.service.impl.MailServiceImpl;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * @author Jenkin.K
// * @date 2017/11/23
// */
//@Repository
//public class ResDaoImpl {
//
//    private final static Logger logger = LogManager.getLogger(ResDaoImpl.class);
//
//    @Autowired
//    @Qualifier("modelingResMongo")
//    protected MongoTemplate mongoTemplate;
//
//    public <T> T findOne(String collectionName, Class<T> entityClass,Query query){
//        PageRequest pageRequest = new PageRequest(0, 1);
//        List<T> resultList = findByPage(collectionName, entityClass, query,pageRequest);
//        if (resultList.size() > 1) {
//            logger.error("expect one object, but actually [{}].", resultList.size());
//        }
//        if (resultList.size() == 0) {
//            return null;
//        }
//        return resultList.get(0);
//    }
//
//    public <T> List<T> findByConditions(String collectionName, Class<T> entityClass, Query query) {
//        return mongoTemplate.find(query, entityClass, collectionName);
//    }
//
//    public <T> List<T> findByPage (String collectionName, Class<T> entityClass, Query query, Pageable pageable) {
//        if (pageable == null) {
//            return findByConditions(collectionName, entityClass, query);
//        }
//        return mongoTemplate.find(query.with(pageable), entityClass);
//    }
//
////    public List<?> distinct(String collectionName,String filed,Class<?> entityClass,DBObject query){
////        return mongoTemplate.getCollection(collectionName).distinct(filed,query);
////    }
//
//    public void save(String collectionName, Object objectToSave) {
//        mongoTemplate.save(objectToSave, collectionName);
//    }
//
//    public void insert(String collectionName, Object objectToSave) {
//        mongoTemplate.insert(objectToSave, collectionName);
//    }
//
//    public Boolean delete(String collection, Query query) {
//        WriteResult result =  mongoTemplate.remove(query, collection);
//        return result.wasAcknowledged() ? true : false;
//    }
//
//    public WriteResult delete(Class<?> entityClass, Query query) {
//        return mongoTemplate.remove(query, entityClass);
//    }
//
//    /**
//     * 计算总数
//     *
//     * @param collectionName
//     * @param entityClass
//     * @param query
//     * @return
//     */
//    public Long count(String collectionName, Class<?> entityClass, Query query) {
//        return mongoTemplate.count(query, entityClass);
//    }
//
//    /**
//     * 更新字段
//     *
//     * @param collectionName 集合
//     * @param query          查询
//     * @param update         更新
//     */
//    public WriteResult updateOne(String collectionName, Query query, Update update) {
//        return mongoTemplate.updateFirst(query, update, collectionName);
//    }
//
//    /**
//     * @author: chenliang
//     * @param: [collectionName, query, update]
//     * @return: com.mongodb.client.result.UpdateResult
//     * @Description:修改所有满足条件数据
//     * @date: 9/27/18 20:25
//     */
//    public WriteResult updateMulti(String collectionName, Query query, Update update) {
//        return mongoTemplate.updateMulti(query, update, collectionName);
//    }
//
//}
