package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.component.client.ISuggestionClient;
import la.niub.abcapi.invest.platform.model.bo.sso.UserTokenBO;
import la.niub.abcapi.invest.platform.model.bo.suggestion.EntityBO;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.ISsoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 输入建议
 */
@RestController
@RequestMapping(path = "/suggestion")
public class SuggestionController {

    private final static Logger logger = LogManager.getLogger(SuggestionController.class);

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private ISuggestionClient suggestionClient;

    @Autowired
    private IInvestCloudClient investCloudClient;

    @GetMapping(path = "/entity")
    Response entity(@RequestParam("keyword") String keyword, @RequestParam("category") String category) {
        try{
            UserTokenBO userTokenBO = ssoService.getUserToken();
            if (userTokenBO == null){
                return new ErrorResponse("获取用户token出错");
            }
            Response<List<EntityBO>> result = suggestionClient.suggestionEntity(keyword,category,userTokenBO.getUserId(),userTokenBO.getToken());
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }


    @GetMapping(value = "/mystock")
    public Response searchStockGroup(@RequestParam("keyword") String keyword, @RequestParam("marketCode") String marketCode, @RequestParam("limit") Integer limit) {
        try{
            return investCloudClient.searchStockGroup(keyword, marketCode, limit);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    @GetMapping(value = "/mystock/v2")
    public Response searchStockV2(@RequestParam("keyword") String keyword, @RequestParam("category") String category, @RequestParam("limit") Integer limit) {
        try{
            return  investCloudClient.searchStockV2(keyword, category, limit);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

}
