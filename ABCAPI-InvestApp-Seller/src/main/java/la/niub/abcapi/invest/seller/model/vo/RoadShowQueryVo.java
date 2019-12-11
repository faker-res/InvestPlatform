package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @author zhairp createDate: 2018-11-26
 */
public class RoadShowQueryVo {
	@NotBlank(message = "用户id必传")
	private String userId;

	@NotBlank(message = "查询开始时间必传")
	private String startTime;

	@NotBlank(message = "查询结束时间必传")
	private String endTime;

	private List<String> companyNames;
	private List<String> industryNames;
	private List<String> sellerNames;
	private List<String> buyerIds;

	private Boolean joinTrip = false;
	private List<String> sellerCompanyIds;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getCompanyNames() {
		return companyNames;
	}

	public void setCompanyNames(List<String> companyNames) {
		this.companyNames = companyNames;
	}

	public List<String> getIndustryNames() {
		return industryNames;
	}

	public void setIndustryNames(List<String> industryNames) {
		this.industryNames = industryNames;
	}

	public List<String> getSellerNames() {
		return sellerNames;
	}

	public void setSellerNames(List<String> sellerNames) {
		this.sellerNames = sellerNames;
	}

	public List<String> getBuyerIds() {
		return buyerIds;
	}

	public void setBuyerIds(List<String> buyerIds) {
		this.buyerIds = buyerIds;
	}

	public Boolean getJoinTrip() {
		return joinTrip;
	}

	public void setJoinTrip(Boolean joinTrip) {
		this.joinTrip = joinTrip;
	}

	public List<String> getSellerCompanyIds() {
		return sellerCompanyIds;
	}

	public void setSellerCompanyIds(List<String> sellerCompanyIds) {
		this.sellerCompanyIds = sellerCompanyIds;
	}
}
