package la.niub.abcapi.invest.platform.service.impl;

import la.niub.abcapi.invest.platform.config.enums.ContentTypeEnum;
import la.niub.abcapi.invest.platform.config.enums.FileSourceEnum;
import la.niub.abcapi.invest.platform.dao.invest.IInvestnewFileDao;
import la.niub.abcapi.invest.platform.model.bo.file.FileBO;
import la.niub.abcapi.invest.platform.model.invest.InvestnewFileModel;
import la.niub.abcapi.invest.platform.service.IFileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {

    private final static Logger logger = LogManager.getLogger(FileServiceImpl.class);

    @Autowired
    HttpServletResponse response;

    @Autowired
    private OssService ossService;

    @Autowired
    private IInvestnewFileDao investnewFileDao;

    @Value("${file.oss.domain}")
    private String ossDomain;

    @Value("${file.local.rootPath}")
    private String localRootPath;

    @Value("${file.local.domain}")
    private String localDomain;

    @Override
    public FileBO upload(String userId,FileSourceEnum fileSource, MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String extension = fileName.contains(".")? fileName.substring(fileName.lastIndexOf(".")+1) : null;
        String path = upload(fileSource,
                file.getInputStream(),extension,file.getSize());
        if (StringUtils.isEmpty(path)){
            return null;
        }
        return addFile(userId,fileSource,path,extension,file.getSize());
    }

    @Override
    public FileBO upload(String userId,FileSourceEnum fileSource, String fileUrl) throws Exception {
        if (!fileUrl.contains("http://") && !fileUrl.contains("https://")){
            return null;
        }
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        String extension = fileName.contains(".")? fileName.substring(fileName.lastIndexOf(".")+1) : null;

        InputStream inputStream = null;
        Long fileSize = null;
        try {
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3 * 60 * 1000);
            inputStream = conn.getInputStream();
            fileSize = conn.getContentLengthLong()>0 ? conn.getContentLengthLong() : null;
        } catch (IOException e) {
            try {
                URL url = new URL(fileUrl);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3 * 60 * 1000);
                inputStream = conn.getInputStream();
                fileSize = conn.getContentLengthLong()>0 ? conn.getContentLengthLong() : null;
            } catch (IOException e2) {
                throw new Exception("获取在线文件出错",e2);
            }
        }

        String path = upload(fileSource,
                inputStream,extension,fileSize);
        if (StringUtils.isEmpty(path)){
            return null;
        }
        return addFile(userId,fileSource,path,extension,fileSize);
    }

    private String upload(FileSourceEnum fileSource, InputStream inputStream, String extension, Long fileSize) throws Exception {
        String path = null;

        if (fileSource.equals(FileSourceEnum.OSS)){
            path = ossService.uploadFile(inputStream,extension,fileSize);
        }else if (fileSource.equals(FileSourceEnum.LOCAL)){
            String fileName = UUID.randomUUID() + "." + extension;
            String destFilePath = localRootPath+fileName;
            File file = new File(destFilePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
            inputStream.close();
            path = fileName;
        }
        return path;
    }

    /**
     * 添加文件
     * @return
     */
    private FileBO addFile(String userId, FileSourceEnum fileSource, String path,String extension, Long fileSize) {
        InvestnewFileModel investnewFileModel = new InvestnewFileModel();
        investnewFileModel.setUser_id(userId);
        investnewFileModel.setSource(fileSource.name().toLowerCase());
        investnewFileModel.setPath(path);
        investnewFileModel.setExtension(extension);
        investnewFileModel.setFilesize(fileSize);
        Boolean result = investnewFileDao.insertSelective(investnewFileModel) > 0;
        if (!result){
            return null;
        }
        return getFile(investnewFileModel.getId());
    }

    @Override
    public FileBO getFile(Long fileId) {
        InvestnewFileModel investnewFileModel = investnewFileDao.selectByPrimaryKey(fileId);
        if (investnewFileModel == null){
            return null;
        }
        FileBO fileBO = new FileBO();
        fileBO.setFileId(investnewFileModel.getId());
        fileBO.setUserId(investnewFileModel.getUser_id());
        fileBO.setFileSize(investnewFileModel.getFilesize());
        fileBO.setCreateTime(investnewFileModel.getCreate_time());

        String url = null;
        try{
            FileSourceEnum fileSource = FileSourceEnum.valueOf(investnewFileModel.getSource().toUpperCase());
            if (fileSource.equals(FileSourceEnum.OSS)){
                url = ossDomain+investnewFileModel.getPath();
            }else if (fileSource.equals(FileSourceEnum.LOCAL)){
                url = localDomain+"file/"+investnewFileModel.getId()+"/"+investnewFileModel.getPath();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        fileBO.setUrl(url);

        return fileBO;
    }

    @Override
    public Boolean delete(Long fileId) {
        Boolean result = investnewFileDao.deleteByPrimaryKey(fileId) > 0;
        return result;
    }

    @Override
    public void download(Long fileId) throws Exception {
        InvestnewFileModel investnewFileModel = investnewFileDao.selectByPrimaryKey(fileId);
        if (investnewFileModel == null){
            return;
        }

        FileSourceEnum fileSource = null;
        try{
            fileSource = FileSourceEnum.valueOf(investnewFileModel.getSource().toUpperCase());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return;
        }

        ContentTypeEnum contentTypeEnum = null;
        try{
            contentTypeEnum = ContentTypeEnum.valueOf(investnewFileModel.getExtension().toUpperCase());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            contentTypeEnum = ContentTypeEnum.JPG;
        }

        if (fileSource.equals(FileSourceEnum.LOCAL)){
            String filePath = localRootPath+investnewFileModel.getPath();
            FileInputStream fileInputStream = new FileInputStream(filePath);

//            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName,"UTF-8"));
//            response.setHeader("Content-Transfer-Encoding","binary");
//            response.setHeader("Expires","0");
//            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
//            response.setHeader("Pragma","public");
            String fileName = URLEncoder.encode(investnewFileModel.getPath(), "UTF-8");
            response.setHeader("Content-Disposition", "filename/filesize="+fileName+"/"+investnewFileModel.getFilesize()+"Byte.");
            response.setHeader("Cache-Control","no-cache");
            response.setContentType(contentTypeEnum.getContenType());
            if (investnewFileModel.getFilesize() != null && investnewFileModel.getFilesize() > 0){
                response.setHeader("Content-Length",investnewFileModel.getFilesize().toString());
            }

            OutputStream outputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        }
    }
}
