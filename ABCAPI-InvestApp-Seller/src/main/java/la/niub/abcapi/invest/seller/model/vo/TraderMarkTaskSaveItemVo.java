package la.niub.abcapi.invest.seller.model.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : ppan
 * @description : 保存券商评分
 * @date : 2019-01-29 19:14
 */
public class TraderMarkTaskSaveItemVo {

    @NotNull(message = "参数[id]不能为空")
    private Long id;

    @NotNull(message = "参数[score]不能为空")
    @Min(value = 0, message = "参数[score]大于等于0")
    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
