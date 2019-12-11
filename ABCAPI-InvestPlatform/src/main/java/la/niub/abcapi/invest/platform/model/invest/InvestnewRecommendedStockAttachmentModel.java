package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.util.Date;

public class InvestnewRecommendedStockAttachmentModel implements Serializable {
    private Integer id;

    private String user_id;

    private String broker;

    private String file_url;

    private Date push_date;

    private String status;

    private Date create_time;

    private static final long serialVersionUID = 1L;

    public InvestnewRecommendedStockAttachmentModel(Integer id, String user_id, String broker, String file_url, Date push_date, String status, Date create_time) {
        this.id = id;
        this.user_id = user_id;
        this.broker = broker;
        this.file_url = file_url;
        this.push_date = push_date;
        this.status = status;
        this.create_time = create_time;
    }

    public InvestnewRecommendedStockAttachmentModel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id == null ? null : user_id.trim();
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker == null ? null : broker.trim();
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url == null ? null : file_url.trim();
    }

    public Date getPush_date() {
        return push_date;
    }

    public void setPush_date(Date push_date) {
        this.push_date = push_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", broker=").append(broker);
        sb.append(", file_url=").append(file_url);
        sb.append(", push_date=").append(push_date);
        sb.append(", status=").append(status);
        sb.append(", create_time=").append(create_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}