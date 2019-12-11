package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.util.Date;

public class InvestnewCommentModel implements Serializable {
    private Integer id;

    private String user_id;

    private String object_id;

    private String object_type;

    private Integer reply_id;

    private Date create_time;

    private Date update_time;

    private String content;

    private static final long serialVersionUID = 1L;

    public InvestnewCommentModel(Integer id, String user_id, String object_id, String object_type, Integer reply_id, Date create_time, Date update_time) {
        this.id = id;
        this.user_id = user_id;
        this.object_id = object_id;
        this.object_type = object_type;
        this.reply_id = reply_id;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public InvestnewCommentModel(Integer id, String user_id, String object_id, String object_type, Integer reply_id, Date create_time, Date update_time, String content) {
        this.id = id;
        this.user_id = user_id;
        this.object_id = object_id;
        this.object_type = object_type;
        this.reply_id = reply_id;
        this.create_time = create_time;
        this.update_time = update_time;
        this.content = content;
    }

    public InvestnewCommentModel() {
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

    public Integer getReply_id() {
        return reply_id;
    }

    public void setReply_id(Integer reply_id) {
        this.reply_id = reply_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", object_id=").append(object_id);
        sb.append(", object_type=").append(object_type);
        sb.append(", reply_id=").append(reply_id);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}