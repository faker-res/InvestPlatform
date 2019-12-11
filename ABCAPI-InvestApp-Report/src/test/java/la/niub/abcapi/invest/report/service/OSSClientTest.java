/**
 * 
 */
package la.niub.abcapi.invest.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectAcl;

/**
 * 
 * 文件
 * 
 * @author zhairp createDate: 2019-03-06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OSSClientTest {
	@Autowired
	private OSSClient ossClient;

	@Test
	public void getObjectAclTest() {
		ObjectAcl objectAcl = ossClient.getObjectAcl("invest-report", "dev/90c40a87-6d93-493c-8e8e-64ed6e4cf407.pdf");
		System.out.println(objectAcl.getOwner().toString());
		System.out.println(objectAcl.getPermission().toString());
		System.out.println(objectAcl.toString());
	}

}
