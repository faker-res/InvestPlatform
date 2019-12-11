package la.niub.abcapi.invest.platform.model.request.client;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Mail配置信息
 * @author jrxia
 * 1/31/18
 */
@Data
public class ConfigUpdateRequest {

    private Integer id;

	//用户id
    private String userId;
    //显示名称
    private String name;

    /**
     * 过滤条件
     */
    private List<String> fileFilter = new ArrayList<>();

    private List<String> senderBlackList = new ArrayList<>();

    private List<String> subjectBlackList = new ArrayList<>();
}
