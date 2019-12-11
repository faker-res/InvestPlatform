/**
 * Copyright (C) 2006-2012 Tuniu All rights reserved
 * Author: 
 * Date: Fri Mar 01 16:53:06 CST 2019
 * Description:
 */
package la.niub.abcapi.invest.report.dao.invest;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;

public interface ResearchTaskDomainMapper {

	int insertSelective(ResearchTaskDomain record);

	ResearchTaskDomain selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ResearchTaskDomain record);
	
	void updateReportParentId(ResearchTaskDomain record);

	/**
	 * 定时获取指定状态指定数量的研报任务
	 * 
	 * @author zhairp createDate: 2019-03-04
	 * @param status
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> getResearchTasks(@Param("status") Integer status, @Param("limit") Integer limit);

	/**
	 * 批量更新研报任务的状态
	 * 
	 * @author zhairp createDate: 2019-03-04
	 * @param ids
	 * @param status
	 */
	void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

	/**
	 * 查询已解析的研报任务
	 * 
	 * @author zhairp createDate: 2019-03-06
	 * @param limit
	 * @return
	 */
	List<ResearchTaskDomain> getResolvedTask(@Param("limit") Integer limit, @Param("status") Integer status);

}