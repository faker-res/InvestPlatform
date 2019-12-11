package la.niub.abcapi.invest.platform.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mail配置信息
 * @author jrxia
 * 1/31/18
 */
@Data
public class MailConfigResponse {

    //主键
    private String id;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

	//用户id
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

    private Boolean sSL;

    /**
     * 过滤条件
     */
    private List<String> fileFilter = new ArrayList<>();

    private List<String> senderBlackList = new ArrayList<>();

    private List<String> subjectBlackList = new ArrayList<>();

    private Integer status;

    private Integer historySyncStatus;
    //是否绑定。true:已绑定，false:已解绑
    private Boolean online;

    private Date lastSyncDate;

    private Date lastHistorySyncDate;

    private String errorMessage;
    //是否删除
    private Boolean deleted;
    //删除时间
    private Date deleteTime;

    public Boolean getsSL() {
        return sSL;
    }

    public void setsSL(Boolean sSL) {
        this.sSL = sSL;
    }


}
