package la.niub.abcapi.invest.search;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import la.niub.abcapi.invest.search.model.response.Response;

/**
 * 综合搜索
 * 
 * @author zhairp createDate: 2019-02-16
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
@EnableScheduling
@EnableCaching
@RestController
public class Application {

	@PostConstruct
	void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	Response index() {
		return new Response("Invest Search Service");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
