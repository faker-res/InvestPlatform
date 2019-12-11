package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskBrokerResponse;
import la.niub.abcapi.invest.seller.model.response.TraderMarkTaskListResponse;
import la.niub.abcapi.invest.seller.model.vo.TraderMarkTaskSaveVo;
import la.niub.abcapi.invest.seller.service.ITraderMarkTaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

/**
 * @author : ppan
 * @description : 券商评分
 * @date : 2019-01-26 16:17
 */
@RestController
@RequestMapping("/tradermark/task")
@Validated
public class TraderMarkTaskController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private ITraderMarkTaskService traderMarkTaskService;

    /**
     * 查询评分任务列表
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("list")
    public Response getTaskList(@NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        Map<Integer,List<TraderMarkTaskListResponse>> result = traderMarkTaskService.getTaskList(userId);
        return new Response(result);
    }

    /**
     * 评分任务券商列表
     * @param userId
     * @param taskId
     * @param status  0:未评价  1:已评价  空：全部
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/list")
    public Response getTaskBroker(@NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[taskId]不能为空") Long taskId, Integer status) throws Exception {
        List<TraderMarkTaskBrokerResponse> result = traderMarkTaskService.getTaskBroker(userId, taskId, status);
        return new Response(result);
    }

    /**
     * 获取各行业名称和行业下研究员数量以及已完成行业
     * @param userId
     * @param taskId
     * @param broker
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/industry")
    public Response getIndustryInfo(@NotBlank(message = "参数[userId]不能为空") String userId,
                                    @NotNull(message = "参数[taskId]不能为空") Long taskId,
                                    @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
        Map<String, Object> result = traderMarkTaskService.getIndustryInfo(userId, taskId, broker);
        return new Response(result);
    }

    /**
     * 评分表格详情(主观数据+客观数据)
     * @param userId
     * @param taskId
     * @param broker
     * @param industry
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/detail")
    public Response getTaskBrokerDetail(@NotBlank(message = "参数[userId]不能为空") String userId,
                                        @NotNull(message = "参数[taskId]不能为空") Long taskId,
                                        @NotBlank(message = "参数[broker]不能为空") String broker,
                                        String industry) throws Exception {
        Map<String, Object> result = traderMarkTaskService.getTaskBrokerDetail(userId, taskId, broker, industry);
        return new Response(result);
    }

    /**
     * 保存评分
     * @param saveVo
     * @return
     * @throws Exception
     */
    @PostMapping("/broker/save")
    public Response saveScore(@RequestBody @Valid TraderMarkTaskSaveVo saveVo) throws Exception {
        traderMarkTaskService.saveScore(saveVo);
        return new Response();
    }

    /**
     * 获取下一个未完成评分的券商
     * @param taskId
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/next")
    public Response getNextIncompleteBroker(@NotNull(message = "参数[taskId]不能为空") Long taskId,
                                            @NotBlank(message = "参数[userId]不能为空") String userId) throws Exception {
        return new Response(traderMarkTaskService.getNextIncompleteBroker(taskId, userId));
    }

    /**
     * 券商最近7个月的金股收益率
     * @param userId
     * @param broker
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/goldrate")
    public Response getBrokerGoldRate(@NotBlank(message = "参数[userId]不能为空") String userId,
                                      @NotBlank(message = "参数[broker]不能为空") String broker,
                                      @NotNull(message = "参数[taskId]不能为空") Long taskId) throws Exception {
        Map<String, Object> result = traderMarkTaskService.getBrokerRate(userId, broker, taskId);
        return new Response(result);
    }

    /**
     * 券商最近7个月的综合得分
     * @param userId
     * @param broker
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/scorechart")
    public Response getBrokerTotalScoreChart(@NotBlank(message = "参数[userId]不能为空") String userId,
                                             @NotBlank(message = "参数[broker]不能为空") String broker) throws Exception {
        Map<String, Object> result = traderMarkTaskService.getBrokerTotalScoreChart(userId, broker);
        return new Response(result);
    }

    /**
     * 股票路演数量列表
     * @param taskId
     * @param broker
     * @param analyst
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/roadshow")
    public Response getBrokerRoadShow(@NotBlank(message = "参数[userId]不能为空") String userId,
                                      @NotNull(message = "参数[taskId]不能为空") Long taskId,
                                      @NotBlank(message = "参数[broker]不能为空") String broker,
                                      @NotBlank(message = "参数[industry]不能为空") String industry,
                                      @NotBlank(message = "参数[analyst]不能为空") String analyst) throws Exception {
        List<Map<String, Object>> result = traderMarkTaskService.getBrokerRoadShow(userId, taskId, broker, industry, analyst);
        return new Response(result);
    }

    /**
     * 路演股票行情
     * @param taskId
     * @param stockCode
     * @param stockName
     * @param broker
     * @param analyst
     * @return
     * @throws Exception
     */
    @GetMapping("/broker/stockmarket")
    public Response getStockMarket(@NotBlank(message = "参数[userId]不能为空") String userId,
                                   @NotNull(message = "参数[taskId]不能为空") Long taskId,
                                   @NotBlank(message = "参数[stockCode]不能为空") String stockCode,
                                   @NotBlank(message = "参数[stockName]不能为空") String stockName,
                                   @NotBlank(message = "参数[broker]不能为空") String broker,
                                   @NotBlank(message = "参数[industry]不能为空") String industry,
                                   @NotBlank(message = "参数[analyst]不能为空") String analyst) throws Exception {
        Map<String, Object> result = traderMarkTaskService.getStockMarket(userId, taskId, stockCode, stockName, broker, industry, analyst);
        return new Response(result);
    }

    /**
     * 下载模板
     * @param userId
     * @param taskId
     * @return
     * @throws Exception
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@NotBlank(message = "参数[userId]不能为空") String userId,
                                             @NotNull(message = "参数[taskId]不能为空") Long taskId) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String fileName = "券商评分.xls";

        traderMarkTaskService.download(bos, userId, taskId);

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
     * 上传模板
     * @param file
     * @param userId
     * @param taskId
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public Response upload(MultipartFile file, @NotBlank(message = "参数[userId]不能为空") String userId, @NotNull(message = "参数[taskId]不能为空") Long taskId) throws Exception {
        traderMarkTaskService.upload(file, userId, taskId);
        return new Response();
    }
}
