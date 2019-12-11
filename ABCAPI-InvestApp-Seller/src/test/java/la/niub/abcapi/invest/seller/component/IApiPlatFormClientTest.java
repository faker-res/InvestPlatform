/**
 * 
 */
package la.niub.abcapi.invest.seller.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import la.niub.abcapi.invest.seller.component.client.IApiPlatFormClient;

/**
 * @author zhairp createDate: 2019-02-18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IApiPlatFormClientTest {
	@Autowired
	private IApiPlatFormClient iApiPlatFormClient;

	@Test
	public void brokerTest() {
		System.out.println("********************" + iApiPlatFormClient.broker());
	}

}
