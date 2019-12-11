package la.niub.abcapi.invest.seller;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import la.niub.abcapi.invest.seller.model.response.Response;

/**
 * 卖方管理
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
@EnableScheduling
@EnableCaching
@Controller
@EnableAsync
public class Application {

	@PostConstruct
	void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	Response index() {
		return new Response("Invest Seller Service");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
