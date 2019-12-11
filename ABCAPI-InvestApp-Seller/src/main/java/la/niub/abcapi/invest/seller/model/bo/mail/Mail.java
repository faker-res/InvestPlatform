package la.niub.abcapi.invest.seller.model.bo.mail;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Data
@ToString(exclude = "detail")
public class Mail {

    private String uuid;

    private String userId;

    private String mail;

    private String folder;

    private String sender;

    private String title;

    private String detail;

    private Date time;

    private String contentUrl;

    private Map<String,String> images;

    private Map<String,String> attachments;
}
