package la.niub.abcapi.invest.seller.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author : ppan
 * @description :
 * @date : 2019-04-03 18:44
 */
@Data
public class RoadShowSellerVo {

    //@NotBlank(message = "路演研究员id必填")
    private String sellerId = "";

    //@NotBlank(message = "路演研究员名称名称必填")
    private String sellerName;

}
