/**
 * 
 */
package la.niub.abcapi.invest.report.jdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import la.niub.abcapi.invest.report.constant.SourceType;

/**
 * @author zhairp createDate: 2019-03-07
 */
public class ListTest {

	@Test
	public void linkedListTest() {
		List<Integer> list = new LinkedList<Integer>();
		list.add(11);
		list.add(22);
		list.add(33);
		System.out.println(list);
	}

	@Test
	public void arrayListTest() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(11);
		list.add(22);
		list.add(33);
		System.out.println(list);
	}

	@Test
	public void calTest() {
		int oldCapacity = 10;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		System.out.println(newCapacity);
	}

	@Test
	public void mapTest() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId123456", "{userId:123456,name:zs}");
		System.out.println(map.toString());
	}

	@Test
	public void arrayTest() {
		List<String> strs = new ArrayList<String>();
		strs.add("mail");
		strs.add("upload");
		System.out.println(StringUtils.join(strs, " OR "));
	}

	@Test
	public void putTest() {
		Map<String, String> map = new HashMap<String, String>();
		System.out.println(map.put("10001", "John"));
		System.out.println(map.put("10001", "Jackie"));
	}

	@Test
	public void sourceTypeTest() {
		System.out.println(SourceType.upload.toString().equals("upload"));
	}

	@Test
	public void pdfTest() {
		String url = "http://invest-report.oss-cn-hangzhou.aliyuncs.com/dev/mail/190417/1f9e9980-c12c-4e6b-93c3-8c6eed359209.pdf";
		System.out.println(url.substring(url.lastIndexOf(".") + 1));
	}

}
