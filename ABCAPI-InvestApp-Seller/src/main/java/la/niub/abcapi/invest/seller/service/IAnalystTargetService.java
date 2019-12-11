package la.niub.abcapi.invest.seller.service;

public interface IAnalystTargetService {
    void synchronizeHistoricalData(String parentId, String startTime, String endTime) throws Exception;

    void synchronizeHistoricalDataByPage(String parentId, String startTime, String endTime, Integer page) throws Exception;

    void removeCache() throws Exception;
}
