package la.niub.abcapi.invest.seller.config.enums;

public enum ExcelTypeEnum {

    XLS("xls"),
    XLSX("xlsx");

    private String type;

    ExcelTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
