package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TraderMarkAnalystTargetModel implements Serializable {

    private static final long serialVersionUID = -1876508575767314390L;

    private Long id;

    private String parentId;

    private String broker;

    private String analyst;

    private Long secUniCode;

    private String stockCode;

    private String stockName;

    private BigDecimal targetValue;

    private Date targetDate;

    private BigDecimal targetStockValue;

    private Date targetCompDate;

    private BigDecimal targetCompStockValue;

    private Integer targetCompDays;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getAnalyst() {
        return analyst;
    }

    public void setAnalyst(String analyst) {
        this.analyst = analyst == null ? null : analyst.trim();
    }

    public Long getSecUniCode() {
        return secUniCode;
    }

    public void setSecUniCode(Long secUniCode) {
        this.secUniCode = secUniCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode == null ? null : stockCode.trim();
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName == null ? null : stockName.trim();
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public BigDecimal getTargetStockValue() {
        return targetStockValue;
    }

    public void setTargetStockValue(BigDecimal targetStockValue) {
        this.targetStockValue = targetStockValue;
    }

    public Date getTargetCompDate() {
        return targetCompDate;
    }

    public void setTargetCompDate(Date targetCompDate) {
        this.targetCompDate = targetCompDate;
    }

    public BigDecimal getTargetCompStockValue() {
        return targetCompStockValue;
    }

    public void setTargetCompStockValue(BigDecimal targetCompStockValue) {
        this.targetCompStockValue = targetCompStockValue;
    }

    public Integer getTargetCompDays() {
        return targetCompDays;
    }

    public void setTargetCompDays(Integer targetCompDays) {
        this.targetCompDays = targetCompDays;
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