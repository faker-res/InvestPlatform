package la.niub.abcapi.invest.seller.model;

import java.io.Serializable;

public class TraderMarkModeModel implements Serializable {

    private static final long serialVersionUID = -3081682617967234206L;

    private Long id;

    private String modeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }
}