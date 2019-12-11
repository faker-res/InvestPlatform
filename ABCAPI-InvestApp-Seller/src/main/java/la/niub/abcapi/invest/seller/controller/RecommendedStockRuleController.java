package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.model.RecommendedStockRuleModel;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleAddVo;
import la.niub.abcapi.invest.seller.model.vo.RecommendedStockRuleUpdateVo;
import la.niub.abcapi.invest.seller.service.IRecommendedStockRuleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 金股提取规则
 * @date : 2019-01-16 15:51
 */
@RestController
@RequestMapping("/recommendedstock/rule")
@Validated
public class RecommendedStockRuleController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IRecommendedStockRuleService recommendedStockRuleService;

    /**
     * 金股提取规则列表-券商筛选
     * @param userId
     * @param keyword
     * @param limit
     * @return
     * @throws Exception
     */
    @GetMapping("/suggestbroker")
    public Response getBrokerList(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword, Integer limit) throws Exception {
        List<Map<String, String>> result = recommendedStockRuleService.getBrokerList(userId, keyword, limit);
        return new Response(result);
    }

    /**
     * 金股提取规则列表
     * @param userId
     * @param brokers 券商名称，可为空,多个用逗号分隔
     * @return
     */
    @GetMapping("/list")
    public Response getRuleList(@NotBlank(message = "参数[userId]不能为空")String userId, String brokers) throws Exception {
        List<RecommendedStockRuleModel> result = recommendedStockRuleService.getRuleList(userId, brokers);
        return new Response(result);
    }

    /**
     * 新增金股提取规则
     * @param ruleAddVo
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    public Response save(@RequestBody @Valid RecommendedStockRuleAddVo ruleAddVo) throws Exception {
        recommendedStockRuleService.save(ruleAddVo);
        return new Response();
    }

    /**
     * 获取金股提取规则详情
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/detail")
    public Response getRuleDetail(@NotBlank(message = "参数[userId]不能为空")String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        return new Response(recommendedStockRuleService.getRuleDetail(userId, id));
    }

    /**
     * 更新金股提取规则
     * @param ruleUpdateVo
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public Response update(@RequestBody @Valid RecommendedStockRuleUpdateVo ruleUpdateVo) throws Exception {
        recommendedStockRuleService.update(ruleUpdateVo);
        return new Response();
    }

    /**
     * 删除金股提取规则
     * @param userId
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/delete")
    public Response delete(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        recommendedStockRuleService.delete(userId, id);
        return new Response();
    }
}
