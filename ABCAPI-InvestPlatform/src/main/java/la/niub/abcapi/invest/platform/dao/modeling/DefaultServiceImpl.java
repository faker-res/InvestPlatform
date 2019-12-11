//package la.niub.abcapi.invest.platform.dao.modeling;
//
//import com.abc.mg.service.*;
//import com.abc.mg.service.dao.impl.DefaultDaoImpl;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//
///**
// * Service进行统一缓存
// *
// * @author Jenkin.K
// * @date 2017/12/4
// */
//@Service
//public class DefaultServiceImpl implements DefaultService {
//    Logger logger = LoggerFactory.getLogger(DefaultServiceImpl.class);
//
//    @Autowired
//    @Lazy
//    private ResDaoImpl defaultDao;
//
//    /**
//     * 根据条件获取一个
//     * 实际返回:查询有多个就返回第多个.
//     *
//     * @param collectionName
//     * @param entityClass
//     * @param query
//     * @return
//     */
//    @Override
//    public ResultMsg findOneByConditions(String collectionName, Class<?> entityClass, LinkedHashMap<String, Document> query) {
//        try {
//            PageRequest pageRequest = PageRequest.of(0, 1);
//            List<?> resultList = defaultDao.findByPage(collectionName, entityClass, Query.deserialize(query), pageRequest);
//            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, resultList);
//        } catch (Exception e) {
//            logger.error("findOneByConditions error. collectionName:[{}], query:[{}]", collectionName, Query.deserialize(query).toString());
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//
//    }
//
//    /**
//     * 分页按条件获取, 查询多个都用此接口.如果不需分页,pageableMode为null即可
//     * 如需排序,加载query中
//     *
//     * @param collectionName
//     * @param entityClass
//     * @param query
//     * @param pageableMode
//     * @return
//     */
//    @Override
//    public ResultMsg findAllByPage(String collectionName, Class<?> entityClass, LinkedHashMap<String, Document> query, PageableMode pageableMode) {
//        try {
//            Pageable pageable = null;
//            if (pageableMode != null) {
//                pageable = PageRequest.of(pageableMode.getPageNum(), pageableMode.getPageSize());
//            }
//            List<?> resultList = defaultDao.findByPage(collectionName, entityClass, Query.deserialize(query), pageable);
//            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, resultList);
//        } catch (Exception e) {
//            logger.error("findAllByPage error. collectionName:[{}], query:[{}], pageableMode:[{}].", collectionName, Query.deserialize(query).toString(), pageableMode.toString(), e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
//    /**
//     * @param collectionName
//     * @param objectToSave
//     * @return
//     */
//    @Override
//    public ResultMsg insertOne(String collectionName, Object objectToSave) {
//        try {
//            defaultDao.insert(collectionName, objectToSave);
//            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, null);
//        } catch (Exception e) {
//            logger.error("insertOne error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//
//    }
//
//    /**
//     * @param collectionName
//     * @param query
//     * @param document
//     * @return
//     */
//    @Override
//    public ResultMsg upsertOne(String collectionName, LinkedHashMap<String, Document> query, Document document) {
//
//        return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, "not support now.");
//    }
//
//    /**
//     * @param collectionName
//     * @param query
//     * @return
//     */
//    @Override
//    public ResultMsg delete(String collectionName, LinkedHashMap<String, Document> query) {
//        try {
//            DeleteResult result = defaultDao.delete(collectionName, Query.deserialize(query));
//            if (result.wasAcknowledged()) {
//                return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, result.toString());
//            } else {
//                return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, result.toString());
//            }
//        } catch (Exception e) {
//            logger.error("save error. collectionName:[{}]. query:[{}]", collectionName, Query.deserialize(query).toString());
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
//    @Override
//    public ResultMsg delete(Class<?> entityClass, LinkedHashMap<String, Document> query) {
//        try {
//            DeleteResult result = defaultDao.delete(entityClass, Query.deserialize(query));
//            if (result.wasAcknowledged()) {
//                return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, result.toString());
//            } else {
//                return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, result.toString());
//            }
//        } catch (Exception e) {
//            logger.error("delete error. query:[{}]", Query.deserialize(query).toString());
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
//    /**
//     * @param collectionName
//     * @param objectToSave
//     * @return
//     */
//    @Override
//    public ResultMsg save(String collectionName, Object objectToSave) {
//        try {
//            defaultDao.save(collectionName, objectToSave);
//            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, objectToSave);
//        } catch (Exception e) {
//            logger.error("save error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
//    @Override
//    public ResultMsg count(String collectionName, Class<?> entityClass, LinkedHashMap<String, Document> query) {
//        try {
//            Long count = defaultDao.count(collectionName, entityClass, Query.deserialize(query));
//            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, count);
//        } catch (Exception e) {
//            logger.error("count error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
//    public ResultMsg updateOne(String collectionName, LinkedHashMap<String, Document> query, Update update) {
//        try {
//            defaultDao.updateOne(collectionName, Query.deserialize(query), update);
//            return new ResultMsg(ResultMsg.RESULT_OK, null, null);
//        } catch (Exception e) {
//            logger.error("count error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//
//    }
//
//    @Override
//    public ResultMsg update(String collectionName, LinkedHashMap<String, Document> query, MyUpdate update) {
//        try {
//            UpdateResult r = defaultDao.updateOne(collectionName, Query.deserialize(query), update);
//            return new ResultMsg(ResultMsg.RESULT_OK, null, r.toString());
//        } catch (Exception e) {
//            logger.error("count error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//
////    @Override
////    public ResultMsg distinct(String collectionName,String filed,Class<?> entityClass,DBObject query){
////        try {
////            List<?> resultList = defaultDao.distinct(collectionName,filed,entityClass,query);
////            return new ResultMsg(ResultMsg.RESULT_OK, ResultMsg.OK_MSG, resultList);
////        } catch (Exception e) {
////            logger.error("distinct error. collectionName:[{}]. exception:[{}]", collectionName, e);
////            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
////        }
////    }
//
//
//    /**
//     * @param collectionName
//     * @param query
//     * @param update
//     * @author: chenliang
//     * @param: [collectionName, query, update]
//     * @return: com.abc.mg.service.ResultMsg
//     * @Description:修改所有满足条件数据
//     * @date: 9/27/18 20:24
//     */
//    @Override
//    public ResultMsg updateMulti(String collectionName, LinkedHashMap<String, Document> query, MyUpdate update) {
//        try {
//            UpdateResult r = defaultDao.updateMulti(collectionName, Query.deserialize(query), update);
//            return new ResultMsg(ResultMsg.RESULT_OK, null, r.toString());
//        } catch (Exception e) {
//            logger.error("count error. collectionName:[{}]. exception:[{}]", collectionName, e);
//            return new ResultMsg(ResultMsg.RESULT_ERROR, ResultMsg.ERROR_MSG, e);
//        }
//    }
//}
