package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 解析出的文本
 */
@Data
public class MailReportTextResponse implements Serializable {
    //主键
    private Long id;

    //解析出的标题
    private String title;

    //解析出的作者
    private String author;

    //文件id
    private String fileId;

    //文档标识
    private MailReportAlgDocumentTagsResponse algDocumentTags;

    //打标签完成的版本
    private Integer forecastExtractionVer;

    //打标签完成的时间
    private Date algDocumentTagsTime;

    //文件额外数据,在file， text, chart表中都会包含该字段
    private Map<String, Object> fileData;

    private String htmlFile;

    private String textFile;

    private String jsonFile;

    private String createTime;

    private TagGainedStatusEnum tagGainedStatus;
}
