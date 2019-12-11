/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-05
 */
@Data
public class InvestDocInput {
	@Id
	private Long id;
	private Boolean downloaded = true;
	private Date update_time;
	private Date time;
	private String file_type;
	private String file_url;
	private String callback_url;
	private InvestDocFileData file_data;
	private String storage = "url";
}
