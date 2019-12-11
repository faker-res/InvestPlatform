package la.niub.abcapi.registercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableEurekaServer
public class RegisterCenterApplication extends SpringBootServletInitializer {

	@PostConstruct
	void postConstruct() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RegisterCenterApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(RegisterCenterApplication.class);
		final ApplicationContext applicationContext = springApplication.run(args);
	}

//	public static void main(String[] args) {
//		SpringApplication.run(RegisterCenterApplication.class, args);
//	}
}
