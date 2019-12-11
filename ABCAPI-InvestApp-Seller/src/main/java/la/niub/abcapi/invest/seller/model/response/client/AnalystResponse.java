package la.niub.abcapi.invest.seller.model.response.client;

public class AnalystResponse {

    private Long peo_uni_code;

    private String name;

    private String analyst_code;

    private CompanyResponse company;

    public Long getPeo_uni_code() {
        return peo_uni_code;
    }

    public void setPeo_uni_code(Long peo_uni_code) {
        this.peo_uni_code = peo_uni_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnalyst_code() {
        return analyst_code;
    }

    public void setAnalyst_code(String analyst_code) {
        this.analyst_code = analyst_code;
    }

    public CompanyResponse getCompany() {
        return company;
    }

    public void setCompany(CompanyResponse company) {
        this.company = company;
    }
}
