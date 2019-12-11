package la.niub.abcapi.invest.seller.model.vo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : ppan
 * @description : 保存券商评分
 * @date : 2019-01-29 19:12
 */
public class TraderMarkTaskSaveVo {

    @NotBlank(message = "参数[userId]不能为空")
    private String userId;

    @NotNull(message = "参数[taskId]不能为空")
    private Long taskId;

    @NotEmpty(message = "参数[scoreList]不能为空")
    @Valid
    private List<TraderMarkTaskSaveItemVo> scoreList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<TraderMarkTaskSaveItemVo> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<TraderMarkTaskSaveItemVo> scoreList) {
        this.scoreList = scoreList;
    }
}
