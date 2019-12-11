package la.niub.abcapi.invest.report.config.configuration;

import com.netflix.hystrix.HystrixCommand;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import la.niub.abcapi.invest.report.component.client.coder.ObjectEncoder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
public class FeignObjectConfiguration {

    @Bean
    public Encoder encoder(){
        return new ObjectEncoder();
    }

}
