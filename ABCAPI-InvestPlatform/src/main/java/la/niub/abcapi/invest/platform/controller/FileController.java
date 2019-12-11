package la.niub.abcapi.invest.platform.controller;

import la.niub.abcapi.invest.platform.config.enums.FileSourceEnum;
import la.niub.abcapi.invest.platform.model.bo.file.FileBO;
import la.niub.abcapi.invest.platform.model.request.file.FileUploadRequest;
import la.niub.abcapi.invest.platform.model.response.ErrorResponse;
import la.niub.abcapi.invest.platform.model.response.Response;
import la.niub.abcapi.invest.platform.service.IDataService;
import la.niub.abcapi.invest.platform.service.IFileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理
 */
@RestController
@RequestMapping(path = "/file")
public class FileController {

    private final static Logger logger = LogManager.getLogger(FileController.class);

    @Autowired
    private IFileService fileService;

    /**
     * 文件上传
     * @return
     */
    @PostMapping(path = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response uploadFile(FileUploadRequest param) {
        try{
            if (param.getFile() == null){
                return new ErrorResponse("缺少file");
            }
            FileBO fileBO = fileService.upload(param.getUserId(),param.getFile_source(),param.getFile());
            return new Response(fileBO);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }
    }
    @PostMapping(path = "/upload")
    Response uploadFileUrl(@RequestBody FileUploadRequest param) {
        try{
            if (StringUtils.isEmpty(param.getUrl())){
                return new ErrorResponse("缺少url");
            }
            FileBO fileBO = fileService.upload(param.getUserId(),param.getFile_source(),param.getUrl());
            return new Response(fileBO);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ErrorResponse(e.getMessage());
        }

    }

    /**
     * 文件下载
     * @return
     */
    @GetMapping(path = "/{fileId}/{filePath}")
    void download(@PathVariable Long fileId) {
        try{
            fileService.download(fileId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
    @GetMapping(path = "/{fileId}")
    void downloadOnlyId(@PathVariable Long fileId) {
        download(fileId);
    }

    /**
     * 获取文件信息
     * @return
     */
    @GetMapping(path = "/info")
    Response getUrl(@RequestParam("file_id") Long fileId) {
        return new Response(fileService.getFile(fileId));
    }
}
