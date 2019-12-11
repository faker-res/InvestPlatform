package la.niub.abcapi.invest.platform.component.health;

import la.niub.abcapi.invest.platform.model.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jenkin.K
 * @date 2017/12/21
 */
@RestController
public class HealthCheckController {

    @GetMapping(value = "/healthCheck")
    @ResponseBody
    public Object healthCheck(){
        return new Response<>();
    }

}
