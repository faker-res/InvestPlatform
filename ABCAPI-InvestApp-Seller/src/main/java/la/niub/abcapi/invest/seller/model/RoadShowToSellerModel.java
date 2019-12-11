package la.niub.abcapi.invest.seller.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoadShowToSellerModel implements Serializable {

    private static final long serialVersionUID = -9112144731682011806L;

    private Long id;

    private Long roadShowId;

    private String sellerId;

    private String sellerName;

    private String sellerCompanyId;

    private String sellerCompanyName;

    private Date createTime;

    private Date updateTime;
}