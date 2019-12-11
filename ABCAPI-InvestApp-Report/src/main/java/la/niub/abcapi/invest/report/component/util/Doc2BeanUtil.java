/**
 * 
 */
package la.niub.abcapi.invest.report.component.util;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import la.niub.abcapi.invest.report.component.annotation.SolrMapping;

/**
 * 文档转化成JavaBean的工具类
 * 
 * @author zhairp createDate: 2019-02-26
 */
public final class Doc2BeanUtil {
	private static final Logger log = LoggerFactory.getLogger(Doc2BeanUtil.class);

	public static <T> T getBean(Class<T> beanClass, SolrDocument document) {
		try {
			// 获取实例对象
			Object bean = beanClass.newInstance();
			// 反射获得所有字段
			Field[] fields = beanClass.getDeclaredFields();
			// 遍历bean中的字段
			for (Field field : fields) {
				SolrMapping anno = field.getAnnotation(SolrMapping.class);
				if (anno != null) {
					String filedName = field.getName();
					// 获得注解中标记的对应的索引域
					String indexName = anno.value();
					Object val = document.get(indexName);
					// 给实例属性赋值
					BeanUtils.setProperty(bean, filedName, val);
				}
			}
			return (T) bean;
		} catch (Exception e) {
			log.error("文档转化成JavaBean异常:{}", e.getMessage());
			return null;
		}
	}
}
