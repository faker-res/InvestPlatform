package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

@Data
public class ModelingWechatGroupListResponse<T> {

    private String wechat_id;

    private Integer sub;

    private String group_icon;

    private String name;

    private String id;
}
