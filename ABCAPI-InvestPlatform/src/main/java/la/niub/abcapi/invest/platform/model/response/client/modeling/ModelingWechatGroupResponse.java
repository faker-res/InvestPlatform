package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

import java.util.List;

@Data
public class ModelingWechatGroupResponse<T> {

    private List<ModelingWechatGroupListResponse> groups;
}
