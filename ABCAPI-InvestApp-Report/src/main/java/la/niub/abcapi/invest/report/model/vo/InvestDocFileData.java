/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-05
 */
@Data
public class InvestDocFileData {
	private String account_id;

	@Override
	public String toString() {
		return "InvestDocFileData [account_id=" + account_id + "]";
	}

}
