package la.niub.abcapi.invest.platform.model.request.file;

import la.niub.abcapi.invest.platform.config.enums.FileSourceEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {

    private MultipartFile file;

    private String url;

    private String userId;

    private FileSourceEnum file_source = FileSourceEnum.OSS;
}
