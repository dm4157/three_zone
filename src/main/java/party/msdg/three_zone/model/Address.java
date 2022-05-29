package party.msdg.three_zone.model;

import lombok.Data;
import org.apache.commons.math3.analysis.function.Add;
import party.msdg.three_zone.util.AddUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wow! Sweet moon.
 */
public class Address {
    public static String[] ZONE_SPLIT = new String[]{"市", "区", "县", "镇", "街道", "街", "路", "弄小区", "弄", "号"};
    public static String SMALL = "小区";
    
    private final Map<String, String> parts = new HashMap<>(ZONE_SPLIT.length + 1);
    
    public Address(String addressStr) {
        if (null == addressStr || "".equals(addressStr)) {
            return;
        }
        
        addressStr = removeOther(addressStr);
        
        for (int i = 0; i < ZONE_SPLIT.length; i++) {
            String[] findParts = find(addressStr, ZONE_SPLIT[i]);
            parts.put(ZONE_SPLIT[i], findParts[1]);
            addressStr = findParts[0];
        }
        parts.put(SMALL, addressStr);
    }
    
    private String[] find(String src, String split) {
        int index = src.indexOf(split);
        
        if (index > -1) {
            String part = src.substring(0, index);
            src = src.substring(index + split.length());
            return new String[] {src, part};
        } else {
            return new String[] {src, null};
        }
    }
    
    private String removeOther(String src) {
        // 经纬度
        int location = src.indexOf("经纬度");
        if (location > -1) {
            src = src.substring(0, location);
        }
        
        src = AddUtil.removeBracket(src);
        return src;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ZONE_SPLIT.length; i++) {
            if (null == parts.get(ZONE_SPLIT[i])) continue;
            sb.append(parts.get(ZONE_SPLIT[i])).append(ZONE_SPLIT[i]).append("|");
        }
        sb.append(parts.get(SMALL));
        
        return sb.toString();
    }
    
    public Map<String, String> getParts() {
        return parts;
    }
}
