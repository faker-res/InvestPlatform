/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.memfactory.utils.HttpUtil;

/**
 * @author zhairp createDate: 2019-03-16
 */
public class OSSTest {

	@Test
	public void sendMsgCodeTest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", "15851812964");
		for (int i = 0; i < 2; i++) {
			System.out.println(HttpUtil.doPost("https://passport.abcfintech.com/sso/mobile/sendMsgCode", params));
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
