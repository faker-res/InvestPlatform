/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhairp createDate: 2019-03-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskTest {
	@Autowired
	private ReportService reportService;

	@Test
	public void empty() {
		reportService.emptyMongo();
		reportService.emptySolr();
	}

}
