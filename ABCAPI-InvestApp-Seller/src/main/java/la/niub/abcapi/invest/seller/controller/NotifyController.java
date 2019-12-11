package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.component.client.IInvestReportClient;
import la.niub.abcapi.invest.seller.model.bo.mail.Mail;
import la.niub.abcapi.invest.seller.model.request.client.InvestReportExternalRequest;
import la.niub.abcapi.invest.seller.model.response.ErrorResponse;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.IRecommendedStockService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接收通知
 */
@RestController
@RequestMapping(path = "/notify")
public class NotifyController {

    private final static Logger logger = LogManager.getLogger(NotifyController.class);

//    @Autowired
//    private HttpServletResponse httpServletResponse;

    @Autowired
    private IInvestReportClient investReportClient;

    @Autowired
    private IRecommendedStockService recommendedStockService;

    /**
     * 外部研报
     * @return
     */
//    @PostMapping(path = "/report-external")
//    Response reportExternal(@RequestBody Mail param) {
//        //@todo 重复推送检验
//        logger.info("NotifyController.reportExternal 收到新邮件推送:{}",param);
//        try{
////            httpServletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
//            if (StringUtils.isEmpty(param.getUserId())){
//                return new ErrorResponse("userId是空");
//            }
//            Map<String,String> attachmentsMap = param.getAttachments();
//            if (ObjectUtils.isEmpty(attachmentsMap)){
//                logger.info("收到，邮件没有附件");
//                return new Response("收到，邮件没有附件");
//            }
//
//            List<InvestReportExternalRequest> requestList = new ArrayList<>();
//            for (String attachment : attachmentsMap.values()){
//                InvestReportExternalRequest request = new InvestReportExternalRequest();
//                request.setUserId(param.getUserId());
//                request.setFileUrl(attachment);
//                requestList.add(request);
//            }
//            logger.info("push InvestReportExternalRequest: "+requestList);
//            Response result = investReportClient.reportBatchExternal(requestList);
//            if (!result.getCode().equals(200)){
//                return new ErrorResponse("推送失败:"+requestList+" result:"+result);
//            }
//            return new Response((Object)requestList.size(),"推送成功");
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//            return new ErrorResponse(e.getMessage());
//        }
//    }

    /**
     * 金股
     * @return
     */
    @PostMapping(path = "/gold-stock")
    Response goldStock(@RequestBody Mail param) {
        //@todo 重复推送检验
        logger.info("NotifyController.goldStock 收到新邮件推送:{}",param);
        try{
            if (StringUtils.isEmpty(param.getUserId())){
                return new ErrorResponse("userId是空");
            }
            Map<String,String> attachmentsMap = param.getAttachments();
            if (ObjectUtils.isEmpty(attachmentsMap)){
                logger.info("收到，邮件没有附件");
                return new Response("收到，邮件没有附件");
            }
            List<String> attachments = attachmentsMap.values().stream()
                    .filter(b-> StringUtils.isNotEmpty(b)).collect(Collectors.toList());
            if (attachments.isEmpty()){
                logger.info("收到，邮件附件为空");
                return new Response("收到，邮件附件为空");
            }

            //处理邮件
            Integer result = recommendedStockService.handleMail(param);

            return new Response((Object) result,"推送成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
}
