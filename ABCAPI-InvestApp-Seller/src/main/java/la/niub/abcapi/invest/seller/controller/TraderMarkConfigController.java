package la.niub.abcapi.invest.seller.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigDimensionResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigObjectResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigWeightResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkConfigWeightUserInfoResponse;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkConfigDimensionAddVo;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkConfigDimensionUpdateVo;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkConfigModeAddVo;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkConfigWeightAddVo;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkConfigWeightUpdateVo;
import la.niub.abcapi.invest.seller.service.ITraderMarkConfigService;

/**
 * @author : ppan
 * @description : 券商评分设置
 * @date : 2019-01-19 11:44
 */
@RestController
@RequestMapping("/tradermark/config")
@Validated
public class TraderMarkConfigController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ITraderMarkConfigService traderMarkConfigService;

    @Value("${traderMark.config.object.excelTemplateUrl}")
    private String traderMarkConfigObjectExcelTemplateUrl;

    /**
     * 查询评价模式列表和当前公司的评价模式
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/mode/list")
    public Response getModeList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        Map<String, Object> result = traderMarkConfigService.getModeList(userId);
        return new Response(result);
    }

    /**
     * 保存或更新评价模式配置
     * @param configModeSaveVo
     * @return
     * @throws Exception
     */
    @PostMapping("/mode/save")
    public Response saveOrUpdateMode(@RequestBody @Valid TraderMarkConfigModeAddVo configModeSaveVo) throws Exception {
        traderMarkConfigService.saveOrUpdateMode(configModeSaveVo);
        return new Response();
    }

    /**
     * 查询评价维度
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/dimension/list")
    public Response getDimensionList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        List<TraderMarkConfigDimensionResponse> result = traderMarkConfigService.getDimensionList(userId);
        return new Response(result);
    }

    /**
     * 保存评价维度
     * @param configDimensionAddVo
     * @return
     * @throws Exception
     */
    @PostMapping("/dimension/save")
    public Response saveDimension(@RequestBody @Valid TraderMarkConfigDimensionAddVo configDimensionAddVo) throws Exception {
        traderMarkConfigService.saveDimension(configDimensionAddVo);
        return new Response();
    }

    /**
     * 获取评价维度详情
     * @param id
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/dimension/detail")
    public Response getDimensionDetail(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        return new Response(traderMarkConfigService.getDimensionDetail(userId, id));
    }

    /**
     * 更新评价维度
     * @param configDimensionUpdateVo
     * @return
     * @throws Exception
     */
    @PostMapping("/dimension/update")
    public Response updateDimension(@RequestBody @Valid TraderMarkConfigDimensionUpdateVo configDimensionUpdateVo) throws Exception {
        traderMarkConfigService.updateDimension(configDimensionUpdateVo);
        return new Response();
    }

    /**
     * 删除评价维度
     * @param userId
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/dimension/delete")
    public Response deleteDimension(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        traderMarkConfigService.deleteDimension(userId, id);
        return new Response();
    }

    /**
     * 评价对象下载模板
     * @return
     * @throws Exception
     */
    @GetMapping("/object/download")
    public Response downloadObjectTemplate() throws Exception {
        JSONObject result = new JSONObject();
        result.put("templateUrl", traderMarkConfigObjectExcelTemplateUrl);
        return new Response(result);
    }

    /**
     * 上传评价对象excel
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/object/upload")
    public Response uploadObject(MultipartFile file, @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        traderMarkConfigService.uploadObject(file, userId);
        return new Response();
    }

    /**
     * 查询评价对象券商列表
     * @param userId
     * @param keyword
     * @param limit
     * @return
     * @throws Exception
     */
    @GetMapping("/object/broker")
    public Response getObjectBrokerList(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword, Integer limit) throws Exception {
        List<Map<String, Object>> result = traderMarkConfigService.getObjectBrokerList(userId, keyword, limit);
        return new Response(result);
    }

    /**
     * 查询评价对象
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/object/list")
    public Response getObjectList(@NotBlank(message = "参数[userId]不能为空") String userId, String brokers) throws Exception {
        List<TraderMarkConfigObjectResponse> result = traderMarkConfigService.getObjectList(userId, brokers);
        return new Response(result);
    }

    /**
     * 获取权重列表
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/weight/list")
    public Response getWeightList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        List<TraderMarkConfigWeightResponse> result = traderMarkConfigService.getWeightList(userId);
        return new Response(result);
    }

    /**
     * 根据关键字搜索用户账号信息
     * @param userId
     * @param keyword
     * @return
     * @throws Exception
     */
    @GetMapping("/weight/suggestuser")
    public Response getUserInfoList(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword, @RequestParam(defaultValue = "20") Integer limit) throws Exception {
        List<TraderMarkConfigWeightUserInfoResponse> result = traderMarkConfigService.getUserInfoList(userId, keyword, limit);
        return new Response(result);
    }

    /**
     * 保存评价权重
     * @param configWeightAddVo
     * @return
     * @throws Exception
     */
    @PostMapping("/weight/save")
    public Response saveWeight(@RequestBody @Valid TraderMarkConfigWeightAddVo configWeightAddVo) throws Exception {
        traderMarkConfigService.saveWeight(configWeightAddVo);
        return new Response();
    }

    /**
     * 查询评价权重详情
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/weight/detail")
    public Response getWeightDetail(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        return new Response(traderMarkConfigService.getWeightDetail(userId, id));
    }

    /**
     * 更新评价权重
     * @param configWeightUpdateVo
     * @return
     * @throws Exception
     */
    @PostMapping("/weight/update")
    public Response updateWeight(@RequestBody @Valid TraderMarkConfigWeightUpdateVo configWeightUpdateVo) throws Exception {
        traderMarkConfigService.updateWeight(configWeightUpdateVo);
        return new Response();
    }

    /**
     * 删除评价权重
     * @param userId
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/weight/delete")
    public Response deleteWeight(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[id]不能为null") Long id) throws Exception {
        traderMarkConfigService.deleteWeight(userId, id);
        return new Response();
    }
}
