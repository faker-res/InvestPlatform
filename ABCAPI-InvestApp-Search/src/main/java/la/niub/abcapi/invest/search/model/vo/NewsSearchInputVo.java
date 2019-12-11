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
public class NewsSearchInputVo {
	private String userId;
	private String token;
	private String request_id;

	@NotBlank(message = "关键字[keyword]不能为空")
	private String keyword;
	private Integer limit;
	private Integer offset;
	private String prior;
	private String channel;
	private String page;
	private String input_from;
	private String selected;

	@Override
	public String toString() {
		return "NewsSearchInputVo [userId=" + userId + ", token=" + token + ", request_id=" + request_id + ", keyword=" + keyword + ", limit=" + limit + ", offset=" + offset + ", prior=" + prior + ", channel=" + channel + ", page=" + page + ", input_from=" + input_from + ", selected=" + selected + "]";
	}

}
