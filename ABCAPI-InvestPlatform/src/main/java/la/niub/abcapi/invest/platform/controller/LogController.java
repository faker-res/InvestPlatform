package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.model.request.log.HistoryAddRequest;
import la.niub.abcapi.invest.platform.model.request.log.HistoryListRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.ILogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志
 */
@RestController
@RequestMapping(path = "/log")
public class LogController {

    private final static Logger logger = LogManager.getLogger(LogController.class);

    @Autowired
    private ILogService logService;

    /**
     * 添加阅读历史
     * @return
     */
    @PostMapping(path = "/history/add")
    Response historyAdd(@RequestBody HistoryAddRequest param) {
        return new Response(logService.addHistory(param));
    }

    /**
     * 阅读历史列表
     * @return
     */
    @GetMapping(path = "/history/list")
    Response historyList(HistoryListRequest param) {
        return new Response(logService.ListHistory(param));
    }
}
