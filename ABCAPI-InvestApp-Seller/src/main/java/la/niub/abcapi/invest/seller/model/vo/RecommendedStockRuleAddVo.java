package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author : ppan
 * @description : 保存金股提取规则请求参数
 * @date : 2019-01-17 10:19
 */
public class RecommendedStockRuleAddVo {

    @NotBlank(message = "参数[userId]必传")
    private String userId;

    @NotBlank(message = "参数[broker]必传")
    private String broker;

    //@NotBlank(message = "参数[sheetName]必传")
    private String sheetName = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
