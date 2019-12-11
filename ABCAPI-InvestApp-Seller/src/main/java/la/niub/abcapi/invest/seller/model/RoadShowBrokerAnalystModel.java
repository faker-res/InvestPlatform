package la.niub.abcapi.invest.seller.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoadShowBrokerAnalystModel implements Serializable {

    private static final long serialVersionUID = 5883367001077400029L;

    private Long id;

    private String parentId;

    private String broker;

    private String industry;

    private String analyst;

    private Integer source;

    private Date createTime;

    private Date updateTime;

    private String createId;

    private String updateId;
}