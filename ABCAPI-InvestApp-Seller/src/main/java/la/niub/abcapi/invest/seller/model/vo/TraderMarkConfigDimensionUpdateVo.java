package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : ppan
 * @description : 保存券商评价纬度
 * @date : 2019-01-21 14:17
 */
public class TraderMarkConfigDimensionUpdateVo {

    @NotBlank(message = "参数[userId]必传")
    private String userId;

    @NotNull(message = "参数[id]必传")
    private Long id;

    @NotBlank(message = "参数[dimension]必传")
    private String dimension;

    @NotNull(message = "参数[calculateStatus]必传")
    @Min(value = 0, message = "参数[calculateStatus]只能为0或1")
    @Max(value = 1, message = "参数[calculateStatus]只能为0或1")
    private Integer calculateStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
