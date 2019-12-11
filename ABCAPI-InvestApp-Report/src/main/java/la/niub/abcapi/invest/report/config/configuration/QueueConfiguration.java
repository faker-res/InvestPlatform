package la.niub.abcapi.invest.report.config.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import la.niub.abcapi.invest.report.dao.invest.ResearchTaskDomainMapper;
import la.niub.abcapi.invest.report.model.vo.ResearchTaskDomain;

/**
 * 消息队列配置类
 * 
 * @author zhairp createDate: 2019-03-23
 */
//@Configuration
public class QueueConfiguration {
	private Logger log = LoggerFactory.getLogger(getClass());

//	@Value("${spring.rabbitmq.queue}")
	private String queueName;

//	@Autowired
	private ResearchTaskDomainMapper researchTaskDomainMapper;

	/**
	 * 配置队列-Direct模式
	 * 
	 * @author zhairp createDate: 2019-03-23
	 * @return
	 */
//	@Bean
//	public Queue queue() {
//		return new Queue(queueName);
//	}

	/**
	 * 接收研报消息队列消息
	 * 
	 * @author zhairp createDate: 2019-03-23
	 * @param msg
	 */
//	@RabbitListener(queues = "${spring.rabbitmq.queue}") // 监听器监听指定的Queue
	public void process(String msg) {
		log.info("Receive queueName:{},msg:{}", queueName, msg);
		ResearchTaskDomain record = JSON.parseObject(msg, ResearchTaskDomain.class);
		log.info("Receive queueName:{},record:{}", queueName, record);
		if (null != record && !StringUtils.isEmpty(record.getUserId()) && !StringUtils.isEmpty(record.getFileUrl()) && !StringUtils.isEmpty(record.getSourceType())) {
			record.setStatus(2);
			researchTaskDomainMapper.insertSelective(record);
		}
	}

}
