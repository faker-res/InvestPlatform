/**
 * 
 */
package la.niub.abcapi.invest.search.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import la.niub.abcapi.invest.search.dao.invest.BrokerAnalystMapper;
import la.niub.abcapi.invest.search.service.TestService;

/**
 * @author zhairp createDate: 2019-02-16
 */
@Component
public class TestServiceImpl implements TestService {
	private static final String ANALYST_CACHE = "analystsCache";

	@Autowired
	private BrokerAnalystMapper brokerAnalystMapper;

	@Cacheable(cacheNames = ANALYST_CACHE)
	@Override
	public List<Map<String, Object>> getAllAnalysts() {
		return brokerAnalystMapper.getAllAnalysts();
	}

	@CacheEvict(cacheNames = ANALYST_CACHE)
	@Override
	public void flushAnalystsCache() {
	}

}
