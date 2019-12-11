package la.niub.abcapi.invest.seller.service.base;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import la.niub.abcapi.invest.seller.config.enums.OssType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class OssService {

    private static final Logger logger = LoggerFactory.getLogger(OssService.class);

    @Value("${oss.md-resource.bucket}")
    private String mdResourceBucket;

    @Value("${oss.md-resource.endPoint}")
    private String mdResourceEndPoint;

    @Value("${oss.md-resource.accessKeyId}")
    private String mdResourceAccessKeyId;

    @Value("${oss.md-resource.accessKeySecret}")
    private String mdResourceAccessKeySecret;

    @Value("${oss.abc-crawler.bucket}")
    private String abcCrawlerBucket;

    @Value("${oss.abc-crawler.endPoint}")
    private String abcCrawlerEndPoint;

    @Value("${oss.abc-crawler.accessKeyId}")
    private String abcCrawlerAccessKeyId;

    @Value("${oss.abc-crawler.accessKeySecret}")
    private String abcCrawlerAccessKeySecret;

    @Value("${oss.abc-parsing.bucket}")
    private String abcParsingBucket;

    @Value("${oss.abc-parsing.endPoint}")
    private String abcParsingEndPoint;

    @Value("${oss.abc-parsing.accessKeyId}")
    private String abcParsingAccessKeyId;

    @Value("${oss.abc-parsing.accessKeySecret}")
    private String abcParsingAccessKeySecret;

    @Value("${oss.invest-report.bucket}")
    private String investReportBucket;

    @Value("${oss.invest-report.endPoint}")
    private String investReportEndPoint;

    @Value("${oss.invest-report.accessKeyId}")
    private String investReportAccessKeyId;

    @Value("${oss.invest-report.accessKeySecret}")
    private String investReportAccessKeySecret;

    private OSSClient ossClient;

    private OSSClient getClient(OssType ossType) {
        String endPoint;
        String accessKeyId;
        String accessKeySecret;
        if (this.ossClient == null) {
            switch (ossType) {
                case MDRESCOURE:
                    endPoint = mdResourceEndPoint;
                    accessKeyId = mdResourceAccessKeyId;
                    accessKeySecret = mdResourceAccessKeySecret;
                    break;
                case ABCCRAWLER:
                    endPoint = abcCrawlerEndPoint;
                    accessKeyId = abcCrawlerAccessKeyId;
                    accessKeySecret = abcCrawlerAccessKeySecret;
                    break;
                case ABCPARSING:
                    endPoint = abcParsingEndPoint;
                    accessKeyId = abcParsingAccessKeyId;
                    accessKeySecret = abcParsingAccessKeySecret;
                    break;
                case INVESTREPORT:
                    endPoint = investReportEndPoint;
                    accessKeyId = investReportAccessKeyId;
                    accessKeySecret = investReportAccessKeySecret;
                    break;
                default:
                    throw new RuntimeException("研报存储的文件空间找不到");
            }
            // 创建ClientConfiguration实例，按照您的需要修改默认参数
            ClientConfiguration conf = new ClientConfiguration();
            // 开启支持CNAME选项
            conf.setSupportCname(true);
            // 需要开启，默认不开启
            conf.setRequestTimeoutEnabled(true);
            this.ossClient = new OSSClient(
                    endPoint,
                    accessKeyId,
                    accessKeySecret,
                    conf
            );
        }
        return this.ossClient;
    }

    public InputStream getFile(String key, OssType ossType) throws Exception {
        // 下载Object到文件。
        String bucket;
        switch (ossType) {
            case MDRESCOURE:
                bucket = mdResourceBucket;
                break;
            case ABCCRAWLER:
                bucket = abcCrawlerBucket;
                break;
            case ABCPARSING:
                bucket = abcParsingBucket;
                break;
            case INVESTREPORT:
                bucket = investReportBucket;
                break;
            default:
                throw new RuntimeException("研报存储的文件空间找不到");
        }
        OSSObject ossObject = this.getClient(ossType).getObject(bucket, key);
        return ossObject.getObjectContent();
    }
}
