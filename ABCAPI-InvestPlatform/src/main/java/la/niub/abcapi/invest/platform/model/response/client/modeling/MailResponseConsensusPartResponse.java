package la.niub.abcapi.invest.platform.model.response.client.modeling;

import lombok.Data;

/**
 * 一致性预期
 */
@Data
public class MailResponseConsensusPartResponse {
    //年份
    private String year;

    //EPS(元)
    private Double epsValue;
}
