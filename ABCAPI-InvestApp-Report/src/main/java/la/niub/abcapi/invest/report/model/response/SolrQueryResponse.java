/**
 * 
 */
package la.niub.abcapi.invest.report.model.response;

import java.util.List;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-02-22
 */
@Data
public class SolrQueryResponse<T> {
	/* 总记录数 */
	private Long numFound;
	/* 分页数据 */
	private List<T> data;

	public SolrQueryResponse(Long numFound, List<T> data) {
		super();
		this.numFound = numFound;
		this.data = data;
	}

	public SolrQueryResponse() {
		super();
	}

	@Override
	public String toString() {
		return "SolrQueryResponse [numFound=" + numFound + ", data=" + data + "]";
	}

}
