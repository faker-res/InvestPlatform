/**
 * 
 */
package la.niub.abcapi.invest.report.model.so;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-09
 */
@Data
public class CompanySo {
	@JSONField(name = "company_id")
	private String parentId;
	private String sname;
	private String fullname;
	private String type;

	/**
	 * @author zhairp createDate: 2019-04-09
	 * @return
	 */
	@Override
	public String toString() {
		return "CompanySo [parentId=" + parentId + ", sname=" + sname + ", fullname=" + fullname + ", type=" + type + "]";
	}

}
