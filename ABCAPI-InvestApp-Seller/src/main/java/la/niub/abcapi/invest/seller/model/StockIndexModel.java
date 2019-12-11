package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockIndexModel implements Serializable {

	private static final long serialVersionUID = -1664887541264865127L;

	private Long id;

	private Long secUniCode;

	private String stockCode;

	private String stockName;

	private BigDecimal goldRate;

	private BigDecimal industryRate;

	private BigDecimal monthBeginPrice;

	private BigDecimal monthEndPrice;

	private String pushMonth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSecUniCode() {
		return secUniCode;
	}

	public void setSecUniCode(Long secUniCode) {
		this.secUniCode = secUniCode;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public BigDecimal getGoldRate() {
		return goldRate;
	}

	public void setGoldRate(BigDecimal goldRate) {
		this.goldRate = goldRate;
	}

	public BigDecimal getIndustryRate() {
		return industryRate;
	}

	public void setIndustryRate(BigDecimal industryRate) {
		this.industryRate = industryRate;
	}

	public String getPushMonth() {
		return pushMonth;
	}

	public BigDecimal getMonthBeginPrice() {
		return monthBeginPrice;
	}

	public void setMonthBeginPrice(BigDecimal monthBeginPrice) {
		this.monthBeginPrice = monthBeginPrice;
	}

	public BigDecimal getMonthEndPrice() {
		return monthEndPrice;
	}

	public void setMonthEndPrice(BigDecimal monthEndPrice) {
		this.monthEndPrice = monthEndPrice;
	}

	public void setPushMonth(String pushMonth) {
		this.pushMonth = pushMonth;
	}

}