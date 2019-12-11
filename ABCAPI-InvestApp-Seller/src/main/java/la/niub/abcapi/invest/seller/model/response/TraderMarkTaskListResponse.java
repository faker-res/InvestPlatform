package la.niub.abcapi.invest.seller.model.response;

import java.math.BigDecimal;

/**
 * @author : ppan
 * @description : 券商评分列表response
 * @date : 2019-01-26 16:27
 */
public class TraderMarkTaskListResponse {

    private Long id;

    private Integer year;

    private Integer quarter;

    /**
     * 0：未完成  1：已完成
     */
    private Integer status;

    /**
     * 整个评分任务进度
     */
    private BigDecimal progressRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(BigDecimal progressRate) {
        this.progressRate = progressRate;
    }
}
