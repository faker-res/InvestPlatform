/**
 * 
 */
package la.niub.abcapi.invest.report.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在字段上使用该注解,并标注对应的索引域, 配合Doc2BeanUtils即可将查询到的Document对象转换成bean
 * 
 * @author zhairp createDate: 2019-02-26
 */
@Documented
@Target({ ElementType.FIELD }) // 作用范围: 字段
@Retention(RetentionPolicy.RUNTIME) // 作用时间: 运行时
public @interface SolrMapping {

	String value() default ""; // solr索引名

}
