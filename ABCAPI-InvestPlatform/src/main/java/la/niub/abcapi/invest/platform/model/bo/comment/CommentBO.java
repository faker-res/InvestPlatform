package la.niub.abcapi.invest.platform.model.bo.comment;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import la.niub.abcapi.invest.platform.model.bo.account.AccountBO;
import lombok.Data;

import java.util.Date;

@Data
public class CommentBO {

    private Integer commentId;

    private AccountBO user;

    private String content;

    private String objectId;

    private ObjectTypeEnum objectType;

    private CommentBO reply;

    private Date createTime;
}
