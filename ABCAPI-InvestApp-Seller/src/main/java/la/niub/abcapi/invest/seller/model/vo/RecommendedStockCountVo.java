package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

public class RecommendedStockCountVo {

    @NotBlank(message = "参数[userId]不能为空")
    private String userId;
    private String date;
    private String keyword;
    private Integer offset = 1;
    private Integer limit = 12;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "RecommendedStockCountVo{" +
                "userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                ", keyword='" + keyword + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
