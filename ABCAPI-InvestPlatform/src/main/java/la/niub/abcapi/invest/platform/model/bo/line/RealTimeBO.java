package la.niub.abcapi.invest.platform.model.bo.line;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RealTimeBO {

    private Date time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date trade_date;

    //最新价
    private Double now;

    //现手
    private Double roundlot;

    //涨跌幅
    private Double differ_range;

    //成交量
    private BigDecimal volume;

    //成交额
    private BigDecimal amount;

    //量比
    private Double volumeratio;

    //委比
    private Double commissiondiff;

    //买价1
    private Double buyprice1;

    //买价2
    private Double buyprice2;

    //买价3
    private Double buyprice3;

    //买价4
    private Double buyprice4;

    //买价5
    private Double buyprice5;

    //买量1
    private Double buyvolume1;

    //买量2
    private Double buyvolume2;

    //买量3
    private Double buyvolume3;

    //买量4
    private Double buyvolume4;

    //买量5
    private Double buyvolume5;

    //卖价1
    private Double sellprice1;

    //卖价2
    private Double sellprice2;

    //卖价3
    private Double sellprice3;

    //卖价4
    private Double sellprice4;

    //卖价5
    private Double sellprice5;

    //卖量1
    private Double sellvolume1;

    //卖量2
    private Double sellvolume2;

    //卖量3
    private Double sellvolume3;

    //卖量4
    private Double sellvolume4;

    //卖量5
    private Double sellvolume5;

    //最高价
    private Double high;

    //最低价
    private Double low;

    //今日开盘价
    private Double open;

    //昨收价
    private Double pre_close;

    //涨跌
    private Double differ;

    //停牌标记(Y:停牌，N:未停牌)
    private String suspension;

    //委差
    private Double commission;

    //外盘
    private Double outvolume;

    //内盘
    private Double involume;

    //上涨家数
    private Integer rise_num;

    //下跌家数
    private Integer fall_num;

    //持平家数
    private Integer fair_num;

    //流通市值
    private BigDecimal liqmv;

    //总市值
    private BigDecimal mv;

    //市盈率(动)
    private Double pe;

    //市净率
    private Double pb;

    //振幅
    private Double swing;

    //换手率
    private Double change;
}
