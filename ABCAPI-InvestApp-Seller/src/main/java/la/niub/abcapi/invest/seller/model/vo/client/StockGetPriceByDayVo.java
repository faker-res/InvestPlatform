package la.niub.abcapi.invest.seller.model.vo.client;

import java.util.Date;
import java.util.List;

public class StockGetPriceByDayVo {

    private List<Long> secUniCodes;

    private Long secUniCode;

    private Date day;

    public List<Long> getSecUniCodes() {
        return secUniCodes;
    }

    public void setSecUniCodes(List<Long> secUniCodes) {
        this.secUniCodes = secUniCodes;
    }

    public Long getSecUniCode() {
        return secUniCode;
    }

    public void setSecUniCode(Long secUniCode) {
        this.secUniCode = secUniCode;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
}
