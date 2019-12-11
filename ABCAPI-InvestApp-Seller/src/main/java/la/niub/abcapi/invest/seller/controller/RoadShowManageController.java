package la.niub.abcapi.invest.seller.controller;

import com.alibaba.fastjson.JSONObject;
import la.niub.abcapi.invest.seller.component.exception.ValidatorException;
import la.niub.abcapi.invest.seller.config.code.SystemEnumCodeConfig;
import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.vo.RoadShowAddVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowQueryVo;
import la.niub.abcapi.invest.seller.model.vo.RoadShowSellerAnalystSaveVo;
import la.niub.abcapi.invest.seller.service.IRoadShowManageService;
import la.niub.abcapi.invest.seller.service.IUserInfoService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : ppan
 * @description : 路演管理
 * @date : 2019-02-11 10:52
 */
@RestController
@RequestMapping("/roadshow/manage")
@Validated
public class RoadShowManageController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRoadShowManageService roadShowManageService;

    @Autowired
    private IUserInfoService userInfoService;

    @Value("${traderMark.config.object.excelTemplateUrl}")
    private String traderMarkConfigObjectExcelTemplateUrl;

    /**
     * 新增路演
     * @param roadShowAddVo
     * @return
     */
    @PostMapping("save")
    public Response save(@RequestBody @Valid RoadShowAddVo roadShowAddVo) throws Exception {
        roadShowManageService.save(roadShowAddVo);
        return new Response();
    }

    /**
     * 查询路演列表-支持高级筛选-券商视角
     * @param roadShowQueryVo
     * @return
     */
    @PostMapping("/seller/list")
    public Response getRoadShowListByBroker(@RequestBody @Valid RoadShowQueryVo roadShowQueryVo) throws Exception {
        return new Response(roadShowManageService.getRoadShowListByBroker(roadShowQueryVo));
    }

    /**
     * 查询路演列表-支持高级筛选-基金公司视角
     * @param roadShowQueryVo
     * @return
     */
    @PostMapping("/buyer/list")
    public Response getRoadShowListByFund(@RequestBody @Valid RoadShowQueryVo roadShowQueryVo) throws Exception {
        return new Response(roadShowManageService.getRoadShowListByFund(roadShowQueryVo));
    }

    /**
     * 查询买方研究员列表
     * @param parentId
     * @return
     */
    @GetMapping("/analyst/buyer/list")
    public Response queryBuyerResearcherList(@NotBlank(message = "参数[parentId]不能为空") String parentId) throws Exception {
        return new Response(userInfoService.getUserInfoList(parentId, CompanyTypeEnum.FUND));
    }

    /**
     * 查询卖方研究员列表 弃用
     * @param userId
     * @return
     */
    @GetMapping("/analyst/seller/list")
    public Response querySellerResearcherList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        String parentId = userInfoService.getUserParentId(userId);
        return new Response(userInfoService.getUserInfoList(parentId, CompanyTypeEnum.FUND));
    }

    /**
     * 查询父账号列表
     * @return
     */
    @GetMapping("/fundcompany")
    public Response getFundCompanyList() throws Exception {
        return new Response(userInfoService.getFundCompanyList(CompanyTypeEnum.FUND));
    }

    /**
     * 添加行程
     * @param roadShowId
     * @param userId
     * @return
     */
    @GetMapping("/jointrip")
    public Response joinTrip(@NotNull(message = "参数[roadShowId]不能为空") Long roadShowId, @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        log.info("joinTrip roadShowId:{},userId:{}", roadShowId, userId);
        roadShowManageService.joinTrip(roadShowId, userId);
        return new Response("添加行程成功");
    }

    /**
     * 取消行程
     * @param roadShowId
     * @param userId
     * @return
     */
    @GetMapping("/canceltrip")
    public Response cancelTrip(@NotNull(message = "参数[roadShowId]不能为空") Long roadShowId, @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        log.info("cancelTrip roadShowId:{},userId:{}", roadShowId, userId);
        roadShowManageService.cancelTrip(roadShowId, userId);
        return new Response("取消行程成功");
    }

    /**
     * 根据路演id获取详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Response getRoadShowDetail(@NotNull(message = "参数[id]不能为空") Long id, @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        return new Response(roadShowManageService.getRoadShowDetail(id, userId));
    }

    /**
     * 根据id更新路演
     * @param roadShowAddVo
     * @param bindingResult
     * @return
     */
    @PostMapping("/update")
    public Response update(@RequestBody @Valid RoadShowAddVo roadShowAddVo, BindingResult bindingResult) throws Exception {
        if (roadShowAddVo.getId() == null) {
            throw new ValidatorException(SystemEnumCodeConfig.ERROR_VALIDATOR_PARAMETER);
        }

        roadShowManageService.updateById(roadShowAddVo);
        return new Response();
    }

    /**
     * 根据id删除路演
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Response delete(@NotNull(message = "参数[id]不能为空") Long id,
                           @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        roadShowManageService.deleteById(id, userId);
        return new Response();
    }


    /**
     * 获取券商路演管理筛选条件
     * @param userId
     * @return
     */
    @GetMapping("/seller/filtercondition")
    public Response getBrokerFilterCondition(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        return new Response(roadShowManageService.getBrokerFilterCondition(userId));
    }

    /**
     * 获取基金公司管理筛选条件
     * @return
     */
    @GetMapping("/buyer/filtercondition")
    public Response getFundCompanyFilterCondition(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        return new Response(roadShowManageService.getFundCompanyFilterCondition(userId));
    }

    /**
     * 会议室下拉提示
     * @param userId
     * @param keyword
     * @param limit
     * @return
     */
    @GetMapping("/meetingroom/list")
    public Response getMeetingRoomList(@NotBlank(message = "参数[userId]不能为空") String userId, String keyword, Integer limit) throws Exception {
        Set<String> result = roadShowManageService.getMeetingRoomList(userId, keyword, limit);
        return new Response(result);
    }

    /**
     * 根据id更新路演会议室
     * @param id
     * @param meetingRoom
     * @return
     */
    @GetMapping("/meetingroom/update")
    public Response updateMeetingRoom(@NotNull(message = "参数[id]不能为空") Long id, @NotBlank(message = "参数[meetingRoom]不能为空") String meetingRoom) throws Exception {
        roadShowManageService.updateMeetingRoomById(id, meetingRoom);
        return new Response();
    }

    /**
     * 根据券商评价行业过滤预约行业
     * @param userId
     * @param buyerCompanyId
     * @param keyword
     * @param limit
     * @return
     */
    @GetMapping("/industry")
    public Response getIndustryList(@NotBlank(message = "参数[userId]不能为空") String userId,
                                    @NotBlank(message = "参数[buyerCompanyId]不能为空") String buyerCompanyId,
                                    String keyword,
                                    Integer limit) throws Exception {
        List<String> result = roadShowManageService.getIndustryList(userId, buyerCompanyId, keyword, limit);
        return new Response(result);
    }

    /**
     * 路演管理添加卖方研究员
     * @param sellerAnalystSaveVo
     * @return
     */
    @PostMapping("/analyst/seller/save")
    public Response addSellerAnalyst(@RequestBody @Valid RoadShowSellerAnalystSaveVo sellerAnalystSaveVo) throws Exception {
        roadShowManageService.saveSellerAnalyst(sellerAnalystSaveVo);
        return new Response();
    }

    /**
     * 券商研究员列表 公众号
     * @param keyword
     * @return
     */
    @GetMapping("/analyst/seller/wechat/list")
    public Response getAnalystList(String keyword,
                                   @NotBlank(message = "参数[userId]不能为空") String userId,
                                   @NotBlank(message = "参数[buyerCompanyId]不能为空") String buyerCompanyId,
                                   @NotBlank(message = "参数[industry]不能为空") String industry) throws Exception {
        Map<String, Object> result = roadShowManageService.getSellerAnalystList(userId, keyword, buyerCompanyId, industry);
        return new Response(result);
    }

    /**
     * 路演导出
     * @param startTime
     * @param endTime
     * @param fields  字段见RoadShowExportEnum
     * @return
     * @throws Exception
     */
    @GetMapping("/export")
    public ResponseEntity<Resource> export(@NotBlank(message = "参数[userId]不能为空") String userId,
                                           @NotBlank(message = "参数[startTime]不能为空") String startTime,
                                           @NotBlank(message = "参数[endTime]不能为空") String endTime,
                                           String fields) throws Exception {
        log.info("导出路演参数,userId:{},startTime:{},endTime:{},fields:{}", userId, startTime, endTime, fields);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String fileName = "路演预约.xls";

        roadShowManageService.export(bos, userId, startTime, endTime, fields);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("charset", "utf-8");
        headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bos.toByteArray());
        headers.add("Content-Length", String.valueOf(byteArrayInputStream.available()));
        Resource resource = new InputStreamResource(byteArrayInputStream);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }

    /**
     * 上传券商行业研究员excel
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public Response uploadObject(MultipartFile file, @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        roadShowManageService.upload(file, userId);
        return new Response();
    }

    /**
     * 券商行业研究员excel下载模板
     * @return
     * @throws Exception
     */
    @GetMapping("/template/download")
    public Response downloadObjectTemplate() throws Exception {
        JSONObject result = new JSONObject();
        result.put("templateUrl", traderMarkConfigObjectExcelTemplateUrl);
        return new Response(result);
    }

    /**
     * 获取卖方研究员路演数量 华宝私有化部署
     * @param parentId
     * @param startTime
     * @param endTime
     * @param broker
     * @param industry
     * @param analyst
     * @return
     * @throws Exception
     */
    @GetMapping("/count")
    public Response getRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception {
        Integer result = roadShowManageService.getRoadShowCountBySellerAndDate(parentId, startTime, endTime, broker, industry, analyst);
        return new Response(result);
    }

    /**
     * 获取卖方研究员各公司路演数量 华宝私有化部署
     * @param parentId
     * @param startTime
     * @param endTime
     * @param broker
     * @param industry
     * @param analyst
     * @return
     * @throws Exception
     */
    @GetMapping("/company/count")
    public Response getCompanyRoadShowCountBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst) throws Exception {
        List<Map<String, Object>> result = roadShowManageService.getCompanyRoadShowCountBySellerAndDate(parentId, startTime, endTime, broker, industry, analyst);
        return new Response(result);
    }

    /**
     * 获取卖方研究员公司路演时间 华宝私有化部署
     * @param parentId
     * @param startTime
     * @param endTime
     * @param broker
     * @param industry
     * @param analyst
     * @param stockCode
     * @param stockName
     * @return
     * @throws Exception
     */
    @GetMapping("/company/time")
    public Response getRoadShowTimeBySellerAndDate(String parentId, String startTime, String endTime, String broker, String industry, String analyst, String stockCode, String stockName) throws Exception {
        List<String> result = roadShowManageService.getRoadShowTimeBySellerAndDate(parentId, startTime, endTime, broker, industry, analyst, stockCode, stockName);
        return new Response(result);
    }
}
