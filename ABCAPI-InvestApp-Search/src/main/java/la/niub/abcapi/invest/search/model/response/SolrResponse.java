/**
 * 
 */
package la.niub.abcapi.invest.search.model.response;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-02-19
 */
@Data
public class SolrResponse {
	/**
	 * 总记录数
	 */
	private Long numFound;
	/**
	 * 分页数据
	 */
	private Object docs;

	@Override
	public String toString() {
		return "SolrResponse [numFound=" + numFound + ", docs=" + docs + "]";
	}

	public SolrResponse() {
		super();
	}

	public SolrResponse(Long numFound, Object docs) {
		super();
		this.numFound = numFound;
		this.docs = docs;
	}

}
