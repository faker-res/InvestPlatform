package la.niub.abcapi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableZuulProxy
@EnableHystrix
public class GatewayApplication {

	@PostConstruct
	void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
