package la.niub.abcapi.invest.platform.model.request.client;

import lombok.Data;

@Data
public class KeyWordRequest {

    private Integer type = 1;

    private Integer module = 10001;

    private Integer cate = 0;

    private Integer terminal = 0;

    private Integer lan = 0;

    private Integer offset = 0;

    private Integer limit = 1;

}
