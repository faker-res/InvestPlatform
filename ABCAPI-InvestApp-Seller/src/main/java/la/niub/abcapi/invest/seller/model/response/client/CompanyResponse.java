package la.niub.abcapi.invest.seller.model.response.client;


import la.niub.abcapi.invest.seller.config.enums.CompanyTypeEnum;

public class CompanyResponse {

    private Long company_id;

    private String sname;

    private String fullname;

    private CompanyTypeEnum type;

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public CompanyTypeEnum getType() {
        return type;
    }

    public void setType(CompanyTypeEnum type) {
        this.type = type;
    }
}
