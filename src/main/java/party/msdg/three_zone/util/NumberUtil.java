package party.msdg.three_zone.util;

import java.text.NumberFormat;

/**
 * Created by msdg on 2018/6/6.
 * Look, there is a moon.
 */
public class NumberUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    /**
     * 格式化小数位数
     *
     * @param value   数值
     * @param dotSize 保留小数位数（强制）
     */
    public static Double parse(Double value, int dotSize) {
        if (null == value) return null;

        numberFormat.setMaximumFractionDigits(dotSize);
        numberFormat.setMinimumFractionDigits(dotSize);
        numberFormat.setGroupingUsed(false);
        return Double.parseDouble(numberFormat.format(value));
    }

    public static boolean isNumeric(String str) {
        if(str == null || "".equals(str)){
            return false;
        }
        char[] strChar = str.toCharArray();
        int start = 0;
        if(strChar[0] == '-'){
            start++;
        }
        for (int i = start; i<strChar.length; i++ ) {
            if (!Character.isDigit(strChar[i])) {
                return false;
            }
        }
        return true;
    }
}
