/**
 * 
 */
package la.niub.abcapi.invest.search.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-02-20
 */
@Data
public class NoticeDetailInputVo {
	private String userId;
	private String token;
	private String request_id;

	@NotBlank(message = "公告ID[articleId]不能为空")
	private String articleId;
	private String sourceType;

	@Override
	public String toString() {
		return "NoticeDetailInputVo [userId=" + userId + ", token=" + token + ", request_id=" + request_id + ", articleId=" + articleId + ", sourceType=" + sourceType + "]";
	}

}
