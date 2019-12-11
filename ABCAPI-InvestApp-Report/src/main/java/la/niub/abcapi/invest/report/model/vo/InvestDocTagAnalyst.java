/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-13
 */
@Data
public class InvestDocTagAnalyst {
	private String analyst_name;
	private String tel;
	private String email;
	private String cert_id;

	/**
	 * @author zhairp createDate: 2019-03-13
	 * @return
	 */
	@Override
	public String toString() {
		return "InvestDocTagAnalyst [analyst_name=" + analyst_name + ", tel=" + tel + ", email=" + email + ", cert_id=" + cert_id + "]";
	}

}
