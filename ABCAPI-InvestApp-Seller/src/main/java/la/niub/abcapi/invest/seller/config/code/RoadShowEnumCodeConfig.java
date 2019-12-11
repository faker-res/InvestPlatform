package la.niub.abcapi.invest.seller.config.code;

public enum RoadShowEnumCodeConfig implements ICodeConfig {
    ERROR_ROAD_SHOW_SELLER_ANALYST_EXIST(53001, "error road show seller analyst exist"),
    ERROR_ROAD_SHOW_DELETE_TIME_START(53002, "error road show delete time start"),
    ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT(52003, "error road show upload excel format"),
    ERROR_TRADER_MARK_UPLOAD_EXCEL_BROKER(52004, "error road show upload excel broker"),
    ;
    private int code;

    private String message;

    private RoadShowEnumCodeConfig(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
