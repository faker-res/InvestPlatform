/**
 * 
 */
package la.niub.abcapi.invest.search.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhairp createDate: 2019-02-19
 */
@Data
public class NewsDetailInputVo {
	private String userId;
	private String token;
	private String request_id;

	@NotBlank(message = "资讯ID[articleUrl]不能为空")
	private String articleUrl;
	private String page;

	@Override
	public String toString() {
		return "NewsDetailInputVo [userId=" + userId + ", token=" + token + ", request_id=" + request_id + ", articleUrl=" + articleUrl + ", page=" + page + "]";
	}

}
