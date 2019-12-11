package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystBO;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystReportCountBO;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.IAnalystService;
import la.niub.abcapi.invest.platform.service.IDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalystServiceImpl implements IAnalystService {

    private final static Logger logger = LogManager.getLogger(AnalystServiceImpl.class);

    @Autowired
    private IDataService dataService;

    @Autowired
    private IInvestCloudClient investCloudClient;

    @Override
    public List<AnalystReportCountBO> getInnerReportCount(List<String> names, String companyName, String startTime, String endTime) {
        List<AnalystReportCountBO> result = new ArrayList<>();

        Map<String,Long> countsMap = new HashMap<>();
        Response<Map<String,Long>> response = investCloudClient.innerReportCount(String.join(",",names),startTime,endTime);
        if (response != null && response.getData() != null){
            countsMap = response.getData();
        }

        try {
            for (String item : names){
                AnalystReportCountBO analystReportCountBO = new AnalystReportCountBO();
                analystReportCountBO.setName(item);
                analystReportCountBO.setCount(countsMap.getOrDefault(item,0L));
                AnalystBO analystBO = dataService.findAnalyst(item,companyName);
                analystReportCountBO.setAnalyst(analystBO);
                result.add(analystReportCountBO);
            }
        } catch (Exception e) {
            logger.error("获取研究员统计信息出错: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }
}
