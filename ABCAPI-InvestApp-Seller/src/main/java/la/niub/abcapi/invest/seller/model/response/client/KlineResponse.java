package la.niub.abcapi.invest.seller.model.response.client;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class KlineResponse {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date trade_date;

    private BigDecimal turn;

    private BigDecimal differ_range;

    private BigDecimal amount;

    private String amount_unit = "元";

    private BigDecimal close_price;

    private String close_price_unit = "元";

    private BigDecimal differ;

    private String differ_unit = "元";

    private BigDecimal high;

    private String high_unit = "元";

    private BigDecimal low;

    private String low_unit = "元";

    private BigDecimal open;

    private String open_unit = "元";

    private BigDecimal volume;

    private String volume_unit = "股";

    private BigDecimal amplitude;

    public Date getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(Date trade_date) {
        this.trade_date = trade_date;
    }

    public BigDecimal getTurn() {
        return turn;
    }

    public void setTurn(BigDecimal turn) {
        this.turn = turn;
    }

    public BigDecimal getDiffer_range() {
        return differ_range;
    }

    public void setDiffer_range(BigDecimal differ_range) {
        this.differ_range = differ_range;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmount_unit() {
        return amount_unit;
    }

    public void setAmount_unit(String amount_unit) {
        this.amount_unit = amount_unit;
    }

    public BigDecimal getClose_price() {
        return close_price;
    }

    public void setClose_price(BigDecimal close_price) {
        this.close_price = close_price;
    }

    public String getClose_price_unit() {
        return close_price_unit;
    }

    public void setClose_price_unit(String close_price_unit) {
        this.close_price_unit = close_price_unit;
    }

    public BigDecimal getDiffer() {
        return differ;
    }

    public void setDiffer(BigDecimal differ) {
        this.differ = differ;
    }

    public String getDiffer_unit() {
        return differ_unit;
    }

    public void setDiffer_unit(String differ_unit) {
        this.differ_unit = differ_unit;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public String getHigh_unit() {
        return high_unit;
    }

    public void setHigh_unit(String high_unit) {
        this.high_unit = high_unit;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public String getLow_unit() {
        return low_unit;
    }

    public void setLow_unit(String low_unit) {
        this.low_unit = low_unit;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public String getOpen_unit() {
        return open_unit;
    }

    public void setOpen_unit(String open_unit) {
        this.open_unit = open_unit;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getVolume_unit() {
        return volume_unit;
    }

    public void setVolume_unit(String volume_unit) {
        this.volume_unit = volume_unit;
    }

    public BigDecimal getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(BigDecimal amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public String toString() {
        return "KlineBO{" +
                "trade_date=" + trade_date +
                ", turn=" + turn +
                ", differ_range=" + differ_range +
                ", amount=" + amount +
                ", amount_unit='" + amount_unit + '\'' +
                ", close_price=" + close_price +
                ", close_price_unit='" + close_price_unit + '\'' +
                ", differ=" + differ +
                ", differ_unit='" + differ_unit + '\'' +
                ", high=" + high +
                ", high_unit='" + high_unit + '\'' +
                ", low=" + low +
                ", low_unit='" + low_unit + '\'' +
                ", open=" + open +
                ", open_unit='" + open_unit + '\'' +
                ", volume=" + volume +
                ", volume_unit='" + volume_unit + '\'' +
                ", amplitude=" + amplitude +
                '}';
    }
}
