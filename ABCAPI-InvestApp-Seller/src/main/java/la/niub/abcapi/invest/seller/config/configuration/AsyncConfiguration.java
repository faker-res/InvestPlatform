package la.niub.abcapi.invest.seller.config.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author : ppan
 * @description : 多线程配置
 * @date : 2019-03-06 09:43
 */
@Configuration
public class AsyncConfiguration {

    @Value("${async.corePoolSize}")
    private Integer corePoolSize ;

    @Value("${async.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${async.queueCapacity}")
    private Integer queueCapacity;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}
