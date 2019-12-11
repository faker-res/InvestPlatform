package la.niub.abcapi.invest.seller.config.enums;

public enum OssType {
    MDRESCOURE("md-resource"),
    ABCCRAWLER("abc-crawler"),
    ABCPARSING("abc-parsing"),
    INVESTREPORT("invest-report");

    private String ossType;

    OssType(String ossType) {
        this.ossType = ossType;
    }

    public String getOssType() {
        return ossType;
    }
}
