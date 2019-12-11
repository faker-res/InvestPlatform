package la.niub.abcapi.invest.platform;

import la.niub.abcapi.invest.platform.model.response.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 投研平台
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
//@EnableCaching
@EnableScheduling
@Controller
public class Application {

	@PostConstruct
	void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	Response index() {
		return new Response("Invest Platform Service");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
