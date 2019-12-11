package la.niub.abcapi.invest.platform.component.job;

import la.niub.abcapi.invest.platform.dao.invest.IInvestnewRecommendedStockAttachmentDao;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewStockIndexDao;
import la.niub.abcapi.invest.platform.model.invest.InvestnewRecommendedStockAttachmentModel;
import la.niub.abcapi.invest.platform.model.invest.InvestnewStockIndexModel;
import la.niub.abcapi.invest.platform.service.IRecommendStockService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 推票处理
 * @author xwu.abcft
 */
@Component
public class RecommendStockJob {

    static Logger logger = LogManager.getLogger(RecommendStockJob.class);

    @Autowired
    private IRecommendStockService recommendStockService;

    @Autowired
    private IInvestnewRecommendedStockAttachmentDao investnewRecommendedStockAttachmentDao;

    @Autowired
    private IInvestnewStockIndexDao investnewStockIndexDao;

    private static List<Integer> handling = new ArrayList<>();

    /**
     * 处理推票邮件附件
     */
//    @Scheduled(cron="0 */10 * * * ?")
    public void handleEmailAttachment(){
        logger.info("RecommendStockJob.handleEmailAttachment begin");
        try {
            if (!handling.isEmpty()){
                logger.info("handling!");
                return;
            }
            List<InvestnewRecommendedStockAttachmentModel> list = investnewRecommendedStockAttachmentDao.selectNotHandled();
            list.forEach((item)->{handling.add(item.getId());});

            for (InvestnewRecommendedStockAttachmentModel item : list){
                try{
                    if (StringUtils.isEmpty(item.getUser_id()) || StringUtils.isEmpty(item.getBroker()) || StringUtils.isEmpty(item.getFile_url())) {
                        continue;
                    }
                    Integer success = recommendStockService.handleEmailStock(item.getUser_id(),item.getBroker(),item.getPush_date(),item.getFile_url());
                    item.setStatus("1");
                    investnewRecommendedStockAttachmentDao.updateByPrimaryKeySelective(item);
                }catch (Exception e){
                    logger.error("处理出错:"+e.getMessage(),e);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(item.getCreate_time());
                    calendar.add(Calendar.DAY_OF_MONTH,7);
                    if (new Date().after(calendar.getTime())){
                        item.setStatus("2");
                        investnewRecommendedStockAttachmentDao.updateByPrimaryKeySelective(item);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("RecommendStockJob.handleEmailAttachment fail:"+e.getMessage(),e);
        }finally {
            handling.clear();
        }
        logger.info("RecommendStockJob.handleEmailAttachment end");
    }

    /**
     * 从数据库找空白数据的条目，导入数据
     */
//    @Scheduled(fixedDelay=1000*60)
//    @Scheduled(cron="0 */6 * * * ?")
    public void startCalc(){
        logger.info("StockJob startCalc begin");
        Lock lock = new ReentrantLock(){};
        try {
            logger.info("getting lock");
            if(lock.tryLock(10, TimeUnit.SECONDS)) {
                logger.info("get lock success");
                try{
                    Integer success = 0;
                    List<InvestnewStockIndexModel> dataEmptyList =  investnewStockIndexDao.selectDataEmpty(10);
                    if (dataEmptyList.isEmpty()){
                        return;
                    }
                    success += recommendStockService.calcRecommendedStockReturnRate(dataEmptyList);
                    logger.info("calc success "+success);
                }catch(Exception ex){
                    ex.printStackTrace();
                    return;
                }finally{
                    lock.unlock();   //释放锁
                }
            }else {
                //如果不能获取锁，则直接做其他事情
                logger.info("get lock fail");
                return;
            }
        } catch (InterruptedException e) {
            logger.info("error");
            e.printStackTrace();
            return;
        }
    }
}
