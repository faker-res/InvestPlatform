package la.niub.abcapi.invest.seller.component.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import la.niub.abcapi.invest.seller.model.response.Response;

/**
 *
 * @author Jenkin.K
 * @date 2017/12/21
 */
@RestController
public class HealthCheckController {
	private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/healthCheck")
    @ResponseBody
    public Object healthCheck(){
    	log.info("##############healthCheck invoked!");
        return new Response<>("系统健康");
    }

}
