package la.niub.abcapi.invest.seller.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoadShowModel implements Serializable {

    private static final long serialVersionUID = 1435545901986615545L;

    private Long id;

    private String theme;

    private String industryId;

    private String industryName;

    private String companyId;

    private String companyName;

    private String sellerId;

    private String sellerName;

    private String sellerCompanyId;

    private String sellerCompanyName;

    private Date roadShowDate;

    private String activityStartTime;

    private String activityEndTime;

    private String activityDesc;

    private Date createTime;

    private Date updateTime;

    private String userId;

    private String meetingRoom;
    
	/**
	 * 路演类型
	 */
	private String roadType;

    private Byte status;

    private String buyerCompanyId;

    private String buyerCompanyName;
}