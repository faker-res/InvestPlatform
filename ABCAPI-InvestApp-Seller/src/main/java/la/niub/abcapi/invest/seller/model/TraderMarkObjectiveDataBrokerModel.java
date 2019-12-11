package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TraderMarkObjectiveDataBrokerModel implements Serializable {

    private static final long serialVersionUID = -4692669070275376441L;

    private Long id;

    private Long taskId;

    private String broker;

    private Integer reportCount;

    private Integer readCount;

    private BigDecimal attainDays;

    private BigDecimal attainProbability;

    private Integer roadShowCount;

    private BigDecimal goldRate;

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

    public BigDecimal getGoldRate() {
        return goldRate;
    }

    public void setGoldRate(BigDecimal goldRate) {
        this.goldRate = goldRate;
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