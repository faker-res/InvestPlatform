/**
 * 
 */
package la.niub.abcapi.invest.report.component.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhairp createDate: 2019-04-09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PlatformServiceTest {
	@Autowired
	private PlatformService platformService;

	@Test
	public void getCompanyTest() {
		System.out.println(platformService.getCompany("80115010136862277"));
		System.out.println(platformService.getCompany("801150101368622771"));
	}

}
