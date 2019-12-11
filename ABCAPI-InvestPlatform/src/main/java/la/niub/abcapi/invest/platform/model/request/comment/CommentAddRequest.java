package la.niub.abcapi.invest.platform.model.request.comment;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import lombok.Data;

@Data
public class CommentAddRequest {

    private String userId;

    private String content;

    private String object_id;

    private ObjectTypeEnum object_type;

    private Integer reply_id;
}
