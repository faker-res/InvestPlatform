package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

import java.util.List;

@Data
public class MailReportSearchResponse {

	private Object partialResult;
	private int total_count;
	private List<MailReportResponse> item;
	private String solrquery;
	/**
	 * 耗时，毫秒值
	 */
	private long time_ms;
}
