/**
 * 
 */
package la.niub.abcapi.invest.report.dao.invest;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author zhairp createDate: 2019-03-05
 */
public interface InvestnewResolvedTaskMapper {

	/**
	 * 解析结果入库
	 * 
	 * @author zhairp createDate: 2019-03-05
	 * @param sourceId
	 * @param processError
	 */
	void saveResolvedTask(@Param("sourceId") Long sourceId, @Param("processError") String processError);

	/**
	 * 批量更新研报任务的状态
	 * 
	 * @author zhairp createDate: 2019-03-06
	 * @param sourceIds
	 * @param status
	 */
	void batchUpdateResolvedTaskStatus(@Param("sourceIds") List<Long> sourceIds, @Param("status") Integer status);

}
