/**
 * 
 */
package la.niub.abcapi.invest.report.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import la.niub.abcapi.invest.report.model.response.Response;
import la.niub.abcapi.invest.report.model.vo.HotQueryInput;
import la.niub.abcapi.invest.report.model.vo.ItemQueryInput;
import la.niub.abcapi.invest.report.service.ReportBusinessService;

/**
 * 卖方研报
 * 
 * @author zhairp createDate: 2019-04-08
 */
@RestController
@RequestMapping("business")
public class ReportBusinessController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReportBusinessService reportBusinessService;

	/**
	 * 查询行业和研报类型
	 * 
	 * @author zhairp createDate: 2019-04-10
	 * @param input
	 * @return
	 */
	@GetMapping("getItems")
	public Response getItems(@Valid ItemQueryInput input) {
		log.info("getItems input:{}", input.toString());
//		return new Response(reportBusinessService.getItems(input));
		return new Response(reportBusinessService.getItems2(input));
	}

	/**
	 * 我的热门公司或者行业
	 * 
	 * @author zhairp createDate: 2019-04-12
	 * @param input
	 * @return
	 */
	@GetMapping("getHotItem")
	public Response getHotItem(HotQueryInput input) {
		log.info("getHotItem input:{}", input.toString());
		return new Response(reportBusinessService.getHotItem(input));
	}

}
