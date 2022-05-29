package party.msdg.three_zone.util;

/**
 * Wow! Sweet moon.
 */
public class AddUtil {
    
    public static String removeBracket(String src) {
        int left = src.indexOf("(");
        if (left == -1) return src;
    
        int right = src.indexOf(")");
        return src.substring(0, left) + src.substring(right+1);
    }
    
    public static String removeBracketZH(String src) {
        int left = src.indexOf("（");
        if (left == -1) return src;
        
        int right = src.indexOf("）");
        return src.substring(0, left) + src.substring(right+1);
    }
}
