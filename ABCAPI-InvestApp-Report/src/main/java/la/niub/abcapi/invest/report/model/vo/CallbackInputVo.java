/**
 * 
 */
package la.niub.abcapi.invest.report.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-03-12
 */
@Data
public class CallbackInputVo {

	@JSONField(name = "fileId")
	private Long sourceId;

	@JSONField(name = "process_error")
	private String processError;

	/**
	 * @author zhairp createDate: 2019-03-12
	 * @return
	 */
	@Override
	public String toString() {
		return "CallbackInputVo [sourceId=" + sourceId + ", processError=" + processError + "]";
	}

}
