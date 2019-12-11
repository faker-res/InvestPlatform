package la.niub.abcapi.invest.seller.config.enums;

/**
 * @author : ppan
 * @description : 季度
 * @date : 2019-01-28 17:14
 */
public enum QuarterDateEnum {
    FIRST_QUARTER(1, "01-01 00:00:00.000", "03-31 23:59:59.999"),
    SECOND_QUARTER(2, "04-01 00:00:00.000", "06-30 23:59:59.999"),
    THIRD_QUARTER(3, "07-01 00:00:00.000", "09-30 23:59:59.999"),
    FOURTH_QUARTER(4, "10-01 00:00:00.000", "12-31 23:59:59.999");

    private Integer quarter;
    private String startDate;
    private String endDate;

    private QuarterDateEnum(Integer quarter, String startDate, String endDate) {
        this.quarter = quarter;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //处理季度
    public static QuarterDateEnum getQuarterDateEnumByQuarter(Integer quarter) {
        QuarterDateEnum[] quarters = values();
        for (QuarterDateEnum q : quarters) {
            if (q.getQuarter().equals(quarter)) {
                return q;
            }
        }

        return null;
    }
}
