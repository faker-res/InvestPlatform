/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-05
 */
@Data
public class InvestDocOutput {
	@Id
	private Long id;
	private InvestDocTagData alg_document_tags;

	/**
	 * @author zhairp createDate: 2019-03-13
	 * @return
	 */
	@Override
	public String toString() {
		return "InvestDocOutput [id=" + id + ", alg_document_tags=" + alg_document_tags + "]";
	}

}
