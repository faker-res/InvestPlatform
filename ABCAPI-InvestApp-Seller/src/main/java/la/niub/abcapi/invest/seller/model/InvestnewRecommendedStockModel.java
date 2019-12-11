package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.util.Date;

public class InvestnewRecommendedStockModel implements Serializable {
    private Long id;

    private String user_id;

    private String parent_id;

    private Long sec_uni_code;

    private String stock_code;

    private String stock_name;

    private String industry;

    private String broker;

    private String push_month;

    private Date push_time;

    private String recommended_reason;

    private static final long serialVersionUID = 1L;

    public InvestnewRecommendedStockModel(Long id, String user_id, String parent_id, Long sec_uni_code, String stock_code, String stock_name, String industry, String broker, String push_month, Date push_time) {
        this.id = id;
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.sec_uni_code = sec_uni_code;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.industry = industry;
        this.broker = broker;
        this.push_month = push_month;
        this.push_time = push_time;
    }

    public InvestnewRecommendedStockModel(Long id, String user_id, String parent_id, Long sec_uni_code, String stock_code, String stock_name, String industry, String broker, String push_month, Date push_time, String recommended_reason) {
        this.id = id;
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.sec_uni_code = sec_uni_code;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.industry = industry;
        this.broker = broker;
        this.push_month = push_month;
        this.push_time = push_time;
        this.recommended_reason = recommended_reason;
    }

    public InvestnewRecommendedStockModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id == null ? null : user_id.trim();
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id == null ? null : parent_id.trim();
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getPush_month() {
        return push_month;
    }

    public void setPush_month(String push_month) {
        this.push_month = push_month == null ? null : push_month.trim();
    }

    public Date getPush_time() {
        return push_time;
    }

    public void setPush_time(Date push_time) {
        this.push_time = push_time;
    }

    public String getRecommended_reason() {
        return recommended_reason;
    }

    public void setRecommended_reason(String recommended_reason) {
        this.recommended_reason = recommended_reason == null ? null : recommended_reason.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", parent_id=").append(parent_id);
        sb.append(", sec_uni_code=").append(sec_uni_code);
        sb.append(", stock_code=").append(stock_code);
        sb.append(", stock_name=").append(stock_name);
        sb.append(", industry=").append(industry);
        sb.append(", broker=").append(broker);
        sb.append(", push_month=").append(push_month);
        sb.append(", push_time=").append(push_time);
        sb.append(", recommended_reason=").append(recommended_reason);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}