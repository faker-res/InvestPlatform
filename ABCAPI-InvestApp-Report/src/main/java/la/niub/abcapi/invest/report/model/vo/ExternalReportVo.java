/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-14
 */
@Data
public class ExternalReportVo {
	@NotBlank(message="userId必填")
	private String userId;
	
	@NotBlank(message="fileUrl必填")
	private String fileUrl;
	
	@NotBlank(message="sourceType必填")
	private String sourceType;
	/**
	 *@author zhairp createDate: 2019-03-14
	 * @return
	 */
	@Override
	public String toString() {
		return "ExternalReportVo [userId=" + userId + ", fileUrl=" + fileUrl + ", sourceType=" + sourceType + "]";
	}
	
}
