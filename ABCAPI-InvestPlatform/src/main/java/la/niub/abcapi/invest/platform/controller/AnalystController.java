package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.util.TimeUtil;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystReportCountBO;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.IAnalystService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 分析师数据
 */
@RestController
@RequestMapping(path = "/analyst")
public class AnalystController {

    private final static Logger logger = LogManager.getLogger(AnalystController.class);

    @Autowired
    private IAnalystService analystService;

    /**
     * 分析师研报数量
     * @return
     */
    @GetMapping(path = "/innerreport/count")
    Response innerReportCount(@RequestParam(value = "name") String name,
                              @RequestParam(value = "company_name",required = false) String companyName,
                              @RequestParam(value = "start_time",required = false) String startTime,
                              @RequestParam(value = "end_time",required = false) String endTime) {
        List<String> nameList = Arrays.asList(name.split(","));
        List<AnalystReportCountBO> result = analystService.getInnerReportCount(nameList,companyName,startTime,endTime);

        return new Response(result);
    }
}
