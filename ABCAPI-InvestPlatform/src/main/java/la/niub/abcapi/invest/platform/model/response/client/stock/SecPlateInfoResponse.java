package la.niub.abcapi.invest.platform.model.response.client.stock;

import lombok.Data;

@Data
public class SecPlateInfoResponse {
    private Long secUniCode;

    private String plateCode;

    private String plateName;

    private int id;
}
