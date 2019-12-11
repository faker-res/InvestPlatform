/**
 * 
 */
package la.niub.abcapi.invest.seller.component.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import la.niub.abcapi.invest.seller.service.ITraderMarkCountService;

/**
 * 券商统计定时任务
 * 
 * @author zhairp createDate: 2019-02-15
 */
@Component
public class TraderMarkCountJob {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ITraderMarkCountService traderMarkCountService;

	/**
	 * 定时刷新缓存
	 * 
	 * @author zhairp createDate: 2019-02-15
	 */
	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 1000 * 3)
	@Async
	public void reload() {
		traderMarkCountService.reload();
		log.info("###################重新加载成功###################");
	}

}
