package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.util.Date;

public class RoadShowToBuyerModel implements Serializable {

    private static final long serialVersionUID = 5042253551890962196L;

    private Long id;

    private Long roadShowId;

    private String buyerId;

    private String buyerName;

    private String buyerCompanyId;

    private String buyerCompanyName;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoadShowId() {
        return roadShowId;
    }

    public void setRoadShowId(Long roadShowId) {
        this.roadShowId = roadShowId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId == null ? null : buyerId.trim();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    public String getBuyerCompanyId() {
        return buyerCompanyId;
    }

    public void setBuyerCompanyId(String buyerCompanyId) {
        this.buyerCompanyId = buyerCompanyId == null ? null : buyerCompanyId.trim();
    }

    public String getBuyerCompanyName() {
        return buyerCompanyName;
    }

    public void setBuyerCompanyName(String buyerCompanyName) {
        this.buyerCompanyName = buyerCompanyName == null ? null : buyerCompanyName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}