package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystReportCountBO;

import java.util.List;

/**
 * 分析师
 */
public interface IAnalystService {
    /**
     * 获取分析师上传的内部研报数量
     * @return
     */
    List<AnalystReportCountBO> getInnerReportCount(List<String> names, String companyName, String startTime, String endTime);
}
