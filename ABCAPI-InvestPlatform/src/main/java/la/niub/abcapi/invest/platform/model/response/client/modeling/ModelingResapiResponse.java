package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

@Data
public class ModelingResapiResponse<T> {

    private Boolean success;

    private String message;

    private T data;
}
