package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewReadHistoryDao;
import la.niub.abcapi.invest.platform.model.bo.log.ReadLogBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewReadHistoryModel;
import la.niub.abcapi.invest.platform.model.invest.InvestnewUserModel;
import la.niub.abcapi.invest.platform.model.reporter.SecBasicInfoModel;
import la.niub.abcapi.invest.platform.model.request.log.HistoryAddRequest;
import la.niub.abcapi.invest.platform.model.request.log.HistoryListRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.IAccountService;
import la.niub.abcapi.invest.platform.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IInvestnewReadHistoryDao investnewReadHistoryDao;

    @Autowired
    private IInvestCloudClient investCloudClient;

    @Override
    public ReadLogBO addHistory(HistoryAddRequest request) {
        InvestnewReadHistoryModel investnewReadHistoryModel = new InvestnewReadHistoryModel();
        investnewReadHistoryModel.setTitle(request.getTitle());
        investnewReadHistoryModel.setObject_id(request.getObject_id());
        investnewReadHistoryModel.setObject_type(request.getObject_type().name().toLowerCase());
        investnewReadHistoryModel.setAuthor(request.getAuthor());
        investnewReadHistoryModel.setCreate_time(new Date());

        investnewReadHistoryModel.setUser_id(request.getUserId());
        InvestnewUserModel investnewUserModel = accountService.userModelByUserId(request.getUserId());
        if (investnewUserModel != null){
            investnewReadHistoryModel.setCompany_id(investnewUserModel.getCompany_id());
        }

        investnewReadHistoryModel.setStock_code(request.getStock_code());
        List<String> nameOrCode = new ArrayList<String>(){{add(request.getStock_code());}};
        Response<List<SecBasicInfoModel>> response = investCloudClient.getStockByNameOrCode(nameOrCode);
        if (response != null && response.getData() != null && !response.getData().isEmpty()){
            SecBasicInfoModel secBasicInfoModel = response.getData().get(0);
            investnewReadHistoryModel.setStock_name(secBasicInfoModel.getSec_name());
        }

        Boolean result = investnewReadHistoryDao.insertSelective(investnewReadHistoryModel) > 0;
        if (!result){
            return null;
        }
        return parseLog(investnewReadHistoryModel);
    }

    @Override
    public List<ReadLogBO> ListHistory(HistoryListRequest request) {
        Integer offset = request.getOffset()+(request.getPage()-1)*request.getLimit();
        String objectType = request.getObject_type() != null ? request.getObject_type().name().toLowerCase() : null;
        Date startTime = request.getStart_time() != null ? new Date(request.getStart_time()) : null;
        Date endTime = request.getEnd_time() != null ? new Date(request.getEnd_time()) : null;
        List<InvestnewReadHistoryModel> investnewReadHistoryModelList = null;
        if (request.getGroup_by_id()){
            investnewReadHistoryModelList = investnewReadHistoryDao.searchGroupById(request.getUserId(),objectType,
                    request.getStock_code(),startTime,endTime,offset,request.getLimit());
        }else{
            investnewReadHistoryModelList = investnewReadHistoryDao.search(request.getUserId(),objectType,
                    request.getStock_code(),startTime,endTime,offset,request.getLimit());
        }

        List<ReadLogBO> readLogBOList = new ArrayList<>();
        if (ObjectUtils.isEmpty(investnewReadHistoryModelList)){
            return readLogBOList;
        }
        for (InvestnewReadHistoryModel item : investnewReadHistoryModelList){
            readLogBOList.add(parseLog(item));
        }
        return readLogBOList;
    }

    private ReadLogBO parseLog(InvestnewReadHistoryModel model){
        ReadLogBO readLogBO = new ReadLogBO();
        readLogBO.setUserId(model.getUser_id());
        readLogBO.setCompanyId(model.getCompany_id());
        readLogBO.setTitle(model.getTitle());
        readLogBO.setObjectId(model.getObject_id());
        try{
            readLogBO.setObjectType(ObjectTypeEnum.valueOf(model.getObject_type().toUpperCase()));
        }catch (Exception e){};
        try{
            readLogBO.setAuthor(Arrays.asList(model.getAuthor().split(",")));
        }catch (Exception e){};
        readLogBO.setStockCode(model.getStock_code());
        readLogBO.setStockName(model.getStock_name());
        readLogBO.setReadTime(model.getCreate_time());
        return readLogBO;
    }
}
