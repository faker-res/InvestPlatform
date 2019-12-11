package la.niub.abcapi.invest.seller.model.response.client;

import java.util.List;

/**
 * @author : ppan
 * @description : 公网券商返回值
 * @date : 2019-01-24 10:06
 */
public class BrokerResponse {
    private List<CompanyResponse> data;

    public List<CompanyResponse> getData() {
        return data;
    }

    public void setData(List<CompanyResponse> data) {
        this.data = data;
    }
}
