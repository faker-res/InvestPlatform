package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.*;
import la.niub.abcapi.invest.seller.model.*;
import la.niub.abcapi.invest.seller.service.ITraderMarkJobTransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 券商评分定时任务事务管理service
 * @date : 2019-01-26 11:56
 */
@Service
public class TraderMarkJobTransactionServiceImpl implements ITraderMarkJobTransactionService {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private TraderMarkTaskMapper taskMapper;

    @Autowired
    private TraderMarkTaskDetailMapper taskDetailMapper;

    @Autowired
    private TraderMarkObjectiveDataDetailMapper objectiveDataDetailMapper;

    @Autowired
    private TraderMarkObjectiveDataBrokerMapper objectiveDataBrokerMapper;

    @Autowired
    private TraderMarkTaskPersonnelMapper taskPersonnelMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public void save(List<TraderMarkTaskModel> taskModelList,
                     Map<String, List<TraderMarkTaskPersonnelModel>> taskPersonnelMap,
                     Map<String, List<TraderMarkTaskDetailModel>> taskDetailMap,
                     Map<String, List<TraderMarkObjectiveDataDetailModel>> objectiveDataDetailMap,
                     Map<String, List<TraderMarkObjectiveDataBrokerModel>> objectiveDataBrokerMap) throws Exception {
        for (TraderMarkTaskModel taskModel : taskModelList) {
            String parentId = taskModel.getParentId();

            logger.info("save parentId:{},task data", parentId);
            taskMapper.insertSelective(taskModel);

            Long taskId = taskModel.getId();
            logger.info("parentId:{}, taskId:{}", parentId, taskId);

            logger.info("save parentId:{},taskPersonnel data", parentId);
            List<TraderMarkTaskPersonnelModel> taskPersonnelModelList = taskPersonnelMap.get(parentId);
            taskPersonnelMapper.insertBatch(taskPersonnelModelList, taskId);

            logger.info("save parentId:{},taskDetail data", parentId);
            List<TraderMarkTaskDetailModel> taskDetailModelList = taskDetailMap.get(parentId);
            taskDetailMapper.insertBatch(taskDetailModelList, taskId);

            logger.info("save parentId:{},objectiveDataDetail data", parentId);
            List<TraderMarkObjectiveDataDetailModel> objectiveDataDetailModelList = objectiveDataDetailMap.get(parentId);
            objectiveDataDetailMapper.insertBatch(objectiveDataDetailModelList, taskId);

            logger.info("save parentId:{},objectiveDataBroker data", parentId);
            List<TraderMarkObjectiveDataBrokerModel> objectiveDataBrokerModelList = objectiveDataBrokerMap.get(parentId);
            objectiveDataBrokerMapper.insertBatch(objectiveDataBrokerModelList, taskId);
        }
    }
}
