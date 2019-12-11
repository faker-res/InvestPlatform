/**
 * 
 */
package la.niub.abcapi.invest.search.component.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import la.niub.abcapi.invest.search.model.response.CompanyResponse;
import la.niub.abcapi.invest.search.model.response.Response;

/**
 * @author zhairp createDate: 2019-02-18
 */
@FeignClient(name = "${feign.client.platform.name}", url = "${feign.client.platform.url}", fallback = UcServiceClient.DefaultFallback.class)
public interface UcServiceClient {

	@GetMapping(value = "/account/company")
	Response<CompanyResponse> getCompanyInfo(@RequestParam("userId") String userId);

	@Component
	class DefaultFallback implements UcServiceClient {
		private Logger log = LoggerFactory.getLogger(getClass());

		@Override
		public Response<CompanyResponse> getCompanyInfo(String userId) {
			log.info("getCompanyInfo userId:{}", userId);
			return new Response("fallback invoked!");
		}
	}

}
