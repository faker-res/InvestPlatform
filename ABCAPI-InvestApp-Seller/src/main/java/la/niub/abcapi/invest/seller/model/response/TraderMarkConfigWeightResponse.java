package la.niub.abcapi.invest.seller.model.response;

import java.math.BigDecimal;

/**
 * @author : ppan
 * @description : 券商评价权重列表
 * @date : 2019-01-22 14:32
 */
public class TraderMarkConfigWeightResponse {

    private Long id;

    private String userId;

    private String userAccount;

    private String userName;

    private BigDecimal weight;

    private Integer threshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}
