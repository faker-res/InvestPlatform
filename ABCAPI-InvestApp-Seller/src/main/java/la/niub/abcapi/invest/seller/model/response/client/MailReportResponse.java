package la.niub.abcapi.invest.seller.model.response.client;

import lombok.Data;

@Data
public class MailReportResponse {

	  private String id;

	  private String owner_id;

	  private String source_url;

	  private String source_type;

	  private String source_name;

	  private String typetitle;

	  private String rating;

	  private String stockname;

	  private String stockcode;

	  private String publish;

	  private String filetype;

	  private int file_pages;

	  private String file_size;

	  private String file_url;

	  private String file_path;

	  private String title;

	  private String author;

	  private String tag;

	  private String category_id;

	  private String categoryname;

	  private String honor;

	  private String analyst;

	  private String alg_document_tags;

	  private String alg_title;

	  private String alg_publish_date;

	  private String alg_publish_category;

	  private String alg_rating_change;

	  private Integer alg_page_count;

	  private String alg_report_type_one;

	  private String alg_report_type_two;

	  private String[] alg_author_tel;

	  private String[] alg_author_email;

	  private String[] alg_author_certid;

	  private String alg_security_id;

	  private long time;

	  //下面是需要高亮的字段
	  private String source_title;

	  private String summary;

	  private String from_name;

	  private String from_address;

	  private String receivers;

	  private String sender_name;

	  private String group_name;

	  private String[] alg_author_name;

	  private String alg_industry_type;

	  private String alg_publish_name;

	  private String alg_report_type;

	  private String alg_report_type_three;

	  private String alg_security_name;

	  private String alg_rating;

	  private String alg_industry_type_one;

	  private String alg_industry_type_two;

	  private String alg_industry_type_three;
}
