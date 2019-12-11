package la.niub.abcapi.invest.report.controller;

import la.niub.abcapi.invest.report.constant.ReportStatusConstant;
import la.niub.abcapi.invest.report.constant.SourceType;
import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.bo.Mail;
import la.niub.abcapi.invest.report.model.response.ErrorResponse;
import la.niub.abcapi.invest.report.model.response.Response;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 接收通知
 */
@RestController
@RequestMapping(path = "/notify")
public class NotifyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResearchTaskDomainMapper researchTaskDomainMapper;

    /**
     * 邮件透视
     * @return
     */
    @PostMapping(path = "/email")
    Response reportExternal(@RequestBody Mail param) {
        logger.info("NotifyController.reportExternal 收到新邮件推送:{}",param);
        try{
            if (StringUtils.isEmpty(param.getUserId())){
                return new ErrorResponse("userId是空");
            }
            Map<String,String> attachmentsMap = param.getAttachments();
            if (ObjectUtils.isEmpty(attachmentsMap)){
                logger.info("收到，邮件没有附件");
                return new Response("收到，邮件没有附件");
            }

            for (String attachment : attachmentsMap.values()){
                ResearchTaskDomain record = new ResearchTaskDomain();
                record.setUserId(param.getUserId());
                record.setFileUrl(attachment);
                record.setSourceType(SourceType.mail.name());
                record.setStatus(ReportStatusConstant.COLLECT_INIT);
                logger.info("Save External Report: "+record);
                researchTaskDomainMapper.insertSelective(record);
            }
            return new Response((Object)attachmentsMap.size(),"外部研报接收成功!");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse("接收失败:"+e.getMessage());
        }
    }
}
