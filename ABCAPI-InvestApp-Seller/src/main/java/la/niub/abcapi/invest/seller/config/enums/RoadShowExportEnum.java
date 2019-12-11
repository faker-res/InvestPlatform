package la.niub.abcapi.invest.seller.config.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ppan
 * @description :
 * @date : 2019-03-19 15:46
 */
public class RoadShowExportEnum {

    public static String getFieldName(String field) {
        return FIELD_TABLE_HEAD.get(field);
    }

    public static final Map<String, String> FIELD_TABLE_HEAD = new HashMap<String, String>() {
        {
            put("industry", "行业");
            put("seller", "卖方人员");
            put("sellerCompany", "卖方机构");
            put("activityStartTime", "时间");
            // 产品定义描述导成excel中的主题
            put("activityDesc", "描述");
            put("buyer", "拟参会人员");
            put("company", "上市公司");
            put("meetingRoom", "会议室");
            put("title", "主题");
        }
    };
}
