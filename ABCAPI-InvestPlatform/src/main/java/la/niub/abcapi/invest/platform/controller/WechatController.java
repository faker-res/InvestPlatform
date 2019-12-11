package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IWechatReadingClient;
import la.niub.abcapi.invest.platform.model.request.wechat.WechatGroupsRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingWechatBindedResponse;
import la.niub.abcapi.invest.platform.model.response.client.modeling.ModelingWechatGroupResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信配置
 */
@RestController
@RequestMapping(path = "/wechat")
public class WechatController {

    private final static Logger logger = LogManager.getLogger(WechatController.class);

    @Autowired
    private IWechatReadingClient wechatReadingClient;

    /**
     * 获取已绑定的微信
     * @param userId
     * @return
     */
    @GetMapping("list")
    public Response list(@RequestParam("userId") String userId) {
        logger.info("wechat/list param:"+userId);
        try{
            ModelingResponse<ModelingWechatBindedResponse> result = wechatReadingClient.binded(userId);
            if (result != null && result.getData() != null){
                return new Response(result.getData().getWechats());
            }else{
                return new Response();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 获取所有微信群
     * @param param
     * @return
     */
    @GetMapping("groups")
    public Response groups(WechatGroupsRequest param) {
        logger.info("wechat/groups param:"+param);
        try{
            ModelingResponse<ModelingWechatGroupResponse> result = wechatReadingClient.groups(param);
            if (result != null && result.getData() != null){
                return new Response(result.getData().getGroups());
            }else{
                return new Response();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

}
