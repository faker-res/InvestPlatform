package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.bo.log.ReadLogBO;
import la.niub.abcapi.invest.platform.model.request.log.HistoryAddRequest;
import la.niub.abcapi.invest.platform.model.request.log.HistoryListRequest;

import java.util.List;

/**
 * 日志
 */
public interface ILogService {

    /**
     * 添加日志
     * @return
     */
    ReadLogBO addHistory(HistoryAddRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<ReadLogBO> ListHistory(HistoryListRequest request);
}
