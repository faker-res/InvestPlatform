package la.niub.abcapi.invest.platform.model.response.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import la.niub.abcapi.invest.platform.config.enums.MailProtocol;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MailConfig {

    //主键
    private Integer id;

	//用户id
    private String userId;

    //邮箱
    private String mail;

    //显示名称
    private String account;

    @JsonIgnore
    private String password;

    //收件服务器
    private String host;

    //端口号
    private Integer port;

    private MailProtocol protocol;

    private Boolean ssl;

    private String remarks;

    private List<String> fileFilter = new ArrayList<>();

    private List<String> senderBlackList = new ArrayList<>();

    private List<String> subjectBlackList = new ArrayList<>();

    private Boolean enable;

    private Boolean notifyEnable;

    private Date syncBeginTime;

    private Date syncEndTime;

    private Integer syncCount;

    private Date createTime;

    private Date updateTime;
}
