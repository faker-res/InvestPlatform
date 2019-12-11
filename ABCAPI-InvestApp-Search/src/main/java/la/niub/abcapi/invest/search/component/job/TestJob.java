/**
 * 
 */
package la.niub.abcapi.invest.search.component.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import la.niub.abcapi.invest.search.service.TestService;

/**
 * 测试定时任务
 * 
 * @author zhairp createDate: 2019-02-16
 */
@Component
public class TestJob {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TestService testService;

	/**
	 * 定时刷新缓存
	 * 
	 * @author zhairp createDate: 2019-02-16
	 */
	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 1000 * 3)
	public void flushAnalystsCache() {
		testService.flushAnalystsCache();
		log.info("###################重新加载成功###################");
	}

}
