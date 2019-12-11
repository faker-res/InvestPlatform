package la.niub.abcapi.invest.seller.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import la.niub.abcapi.invest.seller.constant.ReportSearchConstant;
import lombok.Data;

import java.util.List;

/**
 * @author wangtonghe
 * @since 2018/5/18 13:29
 */
@Data
public class MailReportSearchVo {

    private List<String> involves;

    private String userId;

    private String parentId;

    private String token;

    @JSONField(name = "search_word")
    private SearchWordVo searchWord;

    /**
     * 前端传输单位为毫秒，搜索需要秒，需转化
     */
    @JSONField(name = "end_time")
    private Long endTime;

    @JSONField(name = "page_num")
    private Integer pageNum;

    @JSONField(name = "page_size")
    private Integer pageSize;

    private String subject;

    /**
     * 排序，0为综合排序，1为时间降序，2为时间正序
     */
    private Integer prior;

    private String file_type;

    public int getPageNum() {
        if (pageNum == null) {
            return ReportSearchConstant.MAIL_SEARCH_PAGE_NUM;
        }
        return pageNum;
    }

    public int getPageSize() {
        if (pageSize == null) {
            return ReportSearchConstant.SEARCH_PAGE_SIZE;
        }
        return pageSize;
    }

    public long getEndTime() {
        if (endTime == null || endTime == 0) {
            return System.currentTimeMillis() / 1000;
        }
        return endTime / 1000;
    }

    public String getPriorStr() {
        if (prior == null || prior == 0) {
            return ReportSearchConstant.SORT_SCORE;
        }
        String sortStr = ReportSearchConstant.SORT_SCORE;
        if (prior == 1) {
            sortStr = ReportSearchConstant.SORT_TIME_DESC;
        } else if (prior == 2) {
            sortStr = ReportSearchConstant.SORT_TIME_ASC;
        }
        return sortStr;
    }

    public boolean isSelfStock() {
        if (involves == null || involves.size() == 0) {
            return false;
        }
        return involves.stream().anyMatch(e -> e.equalsIgnoreCase(ReportSearchConstant.MAIL_INVOLVE_STOCK));
    }

}
