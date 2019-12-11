package la.niub.abcapi.invest.seller.model.vo;

import la.niub.abcapi.invest.seller.component.util.DateUtil;
import lombok.Data;

import java.util.List;

@Data
public class SearchWordVo {

    private String after;

    private String before;

    private List<String> analyst;

    private List<String> company;

    private List<String> from;

    private List<String> industry;

    private List<String> keyword;

    private List<String> organization;

    private List<String> rate;

    private List<String> report;

    private List<String> subject;

    private List<String> to;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;


    public long getEndTime() {
        String endDateTime = DateUtil.fillEndTime4Date(before);
        endTime = DateUtil.getSecondsByDateTime(endDateTime);
        if (endTime == 0) {
            return System.currentTimeMillis() / 1000;
        }
        return endTime;
    }


    public long getStartTime() {
        String startDateTime = DateUtil.fillTime4Date(after);
        startTime = DateUtil.getSecondsByDateTime(startDateTime);
        return startTime;
    }


    public String getKeywordStr() {
        if (keyword == null || keyword.size() == 0) {
            return "";
        }
        if (keyword.size() == 1) {
            return keyword.get(0);
        }
        return String.join(" ", keyword);
    }

    public String getFromStr() {
        if (from == null || from.size() == 0) {
            return "";
        }
        return getBlankList(from);
    }

    public String getToStr() {
        if (to == null || to.size() == 0) {
            return "";
        }
        return getBlankList(to);
    }

    public String getRateStr() {
        if (rate == null || rate.size() == 0) {
            return "";
        }
        return getBlankList(rate);
    }

    public String getOrganizationStr() {
        if (organization == null || organization.size() == 0) {
            return "";
        }
        return getBlankList(organization);
    }

    public String getReportTypeStr() {
        if (report == null || report.size() == 0) {
            return "";
        }
        return String.join(" OR ", report);
    }

    public String getCompanyStr() {
        if (company == null || company.size() == 0) {
            return "";
        }
        return getBlankList(company);

    }

    public String getIndustryStr() {
        if (industry == null || industry.size() == 0) {
            return "";
        }
        return getBlankList(industry);
    }

    public String getAnalystStr() {
        if (analyst == null || analyst.size() == 0) {
            return "";
        }
        return getBlankList(analyst);
    }

    private String getBlankList(List<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.set(i, "\"" + list.get(i) + "\"");
        }
        return String.join(" OR ", list);
    }

}
