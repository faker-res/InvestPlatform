package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.util.Date;

public class RoadShowToCompanyModel implements Serializable {

    private static final long serialVersionUID = -5848170331629446982L;

    private Long id;

    private Long roadShowId;

    private String companyId;

    private String companyName;

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
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