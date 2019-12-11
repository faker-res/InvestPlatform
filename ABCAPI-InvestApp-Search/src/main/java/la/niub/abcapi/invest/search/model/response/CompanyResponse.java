package la.niub.abcapi.invest.search.model.response;

import lombok.Data;

@Data
public class CompanyResponse {
	private Long company_id;
	private String sname;
	private String fullname;
	private CompanyTypeEnum type;

	@Override
	public String toString() {
		return "CompanyResponse [company_id=" + company_id + ", sname=" + sname + ", fullname=" + fullname + ", type=" + type + "]";
	}

}
