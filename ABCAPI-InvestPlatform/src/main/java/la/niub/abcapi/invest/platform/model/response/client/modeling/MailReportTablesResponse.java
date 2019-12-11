package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

/**
 * @author : ppan
 * @description : 解析出的图表
 * @date : 2019-02-23 11:30
 */
@Data
public class MailReportTablesResponse {

    //主键
    private String id;

    // 文件id
    private Long fileId;

    // 图片地址
    private String pngFile;

    // 文件地址
    private String fileUrl;

    // 图表数据地址
    private String dataFile;

    // 标题
    private String title;
}
