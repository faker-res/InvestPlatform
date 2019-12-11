/**
 * 
 */
package la.niub.abcapi.invest.search.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-02-19
 */
@Data
public class ReportVo {
	private String title;
	private String author;
	
	@JSONField(name = "source_type")
	private String sourceType;

	@Override
	public String toString() {
		return "ReportVo [title=" + title + ", author=" + author + ", sourceType=" + sourceType + "]";
	}

}
