package la.niub.abcapi.invest.seller.model.response.client;

import java.util.List;

public class AccountListResponse {

    private Integer total;

    private List<AccountInfoResponse> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AccountInfoResponse> getList() {
        return list;
    }

    public void setList(List<AccountInfoResponse> list) {
        this.list = list;
    }
}
