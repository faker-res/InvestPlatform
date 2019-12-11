/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import com.memfactory.pub.http.request.BaseQuery;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-04-23
 */
@Data
public class ReportQuery extends BaseQuery {
	private String userId;
	private String keyword;
}
