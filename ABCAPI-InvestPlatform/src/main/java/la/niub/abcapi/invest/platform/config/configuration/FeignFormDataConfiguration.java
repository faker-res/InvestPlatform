package la.niub.abcapi.invest.platform.config.configuration;

import com.netflix.hystrix.HystrixCommand;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import la.niub.abcapi.invest.platform.component.client.coder.FormDataEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
public class FeignFormDataConfiguration {

    @Bean
    public Encoder encoder(){
        return new FormDataEncoder();
    }

}
