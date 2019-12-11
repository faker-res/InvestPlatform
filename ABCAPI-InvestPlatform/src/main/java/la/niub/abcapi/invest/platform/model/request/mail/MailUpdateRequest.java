package la.niub.abcapi.invest.platform.model.request.mail;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MailUpdateRequest {

    private String id;

    private String uid;

    private String userId;
    //显示名称
    private String displayName;

    /**
     * 过滤条件
     */
    private List<String> fileFilter = new ArrayList<String>();

    private List<String> senderBlackList = new ArrayList<String>();

    private List<String> subjectBlackList = new ArrayList<String>();

}
