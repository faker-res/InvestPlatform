package la.niub.abcapi.invest.seller.service;

import la.niub.abcapi.invest.seller.model.ReadLogModel;

public interface ISellerReportTransactionService {
    int saveReadLog(ReadLogModel readLogModel) throws Exception;
}
