/**
 * 
 */
package la.niub.abcapi.invest.search.component.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhairp createDate: 2019-02-18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UcServiceApiTest {

	@Autowired
	private UcServiceApi ucServiceApi;

	@Test
	public void getUserInfoTest() {
		System.out.println(ucServiceApi.getUserInfo("80115216313098917"));
	}

}
