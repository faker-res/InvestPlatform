package la.niub.abcapi.invest.platform.model.invest;

import java.io.Serializable;
import java.util.Date;

public class InvestnewFileModel implements Serializable {
    private Long id;

    private String user_id;

    private String source;

    private String path;

    private String extension;

    private Long filesize;

    private Date create_time;

    private Date update_time;

    private static final long serialVersionUID = 1L;

    public InvestnewFileModel(Long id, String user_id, String source, String path, String extension, Long filesize, Date create_time, Date update_time) {
        this.id = id;
        this.user_id = user_id;
        this.source = source;
        this.path = path;
        this.extension = extension;
        this.filesize = filesize;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public InvestnewFileModel() {
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension == null ? null : extension.trim();
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", source=").append(source);
        sb.append(", path=").append(path);
        sb.append(", extension=").append(extension);
        sb.append(", filesize=").append(filesize);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}