package la.niub.abcapi.invest.platform.component.client;

import la.niub.abcapi.invest.platform.model.bo.suggestion.EntityBO;
import la.niub.abcapi.invest.platform.model.response.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * analyst服务
 */
//@CacheConfig(cacheNames = "IApiBestNewsClient")
@FeignClient(name = "ISuggestionClient", url = "${feign.client.analyst.url}"
//        , fallbackFactory = ApiBestNewsFallbackFactory.class
//        , configuration = FeignObjectConfiguration.class
)
public interface ISuggestionClient {

//    @Cacheable
    @GetMapping(value = "/api/suggestion/recommend/entity")
    Response<List<EntityBO>> suggestionEntity(@RequestParam("keyword") String keyword, @RequestParam("category") String category,
                                              @RequestParam("userId") String userId, @RequestParam("token") String token);
}
