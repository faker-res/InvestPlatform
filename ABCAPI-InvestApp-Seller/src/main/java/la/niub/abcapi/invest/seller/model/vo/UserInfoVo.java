package la.niub.abcapi.invest.seller.model.vo;

/**
 * @author : ppan
 * @description :
 * @date : 2019-02-11 15:12
 */
public class UserInfoVo {
    private String userId;

    private String username;

    private String parentId;

    private String parentName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
