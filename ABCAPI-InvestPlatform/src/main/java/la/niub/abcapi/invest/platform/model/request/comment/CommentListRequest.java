package la.niub.abcapi.invest.platform.model.request.comment;

import la.niub.abcapi.invest.platform.config.CommonConfig;
import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class CommentListRequest {

    private String userId;

    private Boolean self = false;

    private String object_id;

    private ObjectTypeEnum object_type;

    private Long time;

    private Integer limit = 10;
}
