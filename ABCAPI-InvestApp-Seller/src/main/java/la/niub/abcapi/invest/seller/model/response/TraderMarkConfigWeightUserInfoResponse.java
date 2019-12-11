package la.niub.abcapi.invest.seller.model.response;

/**
 * @author : ppan
 * @description : 券商评价权重设置用户信息
 * @date : 2019-01-22 14:53
 */
public class TraderMarkConfigWeightUserInfoResponse {

    private String userId;

    private String name;

    private String email;

    private String telephone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
