/**
 * 
 */
package la.niub.abcapi.invest.report.component.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;

import la.niub.abcapi.invest.report.config.configuration.OSSConfiguration;

/**
 * @author zhairp createDate: 2019-02-26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OSSConfigurationTest {
	@Autowired
	private OSSConfiguration oSSConfiguration;

	private String bucketName = "bucket-test-md";
	private String objectName = "bucket-test-md_file.txt";

	@Test
	public void getOSSClientTest() {
		System.out.println("****************" + oSSConfiguration.getOSSClient());
	}

	@Test
	public void createBucketTest() {
		OSSClient ossClient = oSSConfiguration.getOSSClient();
		Bucket bucket = ossClient.createBucket(bucketName);
		System.out.println(bucket.toString());
		ossClient.shutdown();
	}

	@Test
	public void putObjectTest() {
		OSSClient ossClient = oSSConfiguration.getOSSClient();
		String content = "Hello OSS!!!";
		PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
		System.out.println(putObjectResult.toString());
		ossClient.shutdown();
	}

	@Test
	public void listObjectsTest() {
		OSSClient ossClient = oSSConfiguration.getOSSClient();
		// ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
		ObjectListing objectListing = ossClient.listObjects(bucketName);
		// objectListing.getObjectSummaries获取所有文件的描述信息。
		for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
		}
		// 关闭OSSClient。
		ossClient.shutdown();
	}

	@Test
	public void getObject() throws IOException {
		OSSClient ossClient = oSSConfiguration.getOSSClient();
		// 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
		OSSObject ossObject = ossClient.getObject(bucketName, objectName);
		// 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
		InputStream content = ossObject.getObjectContent();
		if (content != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				System.out.println("\n" + line);
			}
			// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
			content.close();
		}
		// 关闭OSSClient。
		ossClient.shutdown();
	}

	@Test
	public void generatePresignedUrlTest() {
		OSSClient ossClient = oSSConfiguration.getOSSClient();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 100);
		Date expiration = calendar.getTime();
		URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
		System.out.println(url.toString());
	}

}
