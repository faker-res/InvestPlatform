package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.component.client.IInvestCloudClient;
import la.niub.abcapi.invest.platform.component.util.StringUtil;
import la.niub.abcapi.invest.platform.component.util.TimeUtil;
import la.niub.abcapi.invest.platform.config.code.AccountEnumCodeConfig;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewRecommendedStockDao;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewRecommendedStockRuleDao;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewStockIndexDao;
import la.niub.abcapi.invest.platform.model.bo.stock.StockMonthReturnRateBO;
import la.niub.abcapi.invest.platform.model.bo.stock.StockRecommendedBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewRecommendedStockModel;
import la.niub.abcapi.invest.platform.model.invest.InvestnewRecommendedStockRuleModel;
import la.niub.abcapi.invest.platform.model.invest.InvestnewStockIndexModel;
import la.niub.abcapi.invest.platform.model.reporter.SecBasicInfoModel;
import la.niub.abcapi.invest.platform.model.reporter.SecIndustryNewModel;
import la.niub.abcapi.invest.platform.model.request.client.stock.StockGetMonthReturnRateRequest;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.IAccountService;
import la.niub.abcapi.invest.platform.service.IRecommendStockService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RecommendStockServiceImpl implements IRecommendStockService {

    private final static Logger logger = LogManager.getLogger(RecommendStockServiceImpl.class);

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IInvestnewRecommendedStockDao investnewRecommendedStockDao;

    @Autowired
    private IInvestnewStockIndexDao investnewStockIndexDao;

    @Autowired
    private IInvestnewRecommendedStockRuleDao investnewRecommendedStockRuleDao;

    @Autowired
    private IInvestCloudClient investCloudClient;

    //线程池
    private static ExecutorService fixed_thread_pool;
    {
        fixed_thread_pool = Executors.newFixedThreadPool(1);
    }

    @Override
    public Integer handleEmailStock(String userId,String broker,Date pushDate,String attachmentUrl) throws Exception {
        Long companyId = accountService.getUserCompanyId(userId);
        if (companyId == null){
            throw new Exception(AccountEnumCodeConfig.MISS_COMPANY.getMessage());
        }
        logger.info("-----公司id:{}", companyId);

        InputStream is = null;
        try {
            URL url = new URL(attachmentUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3 * 60 * 1000);
            is = conn.getInputStream();
        } catch (Exception e) {
            try {
                URL url = new URL(attachmentUrl);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3 * 60 * 1000);
                is = conn.getInputStream();
            } catch (Exception e2) {
                throw new Exception("获取在线文件出错");
            }
        }

        List<StockRecommendedBO> stockRecommendedBOList = null;
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(is);

            List<String> sheetFilters = new ArrayList<>();
            List<InvestnewRecommendedStockRuleModel> investnewRecommendedStockRuleModelList = investnewRecommendedStockRuleDao.selectByCompanyIdAndBroker(companyId,broker);
            investnewRecommendedStockRuleModelList.forEach((item)->{
                if (StringUtils.isNotBlank(item.getSheet_name())){
                    sheetFilters.add(item.getSheet_name().trim());
                }
            });

            stockRecommendedBOList = parseGoldStock(wb,sheetFilters);
            logger.info("-----读取到的数据:{}", stockRecommendedBOList);
            if(CollectionUtils.isEmpty(stockRecommendedBOList)) {
                logger.error("读取到空列表");
            }
            for (StockRecommendedBO item : stockRecommendedBOList) {
                item.setUserId(userId);
                item.setParentId(companyId.toString());
                item.setBroker(broker);
            }
            logger.info("-----数据构造完成，将要存储");
        } catch (Exception e) {
            logger.error("读取远程excel文件内容异常:"+e.getMessage(), e);
            throw new RuntimeException("读取远程excel文件内容异常:"+e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("关闭流异常", e);
//                    throw new RuntimeException("关闭流异常", e);
                }
            }
            if (wb != null && wb instanceof SXSSFWorkbook) {
                SXSSFWorkbook xssfwb = (SXSSFWorkbook) wb;
                xssfwb.dispose();
            }
        }

        Integer success = 0;
        try {
            logger.info("-----开始存储", stockRecommendedBOList);
            if (stockRecommendedBOList != null && !stockRecommendedBOList.isEmpty()){
                success = saveRecommendedStock(stockRecommendedBOList,pushDate);
            }
        } catch (Exception e) {
            logger.error("存储失败", e);
//            throw new RuntimeException("存储失败", e);
            try {
                logger.info("-----再次存储");
                Thread.sleep(1000);
                if (stockRecommendedBOList != null && !stockRecommendedBOList.isEmpty()){
                    success = saveRecommendedStock(stockRecommendedBOList,pushDate);
                }
            } catch (Exception e2) {
                logger.error("存储失败", e2);
                throw new RuntimeException("存储失败", e2);
            }
        }

        logger.info("--------------推送数据 end----------------");
        return success;
    }

    public List<StockRecommendedBO> parseGoldStock(Workbook workbook,List<String> sheetFilters) {
//        System.out.println("getNumberOfSheets: "+workbook.getNumberOfSheets());
        LinkedHashMap<String,StockRecommendedBO> stockRecommendedBOMap = new LinkedHashMap<>();
        //遍历sheet
        Iterator sheetIterator = workbook.sheetIterator();
        List<Sheet> sheets = new ArrayList<>();
        while(sheetIterator.hasNext()){
            Sheet sheet = (Sheet) sheetIterator.next();
            logger.info("getSheetName: "+sheet.getSheetName());
            if (sheet.getLastRowNum() == 0){
                continue;
            }
            if (!ObjectUtils.isEmpty(sheetFilters) && !StringUtil.contains(sheet.getSheetName(),sheetFilters)){
                logger.info("sheetName不符合,{}",sheetFilters);
                continue;
            }
            sheets.add(sheet);
        }
        for (Sheet sheet : sheets) {
            Set<Integer> rowFilter = new HashSet<>();
            Map<Integer,List<String>> data = new HashMap();
            Set<String> cellAllValues = new HashSet<>();

            //找出无用的行，待后续过滤掉
            rowFilter.add(0);//大部分推荐第一行都是表头，可以过滤
            Iterator rowIterator = sheet.rowIterator();
            while(rowIterator.hasNext()){
                Row row = (Row) rowIterator.next();

                if (row.getLastCellNum() == 0){
                    rowFilter.add(row.getRowNum());
                    continue;
                }

                Boolean allBlank = true;
                Iterator cellIterator = row.cellIterator();
                while(cellIterator.hasNext()){
                    Cell cell = (Cell) cellIterator.next();
                    if (!org.apache.commons.lang3.StringUtils.isBlank(cell.toString())){
                        allBlank = false;
                        break;
                    }
                }
                if (allBlank){
                    rowFilter.add(row.getRowNum());
                    continue;
                }

//            Integer blankNum = 0;
//            Iterator cellIterator = row.cellIterator();
//            while(cellIterator.hasNext()){
//                Cell cell = (Cell) cellIterator.next();
//                if (StringUtils.isBlank(cell.toString())){
//                    blankNum++;
//                }
//            }
//            Float blankRate = Float.valueOf(blankNum)/row.getLastCellNum();
//            if (blankRate >= 0.5){
//                rowFilter.add(row.getRowNum());
//            }
            }

            //构造以列数为键，列值列表为值的map
            //存储所有单元格值的list
            rowIterator = sheet.rowIterator();
            while(rowIterator.hasNext()){
                Row row = (Row) rowIterator.next();
                if (rowFilter.contains(row.getRowNum())){//跳过无用的行
                    continue;
                }

                Iterator cellIterator = row.cellIterator();
                while(cellIterator.hasNext()){
                    Cell cell = (Cell) cellIterator.next();
                    String cellString = cell.toString();
                    if (!StringUtils.isBlank(cellString)){
                        cellAllValues.add(cellString);

                        Integer columnIndex = cell.getColumnIndex();
                        List<String> columnList;
                        if ((columnList = data.get(columnIndex)) == null){
                            columnList = new ArrayList<>();
                        }
                        columnList.add(cellString);
                        data.put(columnIndex,columnList);
                    }
                }
            }

            //找出列map中无用的列过滤掉
            Iterator<Map.Entry<Integer,List<String>>> dataIterator = data.entrySet().iterator();
            while (dataIterator.hasNext()){
                Map.Entry<Integer,List<String>> entry = dataIterator.next();
                if (new HashSet(entry.getValue()).size() <= 3){
                    dataIterator.remove();
                    continue;
                }
                Float uniqueRate = Float.valueOf(new HashSet(entry.getValue()).size())/entry.getValue().size();
                if (uniqueRate <= 0.5){
                    dataIterator.remove();
                    continue;
                }
            }

            //根据所有单元格值的list找出匹配的证券列表
//            List<SecBasicInfoModel> secBasicInfoModelList = investCloudClient.getStockByNameOrCode(new ArrayList<>(cellAllValues));
            Response<List<SecBasicInfoModel>> getStockByNameOrCodeResp = investCloudClient.getStockByNameOrCode(new ArrayList<>(cellAllValues));
            List<SecBasicInfoModel> secBasicInfoModelList = getStockByNameOrCodeResp.getData();
            List<String> stockNames = new ArrayList<>();
            Map<String,SecBasicInfoModel> stockMap = new HashMap<>();
            for (SecBasicInfoModel item : secBasicInfoModelList){
                stockNames.add(item.getSec_name());
                stockMap.put(item.getAbc_code(),item);
                stockMap.put(item.getSec_code()+"_"+item.getSec_name(),item);
                if (!stockMap.containsKey(item.getSec_name()) || stockMap.get(item.getSec_name()).getAbc_code().endsWith("HK")){
                    stockMap.put(item.getSec_name(),item);
                }
            }

            //证券代码、证券名称、推荐理由所在的列
            Integer codeColumnIndex = null;
            Integer stockColumnIndex = null;
            Integer reasonColumnIndex = null;

            //找出证券代码所在的列
            for (Map.Entry<Integer,List<String>> entry : data.entrySet()){
                Integer columnIndex = entry.getKey();
                Integer matchNum = 0;
                for (String item : entry.getValue()){
                    if (item.trim().matches("^[0-9A-Za-z]{4,6}(\\.[A-Za-z]{2})?$")){
                        matchNum++;
                    }
                }
                Float matchRate = Float.valueOf(matchNum)/entry.getValue().size();
                if (matchRate >= 0.7){
                    codeColumnIndex = columnIndex;
                    break;
                }
            }

            //找出证券名称所在的列
            for (Map.Entry<Integer,List<String>> entry : data.entrySet()){
                Integer columnIndex = entry.getKey();
                Integer matchNum = 0;
                for (String item : entry.getValue()){
                    if (stockNames.contains(item.trim())){
                        matchNum++;
                    }
                }
                Float matchRate = Float.valueOf(matchNum)/entry.getValue().size();
                if (matchRate >= 0.7){
                    stockColumnIndex = columnIndex;
                    break;
                }
            }

            //找出推荐理由所在的列
            for (Map.Entry<Integer,List<String>> entry : data.entrySet()){
                Integer columnIndex = entry.getKey();
                if (codeColumnIndex != null && columnIndex.equals(codeColumnIndex)){
                    continue;
                }
                if (stockColumnIndex != null && columnIndex.equals(stockColumnIndex)){
                    continue;
                }
                Integer matchNum = 0;
                for (String item : entry.getValue()){
                    if (item.trim().matches("^[\\p{Punct}\\d\\.A-Za-z]+$")){
                        continue;
                    }
                    if (item.length() >= 8){
                        matchNum++;
                    }
                }
                Float matchRate = Float.valueOf(matchNum)/entry.getValue().size();
                if (matchRate >= 0.7){
                    reasonColumnIndex = columnIndex;
                    break;
                }
            }

            logger.info("codeColumnIndex: "+codeColumnIndex+" stockColumnIndex: "+stockColumnIndex+" reasonColumnIndex: "+reasonColumnIndex);

            //如果找不到证券代码和证券名称的列，跳过
            if (codeColumnIndex == null && stockColumnIndex == null){
                continue;
            }

            //循环每一行，根据证券代码、证券名称、推荐理由，构造每个推荐股票的业务对象
            rowIterator = sheet.rowIterator();
            while(rowIterator.hasNext()){
                Row row = (Row) rowIterator.next();
                if (rowFilter.contains(row.getRowNum())){//跳过无用的行
                    continue;
                }

                //获取本行股票的证券代码
                String code = null;
                if (codeColumnIndex != null && row.getCell(codeColumnIndex) != null){
                    code = row.getCell(codeColumnIndex).toString().trim();
                    if (StringUtils.isBlank(code)){
                        continue;
                    }
                    if (!code.matches("^[0-9A-Za-z]{4,6}(\\.[A-Za-z]{2})?$")){
                        continue;
                    }
                }
                //获取本行股票的证券名称
                String stock = null;
                if (stockColumnIndex != null && row.getCell(stockColumnIndex) != null){
                    stock = row.getCell(stockColumnIndex).toString().trim();
                    if (StringUtils.isBlank(stock)){
                        continue;
                    }
                }
                //获取本行股票的推荐理由
                String reason = null;
                if (reasonColumnIndex != null && row.getCell(reasonColumnIndex) != null){
                    reason = row.getCell(reasonColumnIndex).toString();
                    if (StringUtils.isBlank(reason)){
                        continue;
                    }
                }
                //如果代码和名称都找不到，跳过
                if (code == null && stock == null){
                    continue;
                }

                //根据前面找到的代码和名称，匹配对应证券原始数据对象
                SecBasicInfoModel secBasicInfoModel = null;
                if (code != null && code.matches("^[0-9A-Za-z]{4,6}\\.[A-Za-z]{2}$")){
                    secBasicInfoModel = stockMap.get(code.toUpperCase());
                }else if (code != null && stock != null){
                    secBasicInfoModel = stockMap.get(code+"_"+stock);
                }
                if (secBasicInfoModel == null && stock != null){
                    secBasicInfoModel = stockMap.get(stock);
                }
                if (secBasicInfoModel == null){
                    continue;
                }

                //构造证券业务对象
                if (stockRecommendedBOMap.containsKey(secBasicInfoModel.getAbc_code())){
                    continue;
                }
                StockRecommendedBO stockRecommendedBO = new StockRecommendedBO();
                stockRecommendedBO.setSecUniCode(secBasicInfoModel.getSec_uni_code());
                stockRecommendedBO.setStockCode(secBasicInfoModel.getAbc_code());
                stockRecommendedBO.setStockName(secBasicInfoModel.getSec_name());
                stockRecommendedBO.setReason(StringUtils.defaultIfEmpty(reason,""));
                stockRecommendedBO.setSecType(secBasicInfoModel.getSec_type());
                stockRecommendedBO.setSecSmallType(secBasicInfoModel.getSec_small_type());
                stockRecommendedBOMap.put(secBasicInfoModel.getAbc_code(),stockRecommendedBO);
            }
        }

        if (workbook != null && workbook instanceof SXSSFWorkbook) {
            SXSSFWorkbook xssfwb = (SXSSFWorkbook) workbook;
            xssfwb.dispose();
        }

        return new ArrayList<>(stockRecommendedBOMap.values());
    }

    public Integer saveRecommendedStock(List<StockRecommendedBO> stocks,Date pushDate) {
        for (Iterator it = stocks.iterator(); it.hasNext();) {
            StockRecommendedBO stockRecommendedBO = (StockRecommendedBO)it.next();
            //过滤港股 2018-11-20 wuxiao
            if (stockRecommendedBO.getStockCode().endsWith("HK")){
                it.remove();
            }
        }

        List<Long> secUniCodes = new ArrayList<>();
        for (StockRecommendedBO item : stocks){
            secUniCodes.add(item.getSecUniCode());
        }
//        Map<Long,String> industrys = new HashMap<>();
//        List<SecIndustryNewModel> industryT = secIndustryNewDao.buildInduListBySecUniCodes(secUniCodes);
//        for (SecIndustryNewModel item : industryT){
//            if (item.getIndu_standard() == 1001014 // 优先展示申万
//            ){
//                industrys.put(item.getSec_uni_code(),item.getFirst_indu_name());
//            }
//        }
        Map<Long, SecIndustryNewModel> industryT = new HashMap<>();
        Response<Map<Long, SecIndustryNewModel>> getIndustryResp = investCloudClient.getIndustry(secUniCodes);
        if (getIndustryResp.getData() != null){
            industryT = getIndustryResp.getData();
        }

        Integer success = 0;
        Date now = new Date();

        if (pushDate == null){
            pushDate = now;
        }
        Boolean currentMonth = pushDate.getYear() == now.getYear() && pushDate.getMonth() == now.getMonth();
        //2018-12-13 wuxiao 当月大于等于20号到下月小于20号算下月的金股推荐
        Boolean addMonth = false;
        if (pushDate.getDate() >= 20){
            addMonth = true;
        }
        String indexPushMonth = TimeUtil.toString(pushDate,"yyyy-MM");
        if (addMonth){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pushDate);
            calendar.add(Calendar.MONTH,1);
            indexPushMonth = TimeUtil.toString(calendar.getTime(),"yyyy-MM");
        }
        List<InvestnewRecommendedStockModel> tblInvestRecommendedStockList = new ArrayList<>();
        for (StockRecommendedBO item : stocks){
            InvestnewRecommendedStockModel model = new InvestnewRecommendedStockModel();
            model.setUser_id(item.getUserId());
            model.setParent_id(item.getParentId());
            model.setSec_uni_code(item.getSecUniCode());
            model.setStock_code(item.getStockCode());
            model.setStock_name(item.getStockName());
//            model.setIndustry(industrys.getOrDefault(item.getSecUniCode(),""));
            SecIndustryNewModel industry = industryT.get(item.getSecUniCode());
            if (industry != null){
                model.setIndustry(industry.getFirst_indu_name());
            }
            model.setRecommended_reason(item.getReason());
            model.setBroker(item.getBroker());
            model.setPush_month(indexPushMonth);
            model.setPush_time(now);
            tblInvestRecommendedStockList.add(model);
        }
        if (!tblInvestRecommendedStockList.isEmpty()){
            success += investnewRecommendedStockDao.insertMulti(tblInvestRecommendedStockList);
        }

        String stockPushMonth = TimeUtil.toString(pushDate,"yyyy-MM");
        List<Long> notEmptySecUniCodes = investnewStockIndexDao.findCodesDataNotEmpty(secUniCodes,stockPushMonth);
        List<InvestnewStockIndexModel> tblInvestStockIndexList = new ArrayList<>();
        for (StockRecommendedBO item : stocks){
            if (notEmptySecUniCodes.contains(item.getSecUniCode())){
                continue;
            }
            //先保存不计算收益率的数据模型
            InvestnewStockIndexModel model = new InvestnewStockIndexModel();
            model.setSec_uni_code(item.getSecUniCode());
            model.setStock_code(item.getStockCode());
            model.setStock_name(item.getStockName());
            model.setPush_month(stockPushMonth);
            tblInvestStockIndexList.add(model);
        }
        if (currentMonth){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pushDate);
            calendar.add(Calendar.MONTH,-1);
            stockPushMonth = TimeUtil.toString(calendar.getTime(),"yyyy-MM");
            notEmptySecUniCodes = investnewStockIndexDao.findCodesDataNotEmpty(secUniCodes,stockPushMonth);
            for (StockRecommendedBO item : stocks){
                if (notEmptySecUniCodes.contains(item.getSecUniCode())){
                    continue;
                }
                //先保存不计算收益率的数据模型
                InvestnewStockIndexModel model = new InvestnewStockIndexModel();
                model.setSec_uni_code(item.getSecUniCode());
                model.setStock_code(item.getStockCode());
                model.setStock_name(item.getStockName());
                model.setPush_month(stockPushMonth);
                tblInvestStockIndexList.add(model);
            }
        }
        if (addMonth){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pushDate);
            calendar.add(Calendar.MONTH,1);
            stockPushMonth = TimeUtil.toString(calendar.getTime(),"yyyy-MM");
            notEmptySecUniCodes = investnewStockIndexDao.findCodesDataNotEmpty(secUniCodes,stockPushMonth);
            for (StockRecommendedBO item : stocks){
                if (notEmptySecUniCodes.contains(item.getSecUniCode())){
                    continue;
                }
                //先保存不计算收益率的数据模型
                InvestnewStockIndexModel model = new InvestnewStockIndexModel();
                model.setSec_uni_code(item.getSecUniCode());
                model.setStock_code(item.getStockCode());
                model.setStock_name(item.getStockName());
                model.setPush_month(stockPushMonth);
                tblInvestStockIndexList.add(model);
            }

            calendar = Calendar.getInstance();
            calendar.setTime(pushDate);
            calendar.add(Calendar.MONTH,-1);
            stockPushMonth = TimeUtil.toString(calendar.getTime(),"yyyy-MM");
            notEmptySecUniCodes = investnewStockIndexDao.findCodesDataNotEmpty(secUniCodes,stockPushMonth);
            for (StockRecommendedBO item : stocks){
                if (notEmptySecUniCodes.contains(item.getSecUniCode())){
                    continue;
                }
                //先保存不计算收益率的数据模型
                InvestnewStockIndexModel model = new InvestnewStockIndexModel();
                model.setSec_uni_code(item.getSecUniCode());
                model.setStock_code(item.getStockCode());
                model.setStock_name(item.getStockName());
                model.setPush_month(stockPushMonth);
                tblInvestStockIndexList.add(model);
            }
        }
        if (!tblInvestStockIndexList.isEmpty()){
            investnewStockIndexDao.insertMulti(tblInvestStockIndexList);
        }

        fixed_thread_pool.execute(() -> {
            try {
                String nowMonthStr = TimeUtil.toString(now,"yyyy-MM");
                Date nowMonth = TimeUtil.parseDateStr(nowMonthStr,"yyyy-MM");
                for (Iterator it = tblInvestStockIndexList.iterator();it.hasNext();){
                    InvestnewStockIndexModel item = (InvestnewStockIndexModel)it.next();
                    Date pushMonth = TimeUtil.parseDateStr(item.getPush_month(),"yyyy-MM");
                    if (pushMonth.equals(nowMonth) || pushMonth.after(nowMonth)){
                        it.remove();
                    }
                }
                calcRecommendedStockReturnRate(tblInvestStockIndexList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return success;
    }

    @Override
    public Integer calcRecommendedStockReturnRate(List<InvestnewStockIndexModel> stockIndexs) {
        logger.info("Begin calcRecommendedStockReturnRate for "+stockIndexs.size()+" stocks");
        Integer total = 0;
        for (InvestnewStockIndexModel item : stockIndexs){
            logger.info("calc start for stock code: "+item.getStock_code());
            Date month = TimeUtil.parseDateStr(item.getPush_month(),"yyyy-MM");
//            StockMonthReturnRateBO stockMonthReturnRateBO = stockService.getMonthReturnRate(item.getSec_uni_code(),month);
            StockGetMonthReturnRateRequest param = new StockGetMonthReturnRateRequest();
            param.setSecUniCode(item.getSec_uni_code());
            param.setMonth(month);
            Response<Map<Long, StockMonthReturnRateBO>> getMonthReturnRateResp = investCloudClient.getMonthReturnRate(param);
            Map<Long, StockMonthReturnRateBO> stockMonthReturnRateBOMap = getMonthReturnRateResp.getData();
            if (stockMonthReturnRateBOMap == null){
                continue;
            }
            StockMonthReturnRateBO stockMonthReturnRateBO = stockMonthReturnRateBOMap.get(item.getSec_uni_code());
            if (stockMonthReturnRateBO == null){
                continue;
            }
            item.setGold_rate(stockMonthReturnRateBO.getGoldRate());
            item.setIndustry_rate(stockMonthReturnRateBO.getIndustryRate());
            item.setMonth_begin_price(stockMonthReturnRateBO.getMonthBeginPrice());
            item.setMonth_end_price(stockMonthReturnRateBO.getMonthEndPrice());

            Integer success = investnewStockIndexDao.updateData(item);
            if (success > 0){
                logger.info("calc success for stock code: "+item.getStock_code());
            }else{
                logger.info("calc fail for stock code: "+item.getStock_code());
            }

            total += success;
        }
        logger.info("End calcRecommendedStockReturnRate success "+total+" stocks");
        return total;
    }

}
