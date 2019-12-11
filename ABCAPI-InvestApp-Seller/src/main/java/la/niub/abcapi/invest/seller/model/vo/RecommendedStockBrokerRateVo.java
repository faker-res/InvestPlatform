package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

public class RecommendedStockBrokerRateVo {
	
	@NotBlank(message="参数[userId]必传")
	private String userId;
	
	private String parentId;
	
	@NotBlank(message="参数[pushMonth]必传")
	private String pushMonth;
	
	private String lastPushMonth;

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

	public String getPushMonth() {
		return pushMonth;
	}

	public void setPushMonth(String pushMonth) {
		this.pushMonth = pushMonth;
	}

	public String getLastPushMonth() {
		return lastPushMonth;
	}

	public void setLastPushMonth(String lastPushMonth) {
		this.lastPushMonth = lastPushMonth;
	}

	@Override
	public String toString() {
		return "StockQueryVo [userId=" + userId + ", parentId=" + parentId + ", pushMonth=" + pushMonth + ", lastPushMonth=" + lastPushMonth + "]";
	}

}
