package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.component.util.TimeUtil;
import la.niub.abcapi.invest.platform.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.platform.config.enums.LineTypeEnum;
import la.niub.abcapi.invest.platform.model.bo.line.KlineBO;
import la.niub.abcapi.invest.platform.model.bo.line.RealTimeBO;
import la.niub.abcapi.invest.platform.model.bo.line.TimelineBO;
import la.niub.abcapi.invest.platform.model.request.KlineRequest;
import la.niub.abcapi.invest.platform.model.request.TimelineRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetMonthReturnRateRequest;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetPriceByDayRequest;
import la.niub.abcapi.invest.platform.model.request.mystock.StockPricesRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.stock.SecPlateInfoResponse;
import la.niub.abcapi.invest.platform.service.ILineService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 股票相关
 */
@RestController
@RequestMapping(path = "/stock")
public class StockController {

    private final static Logger logger = LogManager.getLogger(StockController.class);

    @Autowired
    private IInvestCloudClient investCloudClient;

    @Autowired
    private ILineService lineService;

    /**
     * 根据名字或代码获取股票
     * @param names_or_codes
     * @return
     */
    @GetMapping(path = "/getByNameOrCode")
    Response getStockByNameOrCode(@RequestParam("names_or_codes") String names_or_codes) {
        try{
            List<String> nameOrCode = Arrays.asList(names_or_codes.split(","));
            Response result = investCloudClient.getStockByNameOrCode(nameOrCode);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 根据名字或代码获取股票
     * @param names_or_codes
     * @return
     */
    @PostMapping(path = "/getByNameOrCode")
    Response getStockByNameOrCode(@RequestBody List<String> names_or_codes) {
        try{
            Response result = investCloudClient.getStockByNameOrCode(names_or_codes);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取股票行业
     * @param sec_uni_codes
     * @return
     */
    @GetMapping(path = "/getIndustry")
    Response getIndustry(@RequestParam("sec_uni_codes") String sec_uni_codes) {
        try{
            List<Long> secUniCodes = new ArrayList<>();
            Arrays.asList(sec_uni_codes.split(",")).forEach((item)->{
                secUniCodes.add(Long.valueOf(item));
            });
            Response result = investCloudClient.getIndustry(secUniCodes);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取股票日行情
     * @param sec_uni_codes
     * @param day
     * @return
     */
    @GetMapping(path = "/getPriceByDay")
    Response getPriceByDay(@RequestParam("sec_uni_codes") String sec_uni_codes,@RequestParam(value = "day",required = false) String day) {
        try{
            List<Long> secUniCodes = new ArrayList<>();
            Arrays.asList(sec_uni_codes.split(",")).forEach((item)->{
                secUniCodes.add(Long.valueOf(item));
            });
            Date dayDate = StringUtils.isNotEmpty(day) ? TimeUtil.parseDateStr(day,"yyyy-MM-dd") : null;

            StockGetPriceByDayRequest param = new StockGetPriceByDayRequest();
            param.setSecUniCodes(secUniCodes);
            param.setDay(dayDate);
            Response result = investCloudClient.getPriceByDay(param);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取股票月盈利
     * @param sec_uni_codes
     * @return
     */
    @GetMapping(path = "/getMonthReturnRate")
    Response getMonthReturnRate(@RequestParam("sec_uni_codes") String sec_uni_codes,@RequestParam(value = "month",required = false) String month) {
        try{
            List<Long> secUniCodes = new ArrayList<>();
            Arrays.asList(sec_uni_codes.split(",")).forEach((item)->{
                secUniCodes.add(Long.valueOf(item));
            });
            Date monthDate = StringUtils.isNotEmpty(month) ? TimeUtil.parseDateStr(month,"yyyy-MM") : null;

            StockGetMonthReturnRateRequest param = new StockGetMonthReturnRateRequest();
            param.setSecUniCodes(secUniCodes);
            param.setMonth(monthDate);
            Response result = investCloudClient.getMonthReturnRate(param);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 分时线
     * @return
     */
    @GetMapping(path = "/getTimeline")
    Response getTimeline(TimelineRequest param) {
        try{
            Map<String, TimelineBO> timelineBOMap = new TreeMap<>();

            Date startTime = new Date();
            if (StringUtils.isNotEmpty(param.getStart_time())){
                startTime = TimeUtil.parseDateStr(param.getStart_time(),"HHmm");
            }else{
                //默认时间
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTime(startTime);
                calendar.add(Calendar.HOUR, -2);
                startTime = calendar.getTime();
            }

            List<TimelineBO> timelineBOList = lineService.getTimeline(startTime,param.getStock_code());
            for (TimelineBO item : timelineBOList){
                String timestampStr = TimeUtil.toString(item.getTrade_date(),"yyyy-MM-dd HH:mm:ss");
                timelineBOMap.put(timestampStr,item);
            }

            return new Response(timelineBOMap);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * K线
     * @param param
     * @return
     */
    @GetMapping(path = "/getKline")
    Response getKline(KlineRequest param) {
        try{
            String keyPattern = "yyyy-MM-dd HH:mm:ss";
            Map<String, KlineBO> klineBOMap = new TreeMap<>();
            //默认时间
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(new Date());
            calendar.set(Calendar.MILLISECOND,0);

            LineTypeEnum lineTypeEnum;
            switch (param.getLine_type()){
                case DAY://日
                    calendar.add(Calendar.MONTH, -3);
                    lineTypeEnum = LineTypeEnum.DAY;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case WEEK://周
                    calendar.add(Calendar.MONTH, -6);
                    lineTypeEnum = LineTypeEnum.WEEK;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case MONTH://月
                    calendar.add(Calendar.YEAR, -5);
                    lineTypeEnum = LineTypeEnum.MONTH;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case QUARTER://季度
                    calendar.add(Calendar.YEAR, -10);
                    lineTypeEnum = LineTypeEnum.QUARTER;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case HALFYEAR://半年
                    calendar.add(Calendar.HOUR, -2);
                    lineTypeEnum = LineTypeEnum.HALFYEAR;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case YEAR://年
                    calendar.add(Calendar.HOUR, -2);
                    lineTypeEnum = LineTypeEnum.YEAR;
                    keyPattern = "yyyy-MM-dd 00:00:00";
                    break;
                case MIN1://1分钟
                    calendar.add(Calendar.HOUR, -4);
                    lineTypeEnum = LineTypeEnum.MIN1;
                    break;
                case MIN5://5分钟
                    calendar.add(Calendar.HOUR, -4);
                    lineTypeEnum = LineTypeEnum.MIN5;
                    break;
                case MIN15://15分钟
                    calendar.add(Calendar.HOUR, -5);
                    lineTypeEnum = LineTypeEnum.MIN15;
                    break;
                case MIN30://30分钟
                    calendar.add(Calendar.DAY_OF_YEAR, -8);
                    lineTypeEnum = LineTypeEnum.MIN30;
                    break;
                case MIN60://60分钟
                    calendar.add(Calendar.DAY_OF_YEAR, -16);
                    lineTypeEnum = LineTypeEnum.MIN60;
                    break;
                default:
                    return new ErrorResponse<>(SystemEnumCodeConfig.UNSUPPORTED_TYPE);
            }

            Date startTime = StringUtils.isEmpty(param.getStart_time()) ? calendar.getTime() : TimeUtil.parseDateStr(param.getStart_time(),"yyyy-MM-dd");
            List<KlineBO> klineList = lineService.getKline(startTime,param.getStock_code(),lineTypeEnum);
            for (KlineBO item : klineList){
                String timestampStr = TimeUtil.toString(item.getTrade_date(),keyPattern);
                klineBOMap.put(timestampStr,item);
            }

            return new Response(klineBOMap);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 实时行情
     * @param stockCode
     * @return
     */
    @GetMapping(path = "/getRealTime")
    Response getRealTime(@RequestParam(value = "stock_code") String stockCode) {
        try{
            RealTimeBO result = lineService.getRealTime(stockCode);
            return new Response(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @GetMapping(path = "/getTradeDateByPrice")
    Response getTradeDateByPrice(@RequestParam("secUniCode") Long secUniCode, @RequestParam("startTime") String startTime, @RequestParam("targetPrice") BigDecimal targetPrice, @RequestParam("currentPrice") BigDecimal currentPrice) {
        try{
            Response result = investCloudClient.getTradeDateByPrice(secUniCode, startTime, targetPrice, currentPrice);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取所有指数id
     * @param marketId
     * @return
     */
    @GetMapping(path = "/getSecUniCodeByMarketCode")
    Response getSecUniCodeByMarketCode(@RequestParam("marketId") Integer marketId) {
        try{
            Response result = investCloudClient.getSecUniCodeByMarketCode(marketId);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 根据plateCode获取指数信息
     * @param plateCode
     * @return
     */
    @GetMapping(path = "/getSecIndexPlateInfo")
    Response<List<SecPlateInfoResponse>> getSecIndexPlateInfoByPlateCode(@RequestParam("plateCode") String plateCode) {
        try{
            Response result = investCloudClient.getSecIndexPlateInfoByPlateCode(plateCode);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @GetMapping(path = "/getByCodeName")
    public Response getByCodeName(@RequestParam("stockCodes") String stockCodes, @RequestParam("stockNames") String stockNames) {
        try{
            return investCloudClient.getByCodeName(stockCodes, stockNames);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @PostMapping(value = "/stock-stat-card")
    public Response getPricesByStocks(@RequestBody StockPricesRequest stockPricesRequest) {
        try{
            return investCloudClient.getPricesByStocks(stockPricesRequest);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @GetMapping(value = "/stock-card-base")
    public Response stockCardBase(@RequestParam(value = "stockCode", required = false) String stockCode,
                                  @RequestParam(value = "secUniCode", required = false) String secUniCode) {
        try{
            return investCloudClient.stockCardBase(stockCode, secUniCode);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 根据secUniCode获取申万一级行业信息
     * @param secUniCode
     * @return
     */
    @GetMapping(value = "/industry")
    public Response getSWFirstIndustryInfo(@RequestParam("secUniCode") Long secUniCode) {
        try{
            return investCloudClient.getSWFirstIndustryInfo(secUniCode);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
}
