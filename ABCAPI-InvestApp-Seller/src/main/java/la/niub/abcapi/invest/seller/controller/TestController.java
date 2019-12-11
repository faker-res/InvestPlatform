package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.component.job.RecommendStockJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用testest
 */
@RestController
@RequestMapping(path = "/test")
public class TestController {

    private final static Logger logger = LogManager.getLogger(TestController.class);

    @Value("${server.port}")
    private Integer serverPort;

    public static void main(String[] args) throws Exception {
        ;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    String hello() {
        return "this is test "+serverPort;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    String test() {
        return "this is test123321!!!!";
    }

    @Autowired
    private RecommendStockJob recommendStockJob;

    @RequestMapping(path = "/handleEmailAttachment", method = RequestMethod.GET)
    void handleEmailAttachment() {
        recommendStockJob.handleEmailAttachment();
    }

    @RequestMapping(path = "/startCalc", method = RequestMethod.GET)
    void startCalc() {
        recommendStockJob.startCalc();
    }

}
