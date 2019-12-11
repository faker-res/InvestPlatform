/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.List;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-08
 */
@Data
public class HotQueryOutput {
	private String name;
	private Integer report_total;
	private Integer org_total;
	private List<DetailVo> deail;
}
