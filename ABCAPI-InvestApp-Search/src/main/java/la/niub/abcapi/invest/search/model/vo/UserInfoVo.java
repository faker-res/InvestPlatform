/**
 * 
 */
package la.niub.abcapi.invest.search.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author zhairp createDate: 2018-12-03
 */
@Data
public class UserInfoVo {
	private String userId;

	@JSONField(name = "xingming")
	private String username;

	private String parentId;

	@JSONField(name = "instName")
	private String parentName;

}
