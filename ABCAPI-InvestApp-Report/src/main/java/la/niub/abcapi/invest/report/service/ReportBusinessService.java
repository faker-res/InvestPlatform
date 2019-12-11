/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import java.util.List;
import java.util.Map;

import la.niub.abcapi.invest.report.model.vo.HotQueryInput;
import la.niub.abcapi.invest.report.model.vo.HotQueryOutput;
import la.niub.abcapi.invest.report.model.vo.ItemQueryInput;

/**
 * @author zhairp createDate: 2019-04-08
 */
public interface ReportBusinessService {

	Map<String, Object> getItems(ItemQueryInput input);

	Map<String, Object> getItems2(ItemQueryInput input);

	List<HotQueryOutput> getHotItem(HotQueryInput input);

}
