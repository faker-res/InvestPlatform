/**
 * Copyright (C) 2006-2012 Tuniu All rights reserved
 * Author: 
 * Date: Sat Jan 26 11:04:53 CST 2019
 * Description:
 */
package la.niub.abcapi.invest.seller.dao.invest;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import la.niub.abcapi.invest.seller.model.TraderMarkObjectiveDataDetailModel;

public interface TraderMarkObjectiveDataDetailMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(TraderMarkObjectiveDataDetailModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return TraderMarkObjectiveDataDetailModel 
     */
    TraderMarkObjectiveDataDetailModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(TraderMarkObjectiveDataDetailModel record);

    /**
     * 批量插入
     * @param objectiveDataDetailModelList
     * @param taskId
     * @return
     */
    int insertBatch(@Param("list") List<TraderMarkObjectiveDataDetailModel> objectiveDataDetailModelList, @Param("taskId") Long taskId);
    
	List<Map<String, Object>> getObjectiveData(@Param("quota") String quota, @Param("taskId") Long taskId);
	
	List<String> getSubjectIndexs(@Param("taskId") Long taskId);
	
	List<Map<String, Object>> getSubjectData(@Param("quota") String quota, @Param("taskId") Long taskId);
	
	List<Map<String, Object>> brokerSearchByScore(@Param("broker") String broker, @Param("taskId") Long taskId);
	
	List<Map<String,Object>> getTaskList(@Param("parentId") String parentId);
	
	List<Map<String, Object>> getIndustries(@Param("broker") String broker, @Param("taskId") Long taskId);

	BigInteger getLastestTask(@Param("parentId") String parentId);
}