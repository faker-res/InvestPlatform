package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TraderMarkObjectiveDataDetailModel implements Serializable {

    private static final long serialVersionUID = 558415225760124603L;

    private Long id;

    private Long taskId;

    private String broker;

    private String industry;

    private String analyst;

    private Integer reportCount;

    private Integer readCount;

    private BigDecimal attainDays;

    private BigDecimal attainProbability;

    private Integer roadShowCount;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getAnalyst() {
        return analyst;
    }

    public void setAnalyst(String analyst) {
        this.analyst = analyst == null ? null : analyst.trim();
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public BigDecimal getAttainDays() {
        return attainDays;
    }

    public void setAttainDays(BigDecimal attainDays) {
        this.attainDays = attainDays;
    }

    public BigDecimal getAttainProbability() {
        return attainProbability;
    }

    public void setAttainProbability(BigDecimal attainProbability) {
        this.attainProbability = attainProbability;
    }

    public Integer getRoadShowCount() {
        return roadShowCount;
    }

    public void setRoadShowCount(Integer roadShowCount) {
        this.roadShowCount = roadShowCount;
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