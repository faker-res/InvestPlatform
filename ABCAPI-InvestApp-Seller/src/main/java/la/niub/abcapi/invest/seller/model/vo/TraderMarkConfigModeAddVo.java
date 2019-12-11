package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author : ppan
 * @description : 保存/修改评价模式vo
 * @date : 2019-01-19 15:47
 */
public class TraderMarkConfigModeAddVo {

    @NotBlank(message = "参数[userId]必传")
    private String userId;

    @NotNull(message = "参数[modeId]必传")
    private Long modeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getModeId() {
        return modeId;
    }

    public void setModeId(Long modeId) {
        this.modeId = modeId;
    }
}
