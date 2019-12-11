/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import la.niub.abcapi.invest.report.model.vo.InvestDocInput;

/**
 * @author zhairp createDate: 2019-03-07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTemplateTest {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void findByIdTest() {
		InvestDocInput doc1 = mongoTemplate.findById(97, InvestDocInput.class, "invest_files");
		System.out.println(doc1);
	}

	@Test
	public void emptyMongoTest() {
		Query query = new Query();
		query.limit(100);
		mongoTemplate.remove(query, "invest_files");
		mongoTemplate.remove(query, "invest_status");
		mongoTemplate.remove(query, "invest_tables");
		mongoTemplate.remove(query, "invest_charts");
		mongoTemplate.remove(query, "invest_text");
	}

	@Test
	public void addTest() {
		InvestDocInput doc = new InvestDocInput();
		doc.setId(1000L);
		mongoTemplate.insert(doc, "invest_status");
	}

}
