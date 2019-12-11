/**
 * Copyright (C) 2006-2012 Tuniu All rights reserved
 * Author: 
 * Date: Fri Mar 01 16:53:06 CST 2019
 * Description:
 */
package la.niub.abcapi.invest.report.model.vo;

import java.util.Date;

import lombok.Data;

/**
 * [@Id ongodb主键映射] [@Field solr字段映射] [@SolrMapping solr查询详情映射字段] 研报实体类
 * ResearchTaskDomain investnew_research_task
 */
@Data
public class ResearchTaskDomain {
	/**
	 * 主键id investnew_research_task.id
	 */
	private Long id;

	/**
	 * 标题 investnew_research_task.title
	 */
	private String title;

	/**
	 * 研报摘要 investnew_research_task.summary
	 */
	private String summary;

	/**
	 * 状态: 0-待编辑 1-待审批 2-待解析 3-已解析 4-已推送 investnew_research_task.status
	 */
	private Integer status;

	/**
	 * 研报范围 investnew_research_task.rp_range
	 */
	private String rpRange;

	/**
	 * 研报大类 investnew_research_task.category_code
	 */
	private String categoryCode;

	/**
	 * 研报子类 investnew_research_task.category_child_code
	 */
	private String categoryChildCode;

	/**
	 * 文件地址 investnew_research_task.file_url
	 */
	private String fileUrl;

	/**
	 * 文件类型 investnew_research_task.file_type
	 */
	private String fileType;

	/**
	 * 文件页码数 investnew_research_task.file_pages
	 */
	private Integer filePages;

	/**
	 * 作者名字 investnew_research_task.writer
	 */
	private String writer;

	/**
	 * 撰写日期 investnew_research_task.write_day
	 */
	private Date writeDay;

	/**
	 * 作者ID investnew_research_task.user_id
	 */
	private String userId;

	/**
	 * 公司ID investnew_research_task.parent_id
	 */
	private String parentId;

	/**
	 * 评级 investnew_research_task.rank
	 */
	private String rank;

	/**
	 * 关联行业 investnew_research_task.relate_industry
	 */
	private String relateIndustry;

	/**
	 * 关联股票编码 investnew_research_task.relate_stock_code
	 */
	private String relateStockCode;

	/**
	 * 关联股票名称 investnew_research_task.relate_stock_name
	 */
	private String relateStockName;

	/**
	 * 创建时间 investnew_research_task.create_time
	 */
	private Date createTime;

	/**
	 * 更新时间 investnew_research_task.update_time
	 */
	private Date updateTime;

	private String relateStocks;

	private String sourceType;

	/**
	 *@author zhairp createDate: 2019-03-22
	 * @return
	 */
	@Override
	public String toString() {
		return "ResearchTaskDomain [fileUrl=" + fileUrl + ", userId=" + userId + ", sourceType=" + sourceType + "]";
	}

}