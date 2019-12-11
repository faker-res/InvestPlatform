package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.util.Date;

public class InvestnewReadHistoryModel implements Serializable {
    private Long id;

    private String user_id;

    private Long company_id;

    private String title;

    private String object_id;

    private String object_type;

    private String author;

    private String stock_code;

    private String stock_name;

    private Date create_time;

    private Date update_time;

    private String data;

    private static final long serialVersionUID = 1L;

    public InvestnewReadHistoryModel(Long id, String user_id, Long company_id, String title, String object_id, String object_type, String author, String stock_code, String stock_name, Date create_time, Date update_time) {
        this.id = id;
        this.user_id = user_id;
        this.company_id = company_id;
        this.title = title;
        this.object_id = object_id;
        this.object_type = object_type;
        this.author = author;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public InvestnewReadHistoryModel(Long id, String user_id, Long company_id, String title, String object_id, String object_type, String author, String stock_code, String stock_name, Date create_time, Date update_time, String data) {
        this.id = id;
        this.user_id = user_id;
        this.company_id = company_id;
        this.title = title;
        this.object_id = object_id;
        this.object_type = object_type;
        this.author = author;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.create_time = create_time;
        this.update_time = update_time;
        this.data = data;
    }

    public InvestnewReadHistoryModel() {
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

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id == null ? null : object_id.trim();
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type == null ? null : object_type.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", company_id=").append(company_id);
        sb.append(", title=").append(title);
        sb.append(", object_id=").append(object_id);
        sb.append(", object_type=").append(object_type);
        sb.append(", author=").append(author);
        sb.append(", stock_code=").append(stock_code);
        sb.append(", stock_name=").append(stock_name);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", data=").append(data);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}