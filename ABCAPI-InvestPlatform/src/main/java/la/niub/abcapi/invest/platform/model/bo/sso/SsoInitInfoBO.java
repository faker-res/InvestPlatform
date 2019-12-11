package la.niub.abcapi.invest.platform.model.bo.sso;

import la.niub.abcapi.invest.platform.config.enums.GenderEnum;
import lombok.Data;

@Data
public class SsoInitInfoBO {

    private String name;

    //0表示其他，1男，2女
    private String gender = GenderEnum.MALE.getGender();

    private String email;

    //用户名片路径
    private String businessCard;

    //部门
    private String department = "";

    //企业名称
    private String institution = "";

    //职位
    private String position = "";

    //邀请码
    private String code = "";
}