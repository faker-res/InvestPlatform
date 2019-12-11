package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author : ppan
 * @description : 保存券商评价权重
 * @date : 2019-01-22 15:12
 */
public class TraderMarkConfigWeightAddVo {
    // 当前登录用户id
    @NotBlank(message = "参数[userId]必传")
    private String userId;

    // 权重用户的id
    @NotBlank(message = "参数[objectUserId]必传")
    private String objectUserId;

    @NotBlank(message = "参数[userAccount]必传")
    private String userAccount;

    @NotBlank(message = "参数[userName]必传")
    private String userName;

    @NotNull(message = "参数[weight]必传")
    private Integer weight;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjectUserId() {
        return objectUserId;
    }

    public void setObjectUserId(String objectUserId) {
        this.objectUserId = objectUserId;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
