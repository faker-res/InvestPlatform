package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.util.Date;

public class InvestnewRecommendedStockRuleModel implements Serializable {
    private Long id;

    private String broker;

    private String sheet_name;

    private String parent_id;

    private Date create_time;

    private Date update_time;

    private String create_id;

    private String update_id;

    private static final long serialVersionUID = 1L;

    public InvestnewRecommendedStockRuleModel(Long id, String broker, String sheet_name, String parent_id, Date create_time, Date update_time, String create_id, String update_id) {
        this.id = id;
        this.broker = broker;
        this.sheet_name = sheet_name;
        this.parent_id = parent_id;
        this.create_time = create_time;
        this.update_time = update_time;
        this.create_id = create_id;
        this.update_id = update_id;
    }

    public InvestnewRecommendedStockRuleModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getSheet_name() {
        return sheet_name;
    }

    public void setSheet_name(String sheet_name) {
        this.sheet_name = sheet_name == null ? null : sheet_name.trim();
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id == null ? null : parent_id.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getCreate_id() {
        return create_id;
    }

    public void setCreate_id(String create_id) {
        this.create_id = create_id == null ? null : create_id.trim();
    }

    public String getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(String update_id) {
        this.update_id = update_id == null ? null : update_id.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", broker=").append(broker);
        sb.append(", sheet_name=").append(sheet_name);
        sb.append(", parent_id=").append(parent_id);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", create_id=").append(create_id);
        sb.append(", update_id=").append(update_id);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}