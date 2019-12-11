package la.niub.abcapi.invest.seller.model.vo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class RoadShowAddVo {
	private static String DEFAULT_THEME = "NoTheme";
	private Long id;

//	@NotBlank(message = "主题不能为空")
	private String theme = DEFAULT_THEME;

	private String industryId;

	@NotBlank(message = "行业不能为空")
	private String industryName;

	private String companyId;

//	@NotBlank(message = "公司不能为空")
	private String companyName;
	
//	@NotEmpty(message = "路演公司不能为空")
//	@Valid
	private List<RoadShowCompanyVo> companys;

	//@NotBlank(message = "卖方研究员id为空")
	private String sellerId = "";

	//@NotBlank(message = "卖方研究员name不能为空")
	private String sellerName;

//	@NotEmpty(message = "路演卖方研究员不能为空")
//	@Valid
	private List<RoadShowSellerVo> sellers;

	@NotBlank(message = "卖方研究员所在公司id不能为空")
	private String sellerCompanyId;

	@NotBlank(message = "'卖方研究员所在公司name不能为空")
	private String sellerCompanyName;

	@NotBlank(message = "路演日期不能为空")
	@Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "路演日期格式应为yyyy-MM-dd")
	private String roadShowDate;

	@NotBlank(message = "活动开始时间不能为空")
	@Pattern(regexp = "([0-1]{1}[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$", message = "活动开始时间格式为HH:mm:ss")
	private String activityStartTime;

	@NotBlank(message = "活动结束时间不能为空")
	@Pattern(regexp = "([0-1]{1}[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$", message = "活动结束时间格式为HH:mm:ss")
	private String activityEndTime;

	private String activityDesc;

	@NotEmpty(message = "买方研究员不能为空")
	@Valid
	private List<RoadShowBuyerVo> buyers;

	@NotBlank(message = "买方研究员所在公司id不能为空")
	private String buyerCompanyId;

	@NotBlank(message = "买方研究员所在公司name不能为空")
	private String buyerCompanyName;

	/**
	 * 基金公司集合只是为了展示,新增不需要此参数
	 */
	private List<RoadShowBuyerCompanyVo> buyerCompanyList;

	/**
	 * 前端展示,会议室
	 */
	private String meetingRoom;

	/**
	 * 前端展示,是否已添加会议室
	 */
	private Boolean existMeetingRoom;

	/**
	 * 前端展示,是否加入行程,基金公司详需要
	 */
	private Boolean myItinerary;
	
	/**
	 * 路演创建者
	 */
	private String userId;
	
	/**
	 * 路演类型
	 */
	private String roadType;

	/**
	 * 状态
	 */
	private Byte status;
}
