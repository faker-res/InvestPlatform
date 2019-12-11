package la.niub.abcapi.invest.seller.model.vo;

public class RoadShowCompanyVo {
	private String companyId;

//	@NotBlank(message = "路演公司名称必填")
	private String companyName;

	@Override
	public String toString() {
		return "CompanyVo [companyId=" + companyId + ", companyName=" + companyName + "]";
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
