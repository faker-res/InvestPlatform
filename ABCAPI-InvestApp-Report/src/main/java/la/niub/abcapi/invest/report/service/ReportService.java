/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import la.niub.abcapi.invest.report.model.response.SolrQueryResponse;
import la.niub.abcapi.invest.report.model.vo.InvestDocInput;
import la.niub.abcapi.invest.report.model.vo.InvestDocOutput;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocInput;
import la.niub.abcapi.invest.report.model.vo.InvestSolrDocOutput;
import la.niub.abcapi.invest.report.model.vo.ReportFilter;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;

/**
 * @author zhairp createDate: 2019-03-04
 */
public interface ReportService {

	/**
	 * 批量新增待解析的研报到算法mongodb库
	 * 
	 * @author zhairp createDate: 2019-03-05
	 * @param docs
	 */
	void addReportsToMongodb(List<InvestDocInput> docs);

	/**
	 * 研报上传
	 * 
	 * @author zhairp createDate: 2019-02-26
	 * @param fileName 文件名字
	 * @param input    文件输入流
	 * @return 文件在OSS的上传地址
	 */
	String uploadReport(String fileName, InputStream input);

	/**
	 * 从集合中查询已经解析的指定数量的研报
	 * 
	 * @author zhairp createDate: 2019-03-04
	 * @param status
	 * @param limit
	 * @return
	 */
	List<ResearchTaskDomain> getResearchTasks(Integer status, Integer limit);

	/**
	 * 批量更新研报的状态
	 * 
	 * @author zhairp createDate: 2019-03-04
	 * @param ids
	 * @param status
	 */
	void batchUpdateResearchTaskStatus(List<Long> ids, Integer status);

	/**
	 * 清空集合中所有的研报
	 * 
	 * @author zhairp createDate: 2019-03-04
	 */
	void emptyMongo();

	/**
	 * 批量同步数据到solr
	 * 
	 * @author zhairp createDate: 2019-03-05
	 * @param tasks
	 */
	void addReportsToSolr(List<InvestSolrDocInput> tasks);

	/**
	 * 清空集合中所有的研报
	 * 
	 * @author zhairp createDate: 2019-03-05
	 */
	void emptySolr();

	/**
	 * 根据ID查询文档
	 * 
	 * @author zhairp createDate: 2019-03-07
	 * @param id
	 * @return
	 */
	InvestDocOutput getInvestDocInputById(Long id, String collectionName);

	/**
	 * 查询研报筛选项
	 * 
	 * @author zhairp createDate: 2019-03-14
	 * @param reportFilter
	 * @return
	 */
	Map<String, Object> getReportFilterItems(ReportFilter reportFilter);

	/**
	 * 通用的功能 搜索研报
	 * 
	 * @author zhairp createDate: 2019-03-15
	 * @param reportFilter
	 * @return
	 */
	<T> SolrQueryResponse<T> basicQueryReport(ReportFilter reportFilter);

	/**
	 * 
	 * @author zhairp createDate: 2019-03-19
	 * @param id
	 * @return
	 */
	InvestSolrDocOutput getReportDetail(String id);

}
