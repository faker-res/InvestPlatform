package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.invest.InvestnewStockIndexModel;

import java.util.Date;
import java.util.List;

/**
 * 推票
 */
public interface IRecommendStockService {

    /**
     * 处理邮件推票
     * @param userId
     * @param broker
     * @param pushDate
     * @param attachmentUrl
     * @return
     */
    Integer handleEmailStock(String userId,String broker,Date pushDate,String attachmentUrl) throws Exception;

    /**
     * 计算金股推荐收益率并保存
     *
     * @param stockIndexs
     * @return
     */
    Integer calcRecommendedStockReturnRate(List<InvestnewStockIndexModel> stockIndexs);
}
