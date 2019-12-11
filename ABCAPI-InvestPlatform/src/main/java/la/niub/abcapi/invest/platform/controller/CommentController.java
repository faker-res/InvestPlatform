package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.model.bo.comment.CommentBO;
import la.niub.abcapi.invest.platform.model.request.comment.CommentAddRequest;
import la.niub.abcapi.invest.platform.model.request.comment.CommentListRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.model.response.comment.CommentListResponse;
import la.niub.abcapi.invest.platform.service.ICommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评论
 */
@RestController
@RequestMapping(path = "/comment")
public class CommentController {

    private final static Logger logger = LogManager.getLogger(CommentController.class);

    @Autowired
    private ICommentService commentService;

    /**
     * 评论列表
     * @return
     */
    @GetMapping(path = "/list")
    Response list(CommentListRequest param) {
        try{
            CommentListResponse result = commentService.list(param);
            return new Response(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 发表评论
     * @return
     */
    @PostMapping(path = "/add")
    Response add(@RequestBody CommentAddRequest param) {
        try{
            CommentBO result = commentService.add(param);
            return new Response(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }

    /**
     * 删除评论
     * @return
     */
    @GetMapping(path = "/delete")
    Response delete(@RequestParam(value = "comment_id") Integer commentId) {
        try{
            Boolean result = commentService.delete(commentId);
            return new Response(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
}
