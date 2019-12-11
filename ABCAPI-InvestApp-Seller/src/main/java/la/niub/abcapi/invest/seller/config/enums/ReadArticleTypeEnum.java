package la.niub.abcapi.invest.seller.config.enums;

public enum ReadArticleTypeEnum {

    MAIL_REPORT("mail_report"),
    UPLOAD_REPORT("upload_report");

    private String type;

    ReadArticleTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
