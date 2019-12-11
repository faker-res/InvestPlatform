package la.niub.abcapi.invest.seller.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author : ppan
 * @description : 添加卖方研究员
 * @date : 2019-02-11 19:33
 */
@Data
public class RoadShowSellerAnalystSaveVo {
    @NotBlank(message = "参数[userId]不为空")
    private String userId;

    @NotBlank(message = "参数[buyerCompanyId]不为空")
    private String buyerCompanyId;

    @NotBlank(message = "参数[industry]不为空")
    private String industry;

    @NotBlank(message = "参数[analyst]不为空")
    private String analyst;
}
