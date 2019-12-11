package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.config.enums.LineTypeEnum;
import la.niub.abcapi.invest.platform.model.bo.line.KlineBO;
import la.niub.abcapi.invest.platform.model.bo.line.PublicCompanyTimeBO;
import la.niub.abcapi.invest.platform.model.bo.line.RealTimeBO;
import la.niub.abcapi.invest.platform.model.bo.line.TimelineBO;

import java.util.Date;
import java.util.List;

/**
 * 获取曲线
 */
public interface ILineService {

    /**
     * 获取实时数据
     * @param stockCode
     * @return
     */
    RealTimeBO getRealTime(String stockCode);

    /**
     * 获取分时线
     * @param startTime
     * @param stockCode
     * @return
     */
    List<TimelineBO> getTimeline(Date startTime, String stockCode);

    /**
     * 获取K线
     * @param startTime
     * @param stockCode
     * @param lineTypeEnum
     * @return
     */
    List<KlineBO> getKline(Date startTime, String stockCode, LineTypeEnum lineTypeEnum) throws Exception;

    /**
     * 获取行业实时数据
     * @param indexUniCode
     * @return
     */
    PublicCompanyTimeBO getPublicCompanyRealTime(Long indexUniCode);
}
