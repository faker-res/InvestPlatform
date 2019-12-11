/**
 * 
 */
package la.niub.abcapi.invest.search.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhairp createDate: 2019-02-16
 */
public interface TestService {

	List<Map<String, Object>> getAllAnalysts();

	void flushAnalystsCache();

}
