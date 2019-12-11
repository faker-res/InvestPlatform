/**
 * 
 */
package la.niub.abcapi.invest.report.component.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * @author zhairp createDate: 2019-03-05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OSSClientTest {
	@Autowired
	private OSSClient ossClient;

	@Test
	public void createFolderTest() {
		String bucketName = "invest-report";
		String objectName = "dev/";
		ObjectMetadata objectMeta = new ObjectMetadata();
		byte[] buffer = new byte[0];
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		objectMeta.setContentLength(0);
		try {
			ossClient.putObject(bucketName, objectName, in, objectMeta);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
