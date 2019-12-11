package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.bo.comment.CommentBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewStockIndexModel;
import la.niub.abcapi.invest.platform.model.request.comment.CommentAddRequest;
import la.niub.abcapi.invest.platform.model.request.comment.CommentListRequest;
import la.niub.abcapi.invest.platform.model.response.comment.CommentListResponse;

import java.util.Date;
import java.util.List;

/**
 * 评论
 */
public interface ICommentService {

    /**
     * 添加评论
     * @return
     */
    CommentBO add(CommentAddRequest param) throws Exception;

    /**
     * 删除评论
     * @return
     */
    Boolean delete(Integer commentId);

    /**
     * 评论列表
     * @return
     */
    CommentListResponse list(CommentListRequest param);
}
