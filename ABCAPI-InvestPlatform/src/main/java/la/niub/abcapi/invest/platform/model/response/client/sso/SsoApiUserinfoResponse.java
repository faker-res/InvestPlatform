package la.niub.abcapi.invest.platform.model.response.client.sso;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class SsoApiUserinfoResponse {

    private String userId;

    private String userName;

    private String email;

    private String mobile;

    private Integer gender;

    private String instName;

    private String department;

    private String position;

    private Long passTime;

    private Integer status;

    private Integer terminal;

    private Long createTime;

    private String headImg;

    private String code;

    private String businessCard;
}
