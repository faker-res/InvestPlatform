package la.niub.abcapi.invest.platform.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class OssService {

//    private OSSClient ossClient;
    private final Logger logger = LoggerFactory.getLogger(OssService.class);


    private final String BUCKET = "md-resource-upload";

//    @Value("oss.endPoint")
    private final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";

//    @Value("oss.accessKeyId")
    private final String ACCESSKEYID = "LTAIQDaphjso2nwb";

//    @Value("oss.accessKeySecret")
    private final String ACCESSKEYSECRET = "9CxlmoNlnoNqlOPW3fyHfzi5sp7YDc";

//    @Value("oss.folder")
    private final String FOLDER = "InvestPlatform/";

    public String uploadFile(InputStream inputStream,String extension,Long fileSize) throws Exception{
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

        String fileName = UUID.randomUUID() + "." + extension;
        String objectId = FOLDER + fileName;
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(extension));
            if (fileSize > 0){
                objectMetadata.setContentLength(fileSize);
                objectMetadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            }else{
                objectMetadata.setContentDisposition("filename=" + fileName);
            }
            //上传文件
            ossClient.putObject(BUCKET, objectId, inputStream, objectMetadata);
            ossClient.setObjectAcl(BUCKET, objectId, CannedAccessControlList.PublicRead);
            String url = getUrl(ossClient, objectId);
            logger.info(url);
//            return url;
            return objectId;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        } finally {
            ossClient.shutdown();
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    //获取地址
    private String getUrl(OSSClient ossClient, String key) {
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        URL url = ossClient.generatePresignedUrl(BUCKET, key, expiration);
        if (url != null) {
            String urlStr = url.toString();
            return urlStr.substring(0, urlStr.indexOf("?"));
        }

        return null;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileExtension){
        //文件的后缀名
        if("bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if("gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)  || "png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if("html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if("txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if("vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if ("xls".equalsIgnoreCase(fileExtension) || "xlsx".equalsIgnoreCase(fileExtension)) {
            return "application/msexcel";
        }
        if ("csv".equalsIgnoreCase(fileExtension)) {
            return "application/csv";
        }
        if("xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }
}
