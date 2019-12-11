package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvestnewStockIndexModel implements Serializable {
    private Long id;

    private Long sec_uni_code;

    private String stock_code;

    private String stock_name;

    private BigDecimal gold_rate;

    private BigDecimal industry_rate;

    private BigDecimal month_begin_price;

    private BigDecimal month_end_price;

    private String push_month;

    private static final long serialVersionUID = 1L;

    public InvestnewStockIndexModel(Long id, Long sec_uni_code, String stock_code, String stock_name, BigDecimal gold_rate, BigDecimal industry_rate, BigDecimal month_begin_price, BigDecimal month_end_price, String push_month) {
        this.id = id;
        this.sec_uni_code = sec_uni_code;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.gold_rate = gold_rate;
        this.industry_rate = industry_rate;
        this.month_begin_price = month_begin_price;
        this.month_end_price = month_end_price;
        this.push_month = push_month;
    }

    public InvestnewStockIndexModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSec_uni_code() {
        return sec_uni_code;
    }

    public void setSec_uni_code(Long sec_uni_code) {
        this.sec_uni_code = sec_uni_code;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code == null ? null : stock_code.trim();
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name == null ? null : stock_name.trim();
    }

    public BigDecimal getGold_rate() {
        return gold_rate;
    }

    public void setGold_rate(BigDecimal gold_rate) {
        this.gold_rate = gold_rate;
    }

    public BigDecimal getIndustry_rate() {
        return industry_rate;
    }

    public void setIndustry_rate(BigDecimal industry_rate) {
        this.industry_rate = industry_rate;
    }

    public BigDecimal getMonth_begin_price() {
        return month_begin_price;
    }

    public void setMonth_begin_price(BigDecimal month_begin_price) {
        this.month_begin_price = month_begin_price;
    }

    public BigDecimal getMonth_end_price() {
        return month_end_price;
    }

    public void setMonth_end_price(BigDecimal month_end_price) {
        this.month_end_price = month_end_price;
    }

    public String getPush_month() {
        return push_month;
    }

    public void setPush_month(String push_month) {
        this.push_month = push_month == null ? null : push_month.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sec_uni_code=").append(sec_uni_code);
        sb.append(", stock_code=").append(stock_code);
        sb.append(", stock_name=").append(stock_name);
        sb.append(", gold_rate=").append(gold_rate);
        sb.append(", industry_rate=").append(industry_rate);
        sb.append(", month_begin_price=").append(month_begin_price);
        sb.append(", month_end_price=").append(month_end_price);
        sb.append(", push_month=").append(push_month);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}