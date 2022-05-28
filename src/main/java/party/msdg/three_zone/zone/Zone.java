package party.msdg.three_zone.zone;

import java.util.HashMap;
import java.util.Map;

/**
 * Wow! Sweet moon.
 */
public class Zone {
    public static String[] ZONE = {"黄浦","徐汇","长宁","静安","普陀","虹口","杨浦","闵行","宝山","嘉定","浦东新","金山","松江","南汇","奉贤","青浦","崇明"};
    
    public static Map<String, String> CODE;
    static {
        CODE = new HashMap<>();
        CODE.put("黄浦", "310101");
        CODE.put("徐汇", "310104");
        CODE.put("长宁", "310105");
        CODE.put("静安", "310106");
        CODE.put("普陀", "310107");
        CODE.put("虹口", "310109");
        CODE.put("杨浦", "310110");
        CODE.put("闵行", "310112");
        CODE.put("宝山", "310113");
        CODE.put("嘉定", "310114");
        CODE.put("浦东新", "310115");
        CODE.put("金山", "310116");
        CODE.put("松江", "310117");
        CODE.put("南汇", "310225");
        CODE.put("奉贤", "310226");
        CODE.put("青浦", "310229");
        CODE.put("崇明", "310230");
    }
    
}
