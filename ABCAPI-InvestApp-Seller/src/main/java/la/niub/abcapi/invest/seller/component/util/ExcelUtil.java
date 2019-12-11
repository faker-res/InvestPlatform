package la.niub.abcapi.invest.seller.component.util;

import la.niub.abcapi.invest.seller.config.enums.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author : ppan
 * @description : excel解析和导出工具类
 * @date : 2019-01-21 16:06
 */
public class ExcelUtil {


    /**
     * 单元格合并
     */
    private final static String CELL_MERGE_LEFT = "left";

    /**
     * 单元格合并
     */
    private final static String CELL_MERGE_TOP = "top";

    /**
     * 单元格合并
     */
    private final static String CELL_MERGE_LEFT_TOP = "left-top";

    /**
     * 构造Workbook
     * @param in
     * @param excelType
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbok(InputStream in, String excelType) throws Exception {
        Workbook workbook = null;
        if(ExcelTypeEnum.XLS.getType().equals(excelType)){
            //Excel 2003
            workbook = new HSSFWorkbook(in);
        }else if(ExcelTypeEnum.XLSX.getType().equals(excelType)){
            // Excel 2007/2010
            workbook = new XSSFWorkbook(in);
        } else {
            throw new RuntimeException("excel type error");
        }
        return workbook;
    }

    /**
     *
     * @param is
     * @param excelType excel类型(xls,xlsx)
     * @param sheetIndex sheet的索引,从0开始
     * @param startRow 开始行,从1开始
     * @param endRow 结束行,从1开始
     * @param startColumn 开始列,从1开始
     * @param endColumn 结束列,从1开始
     * @return
     * @throws Exception
     */
    public static List<List<String>> read(InputStream is, String excelType, Integer sheetIndex, Integer startRow, Integer endRow, Integer startColumn, Integer endColumn) throws Exception {
        List<List<String>> result = new ArrayList<>();

        Workbook workbook = getWorkbok(is, excelType);
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new RuntimeException("sheetIndex: " + sheetIndex + ", no exist");
        }
        Integer startRowIndex = startRow - 1 < 0 ? 0 : startRow - 1;
        Integer endRowIndex = endRow == null ? sheet.getLastRowNum() : endRow;
        for(int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return result;
            } else {
                Integer startColumnIndex = startColumn - 1 < 0 ? 0 : startColumn - 1;
                Integer endColumnIndex = endColumn == null ? row.getLastCellNum() : endColumn;
                List<String> dataList = new ArrayList<String>();
                for (int columnIndex = startColumnIndex; columnIndex < endColumnIndex; columnIndex++) {
                    Cell cell = row.getCell(columnIndex);
                    String date = cell == null || cell.getStringCellValue() == null ? null : cell.getStringCellValue().trim();
                    dataList.add(date);
                }

                result.add(dataList);
            }
        }

        return result;
    }

    /**
     *
     * @param is
     * @param excelType excel类型(xls,xlsx)
     * @param startRow 开始行,从1开始
     * @param endRow 结束行,从1开始
     * @param startColumn 开始列,从1开始
     * @param endColumn 结束列,从1开始
     * @return
     * @throws Exception
     */
    public static List<List<String>> readAll(InputStream is, String excelType, Integer startRow, Integer endRow, Integer startColumn, Integer endColumn) throws Exception {
        List<List<String>> result = new ArrayList<>();

        Workbook workbook = getWorkbok(is, excelType);
        for (int i = 0; i < workbook.getNumberOfSheets(); i ++) {
            Sheet sheet = workbook.getSheetAt(i);
            Integer startRowIndex = startRow - 1 < 0 ? 0 : startRow - 1;
            Integer endRowIndex = endRow == null ? sheet.getLastRowNum() : endRow;
            for(int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    return result;
                } else {
                    Integer startColumnIndex = startColumn - 1 < 0 ? 0 : startColumn - 1;
                    Integer endColumnIndex = endColumn == null ? row.getLastCellNum() : endColumn;
                    List<String> dataList = new ArrayList<String>();
                    for (int columnIndex = startColumnIndex; columnIndex < endColumnIndex; columnIndex++) {
                        Cell cell = row.getCell(columnIndex);
                        String date = cell == null || cell.getStringCellValue() == null ? null : cell.getStringCellValue().trim();
                        dataList.add(date);
                    }

                    result.add(dataList);
                }
            }
        }

        return result;
    }

    /**
     * 券商评分下载模板
     * @param outputStream
     * @param keyList 列名
     * @param dataMap 多个sheet数据
     * @param title 标题
     * @throws Exception
     */
    public static void exportTraderMarkTemplate(OutputStream outputStream, List<String> keyList, Map<String, List<List<String>>> dataMap, String title) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (Map.Entry<String, List<List<String>>> entry : dataMap.entrySet()) {
            HSSFSheet sheet = workbook.createSheet(entry.getKey());
            fillSheet(workbook, sheet, keyList, entry.getValue(), title);
        }

        workbook.write(outputStream);
    }

    private static void fillSheet(HSSFWorkbook workbook, HSSFSheet sheet, List<String> keyList, List<List<String>> sheetDataList, String title) throws Exception {
        // 填充标题
        HSSFRow rowTitle = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0, keyList.size() - 1));
        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(setStyle(workbook, setFont(workbook, (short)12, "微软雅黑")));

        // 填充表头
        HSSFRow rowHead = sheet.createRow(1);
        HSSFCellStyle headCellStyle = setStyle(workbook, setFont(workbook, (short) 10, "微软雅黑"));
        for(int i = 0; i < keyList.size(); i++){
            HSSFCell cell = rowHead.createCell(i);
            cell.setCellValue(keyList.get(i));
            cell.setCellStyle(headCellStyle);
        }

        // 填充主体数据
        HSSFCellStyle bodyCellStyle = setStyle(workbook, setFont(workbook, (short) 10, "微软雅黑"));
        for (int i = 0; i < sheetDataList.size(); i++) {
            HSSFRow row = sheet.createRow(i + 2);
            for (int j = 0; j < keyList.size(); j++) {
                Object objectValue = sheetDataList.get(i).get(j);
                boolean isMergeCell = CELL_MERGE_LEFT.equals(objectValue) || CELL_MERGE_TOP.equals(objectValue)
                        || CELL_MERGE_LEFT_TOP.equals(objectValue);
                if (!isMergeCell) {
                    int firstRow = i + 2;
                    int firstColumn = j;
                    int lastRow = i + 2;
                    int lastColumn = j;
                    for (int m = i + 1; m < sheetDataList.size(); m++) {
                        if (!CELL_MERGE_TOP.equals(sheetDataList.get(m).get(j))) {
                            lastRow = firstRow + m - i - 1;
                            break;
                        }
                        if (CELL_MERGE_TOP.equals(sheetDataList.get(m).get(j)) && m == sheetDataList.size() - 1) {
                            lastRow = firstRow + m - i;
                            break;
                        }

                        for (int n = j + 1; n < keyList.size(); n++) {
                            if (!CELL_MERGE_LEFT.equals(sheetDataList.get(i).get(n))){
                                lastColumn = firstColumn + n - j - 1;
                                break;
                            }

                            if (CELL_MERGE_LEFT.equals(sheetDataList.get(i).get(n)) && n == keyList.size() - 1) {
                                lastColumn = firstColumn + n - j;
                                break;
                            }
                        }
                    }

                    if (lastRow > firstRow  || lastColumn > firstColumn ) {
                        sheet.addMergedRegionUnsafe(new CellRangeAddress(firstRow,lastRow,firstColumn, lastColumn));
                    }
                    HSSFCell cell = row.createCell(j);
                    Pattern pattern = Pattern.compile("^\\d+$");
                    if (!StringUtil.isEmpty(sheetDataList.get(i).get(j)) && pattern.matcher(sheetDataList.get(i).get(j)).matches()) {
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(Double.parseDouble(sheetDataList.get(i).get(j)));
                    } else {
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(sheetDataList.get(i).get(j));
                    }
                    cell.setCellStyle(bodyCellStyle);
                }
            }
        }

        // 设置自动列宽
        setColumnAutoSize(sheet, keyList.size());

    }

    /**
     * @MethodName  setColumnAutoSize
     * @Description  设置工作表自动列宽和首行加粗
     * @param sheet
     */
    private static void setColumnAutoSize(HSSFSheet sheet, Integer size){
        //获取本列的最宽单元格的宽度
        for(int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++){
            sheet.autoSizeColumn(i);
        }

        setSizeColumn(sheet, size);
    }

    /**
     * 自适应宽度(中文支持)
     * @param sheet
     * @param size
     */
    private static void setSizeColumn(HSSFSheet sheet, Integer size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    /**
     *@MethodName  setFont
     * @Description  数据字体样式
     * @param workbook
     * @return
     * @throws Exception
     */
    private static HSSFFont setFont(Workbook workbook, Short fontSize, String fontName) throws Exception {
        HSSFFont font = (HSSFFont) workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setColor(HSSFColor.BLACK.index);

        return font;
    }

    /**
     *@MethodName  setDataStyle
     * @Description  数据样式
     * @param workbook
     * @return
     * @throws Exception
     */
    private static HSSFCellStyle setStyle(Workbook workbook, HSSFFont font) throws Exception {
        // 创建单元格样式
        HSSFCellStyle cellStyle = (HSSFCellStyle) workbook.createCellStyle();

        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        // 设置边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        // 设置字体
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 读取券商评分上传的打分excel
     * @param is
     * @param excelType
     * @param startRow
     * @return
     * @throws Exception
     */
    public static Map<String, List<List<String>>> readTraderMarkTaskUplaodExcel(InputStream is, String excelType, Integer startRow) throws Exception {
        Workbook workbook = getWorkbok(is, excelType);

        Map<String, List<List<String>>> result = new HashMap<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            List<List<String>> sheetDateList = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(i);
            if (StringUtil.isEmpty(sheet.getSheetName().trim())) {
                continue;
            }

            String sheetName = sheet.getSheetName();

            Integer startRowIndex = startRow - 1 < 0 ? 0 : startRow - 1;
            Integer endRowIndex = sheet.getLastRowNum();
            for(int rowIndex = startRowIndex; rowIndex <= endRowIndex + 1; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    result.put(sheetName, sheetDateList);
                    break;
                } else {
                    List<String> rowList = new ArrayList<>();
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum() ; columnIndex++) {
                        Cell cell = row.getCell(columnIndex);
                        String data = null;
                        if (cell != null) {
                            cell.setCellType(CellType.STRING);
                            data = cell.getStringCellValue();
                        }
                        rowList.add(data);
                    }

                    sheetDateList.add(rowList);
                }
            }
        }

        return result;
    }
}
