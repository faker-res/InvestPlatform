
package la.niub.abcapi.invest.report.config.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;

/**
 * 配置类
 * 
 * @author zhairp createDate: 2019-03-08
 */
@Configuration
public class OSSConfiguration {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${oss.extranetHost}")
	private String extranetHost;

	@Value("${oss.intranetHost}")
	private String intranetHost;

	@Value("${oss.accessKeyId}")
	private String accessKeyId;

	@Value("${oss.accessKeySecret}")
	private String accessKeySecret;

	@Value("${spring.profiles.active}")
	private String active;

	/**
	 * 获取OSSClient实例
	 * 
	 * @author zhairp createDate: 2019-03-08
	 * @return
	 */
	@Bean(name = "ossClient")
	public OSSClient getOSSClient() {
		log.info("current active is:{}", active);
		/*if ("local".equals(active) || "dev".equals(active)) {
			// 解决不在同一局域网访问问题
			return new OSSClient(extranetHost, accessKeyId, accessKeySecret);
		} else {
			// 部署在阿里云的机器访问OSS请务必使用内网域名，高速且无流量费用。
			return new OSSClient(intranetHost, accessKeyId, accessKeySecret);
		}*/
		return new OSSClient(extranetHost, accessKeyId, accessKeySecret);
	}

}
