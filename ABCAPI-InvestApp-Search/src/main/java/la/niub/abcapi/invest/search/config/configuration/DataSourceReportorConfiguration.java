package la.niub.abcapi.invest.search.config.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceReportorConfiguration.PACKAGE, sqlSessionFactoryRef = "reportorSqlSessionFactory")
public class DataSourceReportorConfiguration {

	// 精确到 reportor 目录，以便跟其他数据源隔离
	static final String PACKAGE = "la.niub.abcapi.invest.search.dao.reportor";
	static final String MAPPER_LOCATION = "classpath:mapper/reportor/*.xml";

	@Value("${spring.datasource.reportor.url}")
	private String url;

	@Value("${spring.datasource.reportor.username}")
	private String user;

	@Value("${spring.datasource.reportor.password}")
	private String password;

	@Value("${spring.datasource.reportor.driverClassName}")
	private String driverClass;

	@Value("${spring.datasource.reportor.timeoutSeconds}")
	private Integer timeout;

	@Bean(name = "reportorDataSource")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setLoginTimeout(timeout);
		dataSource.setQueryTimeout(timeout);
		return dataSource;
	}

	@Bean(name = "reportorTransactionManager")
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(name = "reportorSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("reportorDataSource") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		// mybatis返回resultType="map"时,如果数据为空的字段,则该字段省略不显示.
		// 表示设置结果为Null也返回相应的字段名称
		sessionFactory.getObject().getConfiguration().setCallSettersOnNulls(true);
		sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return sessionFactory.getObject();
	}
}