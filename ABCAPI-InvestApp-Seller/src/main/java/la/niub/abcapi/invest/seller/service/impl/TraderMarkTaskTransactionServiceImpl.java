package la.niub.abcapi.invest.seller.service.impl;

import la.niub.abcapi.invest.seller.dao.invest.TraderMarkStatisticsDetailMapper;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkTaskDetailMapper;
import la.niub.abcapi.invest.seller.dao.invest.TraderMarkTaskMapper;
import la.niub.abcapi.invest.seller.model.TraderMarkStatisticsDetailModel;
import la.niub.abcapi.invest.seller.model.TraderMarkTaskModel;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveItemVo;
import la.niub.abcapi.invest.seller.service.TraderMarkTaskTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : ppan
 * @description : 券商评分增删改事务service(线上环境是主从配置,在同一个事务中同时存在读和增删改操作，会报错,原因:有读操作时运维将整个事务放到从库)
 * @date : 2019-01-30 09:25
 */
@Service
public class TraderMarkTaskTransactionServiceImpl implements TraderMarkTaskTransactionService {

    @Autowired
    private TraderMarkTaskMapper taskMapper;

    @Autowired
    private TraderMarkTaskDetailMapper taskDetailMapper;

    @Autowired
    private TraderMarkStatisticsDetailMapper statisticsDetailMapper;

    @Override
    @Transactional(transactionManager = "investTransactionManager", rollbackFor = Exception.class)
    public Integer saveScore(Long taskId, String userId, List<TraderMarkTaskSaveItemVo> scoreList, List<TraderMarkStatisticsDetailModel> statisticsDetailModelList) throws Exception {
        int result1 = 0;
        if (scoreList != null && scoreList.size() > 0) {
            result1 = taskDetailMapper.updateScoreByIdList(scoreList);
            if (statisticsDetailModelList != null && statisticsDetailModelList.size() > 0) {
                TraderMarkTaskModel taskModel = new TraderMarkTaskModel();
                taskModel.setId(taskId);
                taskModel.setStatus(1);

                int result2 = taskMapper.updateByPrimaryKeySelective(taskModel);
                int result3 = statisticsDetailMapper.insertBatch(statisticsDetailModelList);

                result1 = result1 + result2 + result3;
            }
        }
        return result1;
    }
}
