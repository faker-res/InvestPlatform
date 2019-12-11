/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-08
 */
@Data
public class ItemQueryInput {
	private String userId;
	private String sourceType;
	private String reportFileType;
	@NotBlank(message = "filter不能为空")
	private String filter;

	/**
	 * @author zhairp createDate: 2019-04-19
	 * @return
	 */
	@Override
	public String toString() {
		return "ItemQueryInput [userId=" + userId + ", sourceType=" + sourceType + ", reportFileType=" + reportFileType + ", filter=" + filter + "]";
	}

}
