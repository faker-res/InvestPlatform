package la.niub.abcapi.invest.platform.model.bo.file;

import lombok.Data;

import java.util.Date;

@Data
public class FileBO {

    private Long fileId;

    private String userId;

    private Long fileSize;

    private String url;

    private Date createTime;
}
