/**
 * 
 */
package la.niub.abcapi.invest.report.config.configuration;

import javax.servlet.MultipartConfigElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhairp createDate: 2019-03-12
 */
@Configuration
public class MultipartConfig {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L * 50L);
		return factory.createMultipartConfig();
	}

}
