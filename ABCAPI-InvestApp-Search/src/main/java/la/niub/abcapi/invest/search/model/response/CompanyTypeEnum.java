package la.niub.abcapi.invest.search.model.response;

public enum CompanyTypeEnum {
    BROKER("券商"),
    FUND("基金"),;

    private String chineseName;

    CompanyTypeEnum(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
	}
}