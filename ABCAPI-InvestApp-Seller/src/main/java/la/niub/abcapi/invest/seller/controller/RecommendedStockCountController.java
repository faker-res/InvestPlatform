package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.component.exception.ValidatorException;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import la.niub.abcapi.invest.seller.component.util.TimeUtil;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockBrokerRateVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockCountVo;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.IRecommendedStockCountService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 金股统计
 * @date : 2019-01-15 17:03
 */
@RestController
@RequestMapping("/recommendedstock/count")
@Validated
public class RecommendedStockCountController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IRecommendedStockCountService recommendedStockCountService;

    /**
     * 股票行业分布
     * @param userId
     * @param date
     * @return
     */
    @GetMapping("/industry")
    public Response industryStatistics(@NotBlank(message = "参数[userId]不能为空") String userId, String date) throws Exception {
        if (StringUtil.isEmpty(date) || !TimeUtil.checkDateFormat(date, "yyyy-MM")) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER.getCode(), "error date format");
        }

        Map<String, Object> result = recommendedStockCountService.getIndustryStatistics(userId, date);
        return new Response(result);
    }

    /**
     * 月度金股统计-筛选框股票
     * @param userId
     * @param date
     * @param keyword
     * @return
     */
    @GetMapping("/suggeststock")
    public Response getStockList(@NotBlank(message = "参数[userId]不能为空") String userId, String date, String keyword) throws Exception {
        if (StringUtil.isEmpty(date) || !TimeUtil.checkDateFormat(date, "yyyy-MM")) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER.getCode(), "error date format");
        }

        List<Map<String, Object>> result = recommendedStockCountService.getStockList(userId, date, keyword);
        return new Response(result);
    }

    /**
     * 月度金股统计列表
     * @param recommendedStockCountVo
     * @return
     */
    @GetMapping("/monthstock")
    public Response monthStockStatistics(@Valid RecommendedStockCountVo recommendedStockCountVo) throws Exception {
        String date = recommendedStockCountVo.getDate();
        if (StringUtil.isEmpty(date) || !TimeUtil.checkDateFormat(date, "yyyy-MM")) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER.getCode(), "error date format");
        }

        Map<String, Object> result = recommendedStockCountService.getMonthStockStatistics(recommendedStockCountVo);
        return new Response(result);
    }

    /**
     * 券商金股统计-筛选框券商
     * @param userId
     * @param date
     * @param keyword
     * @return
     */
    @GetMapping("/suggestbroker")
    public Response getBrokerList(@NotBlank(message = "参数[userId]不能为空") String userId, String date, String keyword) throws Exception {
        if (StringUtil.isEmpty(date) || !TimeUtil.checkDateFormat(date, "yyyy-MM")) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER.getCode(), "error date format");
        }

        List<Map<String, Object>> result = recommendedStockCountService.getBrokerList(userId, date, keyword);
        return new Response(result);
    }

    /**
     * 券商金股统计列表
     * @param recommendedStockCountVo
     * @return
     */
    @GetMapping("/brokerstock")
    public Response brokerStockStatistics(@Valid RecommendedStockCountVo recommendedStockCountVo) throws Exception {
        String date = recommendedStockCountVo.getDate();
        if (StringUtil.isEmpty(date) || !TimeUtil.checkDateFormat(date, "yyyy-MM")) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER.getCode(), "error date format");
        }

        Map<String, Object> result = recommendedStockCountService.getBrokerStockStatistics(recommendedStockCountVo);
        return new Response(result);
    }

    /**
     * 统计券商组合收益
     * @param brokerRateVo
     * @return
     */
    @GetMapping("/brokerrate")
    public Response countBrokerRate(@Valid RecommendedStockBrokerRateVo brokerRateVo) throws Exception {
        List<Map<String, Object>> data_ = recommendedStockCountService.countBrokerRate(brokerRateVo);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        if (!CollectionUtils.isEmpty(data_)) {
            data_.forEach(action -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", action.get("broker"));
                map.put("y", action.get("combinedRate"));
                data.add(map);
            });
        }
        return new Response(data);
    }

}
