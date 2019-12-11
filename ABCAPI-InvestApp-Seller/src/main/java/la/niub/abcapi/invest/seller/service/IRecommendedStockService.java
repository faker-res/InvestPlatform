package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.InvestnewStockIndexModel;
import la.niub.abcapi.invest.seller.model.bo.mail.Mail;

import java.util.Date;
import java.util.List;

/**
 * 处理金股
 */
public interface IRecommendedStockService {

    /**
     * 识别邮件为金股
     * @param mail
     * @return
     */
    Integer handleMail(Mail mail);

    /**
     * 处理邮件推票
     * @param userId
     * @param broker
     * @param pushDate
     * @param attachmentUrl
     * @return
     */
    Integer handleEmailStock(String userId, String broker, Date pushDate, String attachmentUrl) throws Exception;

    /**
     * 计算金股推荐收益率并保存
     *
     * @param stockIndexs
     * @return
     */
    Integer calcRecommendedStockReturnRate(List<InvestnewStockIndexModel> stockIndexs);
}
