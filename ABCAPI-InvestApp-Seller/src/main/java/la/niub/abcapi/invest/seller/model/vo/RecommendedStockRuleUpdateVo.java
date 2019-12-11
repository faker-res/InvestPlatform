package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author : ppan
 * @description : 更新金股提取规则
 * @date : 2019-01-17 13:32
 */
public class RecommendedStockRuleUpdateVo {

    @NotBlank(message = "参数[userId]必传")
    private String userId;

    @NotNull(message = "参数[id]必传")
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
