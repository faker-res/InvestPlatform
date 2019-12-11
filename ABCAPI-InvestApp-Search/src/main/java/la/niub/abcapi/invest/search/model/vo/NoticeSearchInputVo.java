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
public class NoticeSearchInputVo {
	private String token;
	private String userId;
	private String request_id;
	private Integer offset;
	private Integer limit;
	private String page;
	private String selected;

	@NotBlank(message = "关键字[keyword]不能为空")
	private String keyword;

	private String sort;
	private String input_from;

	@Override
	public String toString() {
		return "NoticeSearchInputVo [token=" + token + ", userId=" + userId + ", request_id=" + request_id + ", offset=" + offset + ", limit=" + limit + ", page=" + page + ", selected=" + selected + ", keyword=" + keyword + ", sort=" + sort + ", input_from=" + input_from + "]";
	}

}
