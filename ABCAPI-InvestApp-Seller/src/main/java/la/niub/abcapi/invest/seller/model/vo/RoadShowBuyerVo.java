package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

public class RoadShowBuyerVo {
	@NotBlank(message = "买方研究员id不能为空")
	private String buyerId;

	@NotBlank(message = "买方研究员name不能为空")
	private String buyerName;

	@NotBlank(message = "买方研究员所在公司id不能为空")
	private String buyerCompanyId;

	@NotBlank(message = "买方研究员所在公司name不能为空")
	private String buyerCompanyName;

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerCompanyId() {
		return buyerCompanyId;
	}

	public void setBuyerCompanyId(String buyerCompanyId) {
		this.buyerCompanyId = buyerCompanyId;
	}

	public String getBuyerCompanyName() {
		return buyerCompanyName;
	}

	public void setBuyerCompanyName(String buyerCompanyName) {
		this.buyerCompanyName = buyerCompanyName;
	}
}
