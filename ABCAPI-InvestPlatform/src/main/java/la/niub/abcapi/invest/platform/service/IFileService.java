package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.config.enums.FileSourceEnum;
import la.niub.abcapi.invest.platform.model.bo.analyst.AnalystBO;
import la.niub.abcapi.invest.platform.model.bo.data.CompanyBO;
import la.niub.abcapi.invest.platform.model.bo.data.IndustryBO;
import la.niub.abcapi.invest.platform.model.bo.file.FileBO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 文件管理
 */
public interface IFileService {

    /**
     * 上传文件到数据源
     * @param fileSource
     * @return
     */
    FileBO upload(String userId,FileSourceEnum fileSource, MultipartFile file) throws Exception;
    FileBO upload(String userId,FileSourceEnum fileSource, String fileUrl) throws Exception;

    /**
     * 获取文件信息
     * @return
     */
    FileBO getFile(Long fileId);

    /**
     * 删除文件
     * @param fileId
     * @return
     */
    Boolean delete(Long fileId);

    /**
     * 文件下载
     * @param fileId
     * @return
     */
    void download(Long fileId) throws Exception;
}
