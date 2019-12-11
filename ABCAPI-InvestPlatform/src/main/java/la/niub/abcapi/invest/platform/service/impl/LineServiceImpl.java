package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.component.client.IMarketClient;
import la.niub.abcapi.invest.platform.component.util.TimeUtil;
import la.niub.abcapi.invest.platform.config.enums.LineTypeEnum;
import la.niub.abcapi.invest.platform.model.bo.line.KlineBO;
import la.niub.abcapi.invest.platform.model.bo.line.PublicCompanyTimeBO;
import la.niub.abcapi.invest.platform.model.bo.line.RealTimeBO;
import la.niub.abcapi.invest.platform.model.bo.line.TimelineBO;
import la.niub.abcapi.invest.platform.model.bo.sso.UserTokenBO;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.KlineDataResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.KlineResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.RealTimeResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.TimelineDataResponse;
import la.niub.abcapi.invest.platform.model.response.client.lineV2.TimelineResponse;
import la.niub.abcapi.invest.platform.service.ILineService;
import la.niub.abcapi.invest.platform.service.ISsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class LineServiceImpl implements ILineService {

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private IMarketClient marketClient;

    @Override
    public RealTimeBO getRealTime(String stockCode) {
        UserTokenBO userTokenBO = ssoService.getUserToken();
        if (userTokenBO == null){
            return null;
        }
//        Map<String, String> param = new HashMap<>();
//        param.put("stockCode", stockCode.toUpperCase());
//        param.put("userId", userTokenBO.getUserId());
//        param.put("token", userTokenBO.getToken());
//        String url = "https://api-dev.analyst.ai"+"/api/quotes/rest/v1/quote";
//        String resultStr = HttpUtil.get(url, param, null);
//        Response<RealTimeResponse> timelineResponse = JSON.parseObject(resultStr, new TypeReference<Response<RealTimeResponse>>(){});
        Response<RealTimeResponse> timelineResponse = marketClient.getRealTime(stockCode,userTokenBO.getUserId(),userTokenBO.getToken());
        RealTimeResponse data = timelineResponse.getData();
        if (data == null){
            return null;
        }

        RealTimeBO bo = new RealTimeBO();
        bo.setTime(data.getTime());
        bo.setTrade_date(data.getTime());
        bo.setNow(data.getNow());
        bo.setDiffer(data.getDiffer());
        bo.setDiffer_range(data.getDifferPercent());
        bo.setVolume(data.getVolume());
        bo.setAmount(data.getAmount());
        bo.setVolumeratio(data.getVolumeRatio());
        bo.setCommissiondiff(data.getCommissionDiff());
        bo.setBuyprice1(data.getBuyPrice1());
        bo.setBuyprice2(data.getBuyPrice2());
        bo.setBuyprice3(data.getBuyPrice3());
        bo.setBuyprice4(data.getBuyPrice4());
        bo.setBuyprice5(data.getBuyPrice5());
        bo.setBuyvolume1(data.getBuyVolume1());
        bo.setBuyvolume2(data.getBuyVolume2());
        bo.setBuyvolume3(data.getBuyVolume3());
        bo.setBuyvolume4(data.getBuyVolume4());
        bo.setBuyvolume5(data.getBuyVolume5());
        bo.setSellprice1(data.getSellPrice1());
        bo.setSellprice2(data.getSellPrice2());
        bo.setSellprice3(data.getSellPrice3());
        bo.setSellprice4(data.getSellPrice4());
        bo.setSellprice5(data.getSellPrice5());
        bo.setSellvolume1(data.getSellVolume1());
        bo.setSellvolume2(data.getSellVolume2());
        bo.setSellvolume3(data.getSellVolume3());
        bo.setSellvolume4(data.getSellVolume4());
        bo.setSellvolume5(data.getSellVolume5());
        bo.setHigh(data.getHigh());
        bo.setLow(data.getLow());
        bo.setOpen(data.getOpen());
        bo.setPre_close(data.getPrevClose());
        bo.setSuspension(data.getSuspended() ? "Y" : "N");
        bo.setCommission(data.getCommissionDiff());
//        bo.setRoundlot(data.getOpen());
//        bo.setOutvolume(data.getBuyPrice1());
//        bo.setInvolume(data.getBuyPrice1());
        bo.setRise_num(data.getUps());
        bo.setFall_num(data.getDowns());
        bo.setFair_num(data.getFlats());
        bo.setLiqmv(data.getLiqmv());
        bo.setMv(data.getMv());
        bo.setPe(data.getPe());
        bo.setPb(data.getPb());
        bo.setSwing(data.getAmplitude());
        bo.setChange(data.getTurnoverRate());

        return bo;
    }

    @Override
    public List<TimelineBO> getTimeline(Date startTime, String stockCode) {
        UserTokenBO userTokenBO = ssoService.getUserToken();
        if (userTokenBO == null){
            return new ArrayList<>();
        }
//        Map<String, String> param = new HashMap<>();
//        param.put("stockCode", stockCode.toUpperCase());
//        param.put("startDate", "2019-02-15");
//        param.put("endDate", "2019-02-15");
//        param.put("userId", userTokenBO.getUserId());
//        param.put("token", userTokenBO.getToken());
//        String url = "https://api-dev.analyst.ai"+"/api/quotes/rest/v1/trending";
//        String resultStr = HttpUtil.get(url, param, null);
//        Response<TimelineResponse> timelineResponse = JSON.parseObject(resultStr, new TypeReference<Response<TimelineResponse>>(){});
        Response<TimelineResponse> timelineResponse = marketClient.getTimeline(stockCode,userTokenBO.getUserId(),userTokenBO.getToken());
        if (timelineResponse.getData() == null){
            return new ArrayList<>();
        }
        List<TimelineDataResponse> data = timelineResponse.getData().getTrending();
        if (ObjectUtils.isEmpty(data)){
            return new ArrayList<>();
        }

        List<TimelineBO> list = new LinkedList<>();
        for (TimelineDataResponse item : data){
            TimelineBO bo = new TimelineBO();
            bo.setTrade_date(item.getTimestamp());
            bo.setOpen(item.getNow() != null ? BigDecimal.valueOf(item.getNow()) : null);
            bo.setAvg_price(item.getAvg() != null ? BigDecimal.valueOf(item.getAvg()) : null);
            bo.setDiffer(item.getDiffer() != null ? BigDecimal.valueOf(item.getDiffer()) : null);
            bo.setDiffer_range(item.getDifferPercent() != null ? BigDecimal.valueOf(item.getDifferPercent()) : null);
            bo.setVolume(item.getVolume());
            bo.setAmount(item.getAmount());
            bo.setTurn(item.getTurnoverRate() != null ? BigDecimal.valueOf(item.getTurnoverRate()) : null);
            bo.setAmount_unit("元");
            bo.setAvg_price_unit("元");
            bo.setDiffer_unit("元");
            bo.setOpen_unit("元");
            bo.setVolume_unit("股");
//            if (bo.getTurn() != null){
//                bo.setTurn(bo.getTurn().multiply(new BigDecimal(100)));
//            }
//            if (bo.getDiffer_range() != null){
//                bo.setDiffer_range(bo.getDiffer_range().multiply(new BigDecimal(100)));
//            }
//            if (bo.getVolume() != null){
//                bo.setVolume(bo.getVolume().multiply(new BigDecimal(100)));
//            }

            list.add(bo);
        }
        return list;
    }

    @Override
    public List<KlineBO> getKline(Date startTime, String stockCode, LineTypeEnum lineTypeEnum) throws Exception {
        String keyPattern = null;
        switch (lineTypeEnum){
            case DAY:
            case WEEK:
                keyPattern = "yyyyMMdd";
                break;
            case MONTH:
            case QUARTER:
            case HALFYEAR:
                keyPattern = "yyyyMM";
                break;
            case YEAR:
                keyPattern = "yyyy";
                break;
            case MIN1:
            case MIN5:
            case MIN15:
            case MIN30:
            case MIN60:
                keyPattern = "yyyyMMddHHmm";
                break;
            default:
                return new ArrayList<>();
        }

        UserTokenBO userTokenBO = ssoService.getUserToken();
        if (userTokenBO == null){
            return new ArrayList<>();
        }
//        Map<String, String> param = new HashMap<>();
//        param.put("stockCode", stockCode.toUpperCase());
//        param.put("startDate", TimeUtil.toString(startTime,"yyyy-MM-dd"));
//        param.put("period", lineTypeEnum.getTagV2());
//        param.put("userId", userTokenBO.getUserId());
//        param.put("token", userTokenBO.getToken());
//        String url = "https://api-dev.analyst.ai"+"/api/quotes/rest/v1/kx";
//        String resultStr = HttpUtil.get(url, param, null);
//        Response<KlineResponse> klineResponse = JSON.parseObject(resultStr, new TypeReference<Response<KlineResponse>>(){});
        Response<KlineResponse> klineResponse = marketClient.getKline(stockCode.toUpperCase(),TimeUtil.toString(startTime,"yyyy-MM-dd"),
                lineTypeEnum.getTagV2(),userTokenBO.getUserId(),userTokenBO.getToken());
        if (klineResponse.getData() == null){
            return new ArrayList<>();
        }
        List<KlineDataResponse> data = klineResponse.getData().getData();
        if (ObjectUtils.isEmpty(data)){
            new ArrayList<>();
        }

        Map<String, KlineBO> listMap = new TreeMap<>();

        for (KlineDataResponse item : data){
            KlineBO bo = new KlineBO();
            bo.setTrade_date(item.getTimestamp());
            bo.setClose_price(item.getClose() != null ? BigDecimal.valueOf(item.getClose()) : null);
            bo.setOpen(item.getOpen() != null ? BigDecimal.valueOf(item.getOpen()) : null);
            bo.setHigh(item.getHigh() != null ? BigDecimal.valueOf(item.getHigh()) : null);
            bo.setLow(item.getLow() != null ? BigDecimal.valueOf(item.getLow()) : null);
            bo.setVolume(item.getVolume());
            bo.setAmount(item.getAmount());
            bo.setDiffer(item.getDiffer() != null ? BigDecimal.valueOf(item.getDiffer()) : null);
            bo.setDiffer_range(item.getDifferPercent() != null ? BigDecimal.valueOf(item.getDifferPercent()) : null);
            bo.setTurn(item.getTurnoverRate() != null ? BigDecimal.valueOf(item.getTurnoverRate()) : null);
            bo.setAmplitude(item.getAmplitude() != null ? BigDecimal.valueOf(item.getAmplitude()) : null);
//            if (bo.getTurn() != null){
//                bo.setTurn(bo.getTurn().multiply(new BigDecimal(100)));
//            }
//            if (bo.getDiffer_range() != null){
//                bo.setDiffer_range(bo.getDiffer_range().multiply(new BigDecimal(100)));
//            }
//            if (bo.getVolume() != null){
//                bo.setVolume(bo.getVolume().multiply(new BigDecimal(100)));
//            }
//            if (bo.getAmplitude() != null){
//                bo.setAmplitude(bo.getAmplitude().multiply(new BigDecimal(100)));
//            }

            listMap.put(TimeUtil.toString(item.getTimestamp(),keyPattern),bo);
        }

        //整理成list返回
        List<KlineBO> list = new LinkedList<>();
        for (Map.Entry<String, KlineBO> entry : listMap.entrySet()){
            list.add(entry.getValue());
        }

        /**
         * 把最后一个动态数据的时间设置成对应时间类型后一个区间的时间
         * 比如15分钟线最后一个动态数据时间需要是最接近的未来15分钟，如6点15、6点30、6点45、7点
         */
        switch (lineTypeEnum){
            case MIN5:
            case MIN15:
            case MIN30:
            case MIN60:
                KlineBO lastKlineCompanyBO = ((LinkedList<KlineBO>) list).getLast();
                Calendar lastCalendar = Calendar.getInstance();
                lastCalendar.setTime(lastKlineCompanyBO.getTrade_date());
                int minute = lastCalendar.get(Calendar.MINUTE);

                int minuteInterval = lineTypeEnum.getMinuteInterval();
                if (minute%minuteInterval != 0){
                    lastCalendar.add(Calendar.MINUTE,-minute%minuteInterval);
                    lastCalendar.add(Calendar.MINUTE,minuteInterval);
                }
                lastKlineCompanyBO.setTrade_date(lastCalendar.getTime());
                break;
            default:
                break;
        }

        return list;
    }

    @Override
    public PublicCompanyTimeBO getPublicCompanyRealTime(Long indexUniCode) {
        return null;
    }
}
