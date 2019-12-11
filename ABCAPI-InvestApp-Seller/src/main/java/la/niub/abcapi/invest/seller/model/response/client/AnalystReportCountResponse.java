package la.niub.abcapi.invest.seller.model.response.client;

public class AnalystReportCountResponse {

    private Long count;

    private String name;

    private AnalystResponse analyst;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnalystResponse getAnalyst() {
        return analyst;
    }

    public void setAnalyst(AnalystResponse analyst) {
        this.analyst = analyst;
    }
}
