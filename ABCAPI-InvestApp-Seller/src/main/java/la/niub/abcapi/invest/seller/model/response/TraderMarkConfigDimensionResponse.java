package la.niub.abcapi.invest.seller.model.response;

/**
 * @author : ppan
 * @description : 券商评价纬度设置列表
 * @date : 2019-01-21 14:10
 */
public class TraderMarkConfigDimensionResponse {

    private Long id;

    private String dimension;

    private Integer calculateStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Integer getCalculateStatus() {
        return calculateStatus;
    }

    public void setCalculateStatus(Integer calculateStatus) {
        this.calculateStatus = calculateStatus;
    }
}
