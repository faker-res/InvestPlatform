package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

@Data
public class ModelingWechatBindedListResponse<T> {

    private String wechat_id;

    private String headimg;

    private String nickname;

    private Integer status;
}
