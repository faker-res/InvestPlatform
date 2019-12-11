package la.niub.abcapi.invest.search.config.configuration;

import com.netflix.hystrix.HystrixCommand;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import la.niub.abcapi.invest.search.component.client.coder.ObjectFormDataEncoder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
public class FeignObjectFormDataConfiguration {

    @Bean
    public Encoder encoder(){
        return new ObjectFormDataEncoder();
    }

}
