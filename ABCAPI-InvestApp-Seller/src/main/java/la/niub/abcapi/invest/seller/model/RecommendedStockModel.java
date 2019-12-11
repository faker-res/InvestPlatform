package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.util.Date;

public class RecommendedStockModel implements Serializable {

	private static final long serialVersionUID = 5454967557116306785L;

	private Long id;

	private String userId;

	private String parentId;

	private Long secUniCode;

	private String stockCode;

	private String stockName;

	private String industry;

	private String broker;

	private String pushMonth;

	private Date pushTime;

	private String recommendedReason;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getPushMonth() {
		return pushMonth;
	}

	public void setPushMonth(String pushMonth) {
		this.pushMonth = pushMonth;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getRecommendedReason() {
		return recommendedReason;
	}

	public void setRecommendedReason(String recommendedReason) {
		this.recommendedReason = recommendedReason;
	}

}