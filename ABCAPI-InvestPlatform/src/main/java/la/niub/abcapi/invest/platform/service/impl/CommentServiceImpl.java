package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewCommentDao;
import la.niub.abcapi.invest.platform.model.bo.account.AccountBO;
import la.niub.abcapi.invest.platform.model.bo.comment.CommentBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewCommentModel;
import la.niub.abcapi.invest.platform.model.request.comment.CommentAddRequest;
import la.niub.abcapi.invest.platform.model.request.comment.CommentListRequest;
import la.niub.abcapi.invest.platform.model.response.comment.CommentListResponse;
import la.niub.abcapi.invest.platform.service.IAccountService;
import la.niub.abcapi.invest.platform.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IInvestnewCommentDao investnewCommentDao;

    @Override
    public CommentBO add(CommentAddRequest param) throws Exception {
        InvestnewCommentModel investnewCommentModel = new InvestnewCommentModel();
        investnewCommentModel.setUser_id(param.getUserId());
        investnewCommentModel.setContent(param.getContent());
        investnewCommentModel.setObject_id(param.getObject_id());
        investnewCommentModel.setObject_type(param.getObject_type().name().toLowerCase());
        investnewCommentModel.setReply_id(param.getReply_id());
        Boolean result = investnewCommentDao.insertSelectiveSelectId(investnewCommentModel) > 0;
        if (!result){
            return null;
        }
        List<Integer> commentIds = new ArrayList<Integer>(){{
            add(investnewCommentModel.getId());
            if (param.getReply_id() != null){
                add(param.getReply_id());
            }
        }};
        Map<Integer,CommentBO> commentBOMap = getCommentMap(commentIds);
        CommentBO commentBO = commentBOMap.get(investnewCommentModel.getId());
        if (param.getReply_id() != null){
            commentBO.setReply(commentBOMap.get(param.getReply_id()));
        }
        return commentBO;
    }

    @Override
    public Boolean delete(Integer commentId) {
        return investnewCommentDao.deleteByPrimaryKey(commentId) > 0;
    }

    @Override
    public CommentListResponse list(CommentListRequest param) {
        CommentListResponse commentListResponse = new CommentListResponse();

        String userId = param.getSelf() ? param.getUserId() : null;
        Date time = new Date();
        if (param.getTime() != null){
            time = new Date(param.getTime());
        }
        Integer total = investnewCommentDao.getCount(
                param.getObject_id(),param.getObject_type().name().toLowerCase(),
                userId,time,param.getLimit());
        commentListResponse.setTotal(total);
        if (total.equals(0)){
            return commentListResponse;
        }

        List<CommentBO> commentBOList = new ArrayList<>();
        List<InvestnewCommentModel> investnewCommentModelList = investnewCommentDao.getByObject(
                param.getObject_id(),param.getObject_type().name().toLowerCase(),
                userId,time,param.getLimit());

        List<String> userIds = new ArrayList<>();
        List<Integer> replyIds = new ArrayList<>();
        for (InvestnewCommentModel item : investnewCommentModelList){
            userIds.add(item.getUser_id());
            if (item.getReply_id() != null){
                replyIds.add(item.getReply_id());
            }
        }
        Map<String, AccountBO> accountBOMap = accountService.userInfoByUserIds(userIds);
        Map<Integer,CommentBO> commentBOMap = getCommentMap(replyIds);

        for (InvestnewCommentModel item : investnewCommentModelList){
            CommentBO commentBO = new CommentBO();
            commentBO.setCommentId(item.getId());
            commentBO.setUser(accountBOMap.get(item.getUser_id()));
            commentBO.setContent(item.getContent());
            commentBO.setObjectId(item.getObject_id());
            commentBO.setObjectType(ObjectTypeEnum.valueOf(item.getObject_type().toUpperCase()));
            if (item.getReply_id() != null){
                commentBO.setReply(commentBOMap.get(item.getReply_id()));
            }
            commentBO.setCreateTime(item.getCreate_time());
            commentBOList.add(commentBO);
        }
        commentListResponse.setList(commentBOList);
        return commentListResponse;
    }

    private Map<Integer,CommentBO> getCommentMap(List<Integer> commentIds){
        Map<Integer,CommentBO> commentBOMap = new HashMap<>();
        if (commentIds.isEmpty()){
            return commentBOMap;
        }

        List<InvestnewCommentModel> investnewCommentModelList = investnewCommentDao.selectByIds(commentIds);
        if (investnewCommentModelList.isEmpty()){
            return commentBOMap;
        }

        List<String> userIds = new ArrayList<>();
        for (InvestnewCommentModel item : investnewCommentModelList){
            userIds.add(item.getUser_id());
        }
        Map<String, AccountBO> accountBOMap = accountService.userInfoByUserIds(userIds);

        for (InvestnewCommentModel item : investnewCommentModelList){
            CommentBO commentBO = new CommentBO();
            commentBO.setCommentId(item.getId());
            commentBO.setUser(accountBOMap.get(item.getUser_id()));
            commentBO.setContent(item.getContent());
            commentBO.setObjectId(item.getObject_id());
            commentBO.setObjectType(ObjectTypeEnum.valueOf(item.getObject_type().toUpperCase()));
//            commentBO.setReply();
            commentBO.setCreateTime(item.getCreate_time());
            commentBOMap.put(item.getId(),commentBO);
        }
        return commentBOMap;
    }
}
