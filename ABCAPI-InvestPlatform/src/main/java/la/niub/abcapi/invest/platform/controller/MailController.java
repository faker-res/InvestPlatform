package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestDataEmailClient;
import la.niub.abcapi.invest.platform.config.enums.MailProtocol;
import la.niub.abcapi.invest.platform.model.response.MailConfigResponse;
import la.niub.abcapi.invest.platform.model.request.client.ConfigBindRequest;
import la.niub.abcapi.invest.platform.model.request.client.ConfigUpdateRequest;
import la.niub.abcapi.invest.platform.model.request.mail.MailBindRequest;
import la.niub.abcapi.invest.platform.model.request.mail.MailDeleteRequest;
import la.niub.abcapi.invest.platform.model.request.mail.MailDisableRequest;
import la.niub.abcapi.invest.platform.model.request.mail.MailEnableRequest;
import la.niub.abcapi.invest.platform.model.request.mail.MailUpdateRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.MailConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮件配置
 */
@RestController
@RequestMapping("mail")
public class MailController {

    private static final Logger logger = LogManager.getLogger(MailController.class);

    @Autowired
    private IInvestDataEmailClient investDataEmailClient;

    @PostMapping("bind")
    public Response bindMail(@RequestBody MailBindRequest param) {
        logger.info("mail/bind param:"+param);
        try {
            if (StringUtils.isEmpty(param.getMail())
                    || StringUtils.isEmpty(param.getPassword())
                    || StringUtils.isEmpty(param.getHost())
                    || param.getPort() == null
                    || StringUtils.isEmpty(param.getProtocol())){
                return new ErrorResponse("缺少必要配置项:mail、password、host、port、protocal");
            }
            MailProtocol protocol = null;
            try{
                protocol = MailProtocol.valueOf(param.getProtocol().toUpperCase());
            }catch (Exception e){
                return new ErrorResponse("不正确的协议类型，请选用imap或exchange或pop3");
            }
            ConfigBindRequest request = new ConfigBindRequest();
            request.setUserId(param.getUserId());
            request.setName(param.getDisplayName());
            request.setMail(param.getMail());
            request.setAccount(param.getLoginName());
            request.setPassword(param.getPassword());
            request.setHost(param.getHost());
            request.setPort(param.getPort());
            request.setProtocol(protocol);
            request.setSsl(param.getSsl());
            request.setFileFilter(param.getFileFilter());
            request.setSenderBlackList(param.getSenderBlackList());
            request.setSubjectBlackList(param.getSubjectBlackList());
            Response response = investDataEmailClient.configBind(request);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @PostMapping("enable")
    public Response enableMail(@RequestBody MailEnableRequest param) {
        logger.info("mail/enable param:"+param);
        try{
            Response response = investDataEmailClient.configEnable(Integer.valueOf(param.getId()));
            return response;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @PostMapping("disable")
    public Response disableMail(@RequestBody MailDisableRequest param) {
        logger.info("mail/disable param:"+param);
        try{
            Response response = investDataEmailClient.configDisable(Integer.valueOf(param.getId()));
            return response;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 删除绑定的邮件
     *
     * @param param
     * @return
     */
    @PostMapping("delete")
    public Response deleteEmail(@RequestBody MailDeleteRequest param) {
        logger.info("mail/delete param:"+param);
        try{
            Response response = investDataEmailClient.configUnbind(Integer.valueOf(param.getId()));
            return response;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @GetMapping("list")
    public Response list(@RequestParam("userId") String userId) {
        logger.info("mail/list param:"+userId);
        try{
            Response<List<MailConfig>> response = investDataEmailClient.configList(userId);
            if (!response.getCode().equals(200) || response.getData() == null){
                return response;
            }
            List<MailConfig> mailConfigList = response.getData();
            List<MailConfigResponse> mailConfigResponseList = new ArrayList<>();
            for (MailConfig item : mailConfigList){
                MailConfigResponse mailConfigResponse = new MailConfigResponse();
                mailConfigResponse.setId(String.valueOf(item.getId()));
                mailConfigResponse.setUserId(item.getUserId());
                mailConfigResponse.setDisplayName(item.getRemarks());
                mailConfigResponse.setMail(item.getMail());
                mailConfigResponse.setLoginName(item.getAccount());
//                mailConfigResponse.setPassword(item.getPassword());
                mailConfigResponse.setHost(item.getHost());
                mailConfigResponse.setPort(item.getPort());
                mailConfigResponse.setProtocol(item.getProtocol().name().toLowerCase());
                mailConfigResponse.setsSL(item.getSsl());
                mailConfigResponse.setFileFilter(item.getFileFilter());
                mailConfigResponse.setSenderBlackList(item.getSenderBlackList());
                mailConfigResponse.setSubjectBlackList(item.getSubjectBlackList());
                mailConfigResponse.setOnline(item.getEnable());
                mailConfigResponse.setLastSyncDate(item.getSyncEndTime());
                mailConfigResponse.setCreateTime(item.getCreateTime());
                mailConfigResponse.setUpdateTime(item.getUpdateTime());
                mailConfigResponseList.add(mailConfigResponse);
            }
            return new Response(mailConfigResponseList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @PostMapping("update")
    public Response updateConfig(@RequestBody MailUpdateRequest param) {
        logger.info("mail/update param:"+param);
        try {
            ConfigUpdateRequest request = new ConfigUpdateRequest();
            request.setId(Integer.valueOf(param.getId()));
            request.setUserId(param.getUserId());
            request.setName(param.getDisplayName());
            request.setFileFilter(param.getFileFilter());
            request.setSenderBlackList(param.getSenderBlackList());
            request.setSubjectBlackList(param.getSubjectBlackList());
            Response response = investDataEmailClient.configUpdate(request);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

}
