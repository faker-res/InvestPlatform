package la.niub.abcapi.invest.platform.model.request.client;

import la.niub.abcapi.invest.platform.config.enums.MailProtocol;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Mail配置信息
 * @author jrxia
 * 1/31/18
 */
@Data
public class ConfigBindRequest {

	//用户id
    private String userId;
    //显示名称
    private String name;
    //邮箱
    private String mail;

    private String account;

    private String password;
    //收件服务器
    private String host;
    //端口号
    private Integer port;

    private MailProtocol protocol;

    private Boolean ssl;

    /**
     * 过滤条件
     */
    private List<String> fileFilter = new ArrayList<>();

    private List<String> senderBlackList = new ArrayList<>();

    private List<String> subjectBlackList = new ArrayList<>();
}
