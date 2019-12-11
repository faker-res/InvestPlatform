package la.niub.abcapi.invest.seller.config.code;

public enum TraderMarkEnumCodeConfig implements ICodeConfig {
    ERROR_TRADER_MARK_UPLOAD_EXCEL_FORMAT(52001, "error trader mark upload excel format"),
    ERROR_TRADER_MARK_UPLOAD_EXCEL_BROKER(52002, "error trader mark upload excel broker"),
    ERROR_TRADER_MARK_NO_MODE(52003, "error trader mark no mode"),
    ERROR_TRADER_MARK_SCORE(52004, "error trader mark score"),
    ERROR_TRADER_MARK_SCORE_FINISH(52005, "error trader mark score finish"),
    ERROR_TRADER_MARK_WEIGHT(52006, "error trader mark weight"),
    ;
    private int code;

    private String message;

    private TraderMarkEnumCodeConfig(int code, String message) {
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
