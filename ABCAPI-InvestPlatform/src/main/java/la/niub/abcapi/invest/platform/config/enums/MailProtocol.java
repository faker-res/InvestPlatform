package la.niub.abcapi.invest.platform.config.enums;

public enum MailProtocol {

    POP3("Pop3协议",110,995),
    EXCHANGE("Exchange协议",80,443),
    IMAP("Imap协议",143,993),
    SMTP("Smtp协议",25,465);

    private String description;

    private Integer defaultPort;

    private Integer defaultSslPort;

    MailProtocol(String description, Integer defaultPort, Integer defaultSslPort) {
        this.description = description;
        this.defaultPort = defaultPort;
        this.defaultSslPort = defaultSslPort;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDefaultPort() {
        return defaultPort;
    }

    public Integer getDefaultSslPort() {
        return defaultSslPort;
    }
}
