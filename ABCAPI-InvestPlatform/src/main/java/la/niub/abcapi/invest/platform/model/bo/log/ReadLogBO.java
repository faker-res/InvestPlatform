package la.niub.abcapi.invest.platform.model.bo.log;

import la.niub.abcapi.invest.platform.config.enums.ObjectTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReadLogBO {

    private String userId;

    private Long companyId;

    private String title;

    private String objectId;

    private ObjectTypeEnum objectType;

    private List<String> author;

    private String stockCode;

    private String stockName;

    private Date readTime;
}
