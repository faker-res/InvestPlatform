package la.niub.abcapi.invest.seller.model.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : ppan
 * @description : 券商评分券商列表response
 * @date : 2019-01-26 16:27
 */
public class TraderMarkTaskBrokerResponse {

    private String broker;

    private Integer reportCount;

    private Integer readCount;

    private Integer roadShowCount;

    private BigDecimal attainDays;

    private BigDecimal goldStockRate;

    private List<Map<String, Object>> dimensions;

    private BigDecimal totalScore;

    /**
     * 状态 1：已评价 0：未评价
     */
    private Integer status;

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getRoadShowCount() {
        return roadShowCount;
    }

    public void setRoadShowCount(Integer roadShowCount) {
        this.roadShowCount = roadShowCount;
    }

    public BigDecimal getAttainDays() {
        return attainDays;
    }

    public void setAttainDays(BigDecimal attainDays) {
        this.attainDays = attainDays;
    }

    public BigDecimal getGoldStockRate() {
        return goldStockRate;
    }

    public void setGoldStockRate(BigDecimal goldStockRate) {
        this.goldStockRate = goldStockRate;
    }

    public List<Map<String, Object>> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Map<String, Object>> dimensions) {
        this.dimensions = dimensions;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
