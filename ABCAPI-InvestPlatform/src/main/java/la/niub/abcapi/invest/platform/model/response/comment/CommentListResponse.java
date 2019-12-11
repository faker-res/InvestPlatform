package la.niub.abcapi.invest.platform.model.response.comment;

import la.niub.abcapi.invest.platform.model.bo.comment.CommentBO;
import lombok.Data;

import java.util.List;

@Data
public class CommentListResponse {

    private Integer total;

    private List<CommentBO> list;
}
