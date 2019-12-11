/**
 * 
 */
package la.niub.abcapi.invest.report;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhairp createDate: 2019-02-21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {

	@Test
	public void helloTest() {
		System.out.println("hello!");
	}

	@Test
	public void calTest() {
		System.out.println(2 << 3);
	}

}
