package la.niub.abcapi.invest.seller.model.response;

/**
 * @author : ppan
 * @description : 券商评分设置评价对象列表
 * @date : 2019-01-22 09:34
 */
public class TraderMarkConfigObjectResponse {

    private Long id;

    private String broker;

    private String industry;

    private String analyst;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAnalyst() {
        return analyst;
    }

    public void setAnalyst(String analyst) {
        this.analyst = analyst;
    }
}
