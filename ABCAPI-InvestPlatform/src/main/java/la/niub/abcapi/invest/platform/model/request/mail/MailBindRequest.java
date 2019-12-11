package la.niub.abcapi.invest.platform.model.request.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MailBindRequest {

    private String userId;
    //显示名称
    private String displayName;
    //邮箱
    private String mail;

    private String loginName;

    private String password;
    //收件服务器
    private String host;
    //端口号
    private Integer port;

    private String protocol;

    private Boolean ssl;

    /**
     * 过滤条件
     */
    private List<String> fileFilter = new ArrayList<String>();

    private List<String> senderBlackList = new ArrayList<String>();

    private List<String> subjectBlackList = new ArrayList<String>();
}
