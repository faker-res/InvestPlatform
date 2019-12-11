package la.niub.abcapi.invest.seller.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class StockMonthReturnRateBO {

    private Long secUniCode;

    private Date month;

    private BigDecimal goldRate;

    private BigDecimal industryRate;

    private BigDecimal monthBeginPrice;

    private BigDecimal monthEndPrice;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectBegin;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectEnd;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date actualBegin;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date actualEnd;

    public Long getSecUniCode() {
        return secUniCode;
    }

    public void setSecUniCode(Long secUniCode) {
        this.secUniCode = secUniCode;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public BigDecimal getGoldRate() {
        return goldRate;
    }

    public void setGoldRate(BigDecimal goldRate) {
        this.goldRate = goldRate;
    }

    public BigDecimal getIndustryRate() {
        return industryRate;
    }

    public void setIndustryRate(BigDecimal industryRate) {
        this.industryRate = industryRate;
    }

    public BigDecimal getMonthBeginPrice() {
        return monthBeginPrice;
    }

    public void setMonthBeginPrice(BigDecimal monthBeginPrice) {
        this.monthBeginPrice = monthBeginPrice;
    }

    public BigDecimal getMonthEndPrice() {
        return monthEndPrice;
    }

    public void setMonthEndPrice(BigDecimal monthEndPrice) {
        this.monthEndPrice = monthEndPrice;
    }

    public Date getExpectBegin() {
        return expectBegin;
    }

    public void setExpectBegin(Date expectBegin) {
        this.expectBegin = expectBegin;
    }

    public Date getExpectEnd() {
        return expectEnd;
    }

    public void setExpectEnd(Date expectEnd) {
        this.expectEnd = expectEnd;
    }

    public Date getActualBegin() {
        return actualBegin;
    }

    public void setActualBegin(Date actualBegin) {
        this.actualBegin = actualBegin;
    }

    public Date getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(Date actualEnd) {
        this.actualEnd = actualEnd;
    }
}
