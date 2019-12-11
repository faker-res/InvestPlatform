package la.niub.abcapi.invest.seller.component.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportUtil {


    public static final String FONT_RED_TAG = "red";


    public static String getRedReportType(String reportOne, String reportTwo, String reportThree) {
        String reportType = reportThree;
        if (StringUtils.isNotEmpty(reportOne) && reportOne.contains(FONT_RED_TAG)) {
            reportType = reportOne;
        } else if (StringUtils.isNotEmpty(reportTwo) && reportTwo.contains(FONT_RED_TAG)) {
            reportType = reportTwo;
        } else if (StringUtils.isNotEmpty(reportThree) && reportThree.contains(FONT_RED_TAG)) {
            reportType = reportThree;
        }
        return reportType;
    }

    /**
     * 获取标红研报类型，若都没标红，取第三类研报类型
     *
     * @param jsonItem 条目
     * @return 研报类型
     */
    public static String getRedReportType(JSONObject jsonItem) {
        String reportOne = jsonItem.getString("alg_report_type_one");
        String reportTwo = jsonItem.getString("alg_report_type_two");
        String reportThree = jsonItem.getString("alg_report_type_three");
        String reportType = reportThree;
        if (StringUtils.isNotEmpty(reportOne) && reportOne.contains(FONT_RED_TAG)) {
            reportType = reportOne;
        } else if (StringUtils.isNotEmpty(reportTwo) && reportTwo.contains(FONT_RED_TAG)) {
            reportType = reportTwo;
        } else if (StringUtils.isNotEmpty(reportThree) && reportThree.contains(FONT_RED_TAG)) {
            reportType = reportThree;
        }
        return reportType;
    }

    /**
     * 获取标红研报类型，若都没标红，取第三类研报类型
     *
     * @param jsonItem 条目
     * @return 研报类型
     */
    public static String getRedIndustryType(JSONObject jsonItem) {
        String industryTypeZero = jsonItem.getString("alg_industry_type");
        String industryTypeOne = jsonItem.getString("alg_industry_type_one");
        String industryTypeTwo = jsonItem.getString("alg_industry_type_two");
        String industryTypeThree = jsonItem.getString("alg_industry_type_three");
        String industryType = industryTypeThree;
        if (StringUtils.isNotEmpty(industryTypeZero) && industryTypeZero.contains(FONT_RED_TAG)) {
            industryType = industryTypeZero;
        } else if (StringUtils.isNotEmpty(industryTypeOne) && industryTypeOne.contains(FONT_RED_TAG)) {
            industryType = industryTypeOne;
        } else if (StringUtils.isNotEmpty(industryTypeTwo) && industryTypeTwo.contains(FONT_RED_TAG)) {
            industryType = industryTypeTwo;
        } else if (StringUtils.isNotEmpty(industryTypeThree) && industryTypeThree.contains(FONT_RED_TAG)) {
            industryType = industryTypeThree;
        }
        return industryType;
    }

    public static String getAuthorFromJson(JSONObject itemJson) {
        JSONArray authorJson = itemJson.getJSONArray("alg_analyst_info");
        String authorStr = "";
        if (authorJson == null) {
            return authorStr;
        }
        int size = authorJson.size();
        List<String> authorList = new ArrayList<>();
        String author;
        for (int i = 0; i < size; i++) {
            JSONObject eachAuthor = authorJson.getJSONObject(i);
            if (eachAuthor != null && StringUtils.isNotEmpty(author = eachAuthor.getString("analyst_name"))) {
                authorList.add(author);
            }
        }
        return String.join(",", authorList);
    }
}
