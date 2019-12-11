package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.component.client.fallback.ApiBestNewsFallbackFactory;
import la.niub.abcapi.invest.platform.config.configuration.FeignObjectConfiguration;
import la.niub.abcapi.invest.platform.model.request.client.ClientBestNewsRequest;
import la.niub.abcapi.invest.platform.model.response.client.ApiBestNewsResponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 研报solr服务
 */
@CacheConfig(cacheNames = "IApiBestNewsClient")
@FeignClient(name = "${feign.client.apiBestNews.name}", url = "${feign.client.apiBestNews.url}"
        , fallbackFactory = ApiBestNewsFallbackFactory.class
        , configuration = FeignObjectConfiguration.class
)
public interface IApiBestNewsClient {

//    @Cacheable
    @RequestMapping(value = "", method = RequestMethod.GET)
    ApiBestNewsResponse bestNews(@PathVariable(value = "default") ClientBestNewsRequest request);
}
