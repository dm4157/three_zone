package party.msdg.three_zone.util;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 呵呵哒
 * Created by msdg on 2018/8/12.
 * Look, there is a moon.
 */
public class LittleTrick {

    public static boolean notEmpty(Object target) {
        return ! isEmpty(target);
    }

    public static boolean isEmpty(Object target) {
        if (null == target) return true;

        if (target instanceof String) {
            return 0 == ((String) target).length();
        }

        if (target instanceof Collection) {
            return 0  == ((Collection) target).size();
        }

        if (target instanceof Map) {
            return 0 == ((Map) target).size();
        }

        if (target.getClass().isArray()) {
            return 0 == Array.getLength(target);
        }

        return false;
    }

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static Date toDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            return new Date(0);
        }
    }
    
    public static LocalDateTime todayStart() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    }

    public static LocalDateTime tomorrowStart() {
        return todayStart().plusDays(1);
    }

    public static LocalDateTime severalMinutesAgo(long minutes) {
        return LocalDateTime.now().minusMinutes(minutes);
    }

    public static LocalDateTime severalHoursAgo(long hours) {
        return LocalDateTime.now().minusHours(hours);
    }

    public static LocalDateTime severalDaysAgo(long days) {
        return LocalDateTime.now().minusDays(days);
    }
    
    public static String buildMsgWithTemplate(String messageTemp, Object...message) {
        if (null == messageTemp) return null;
        
        Object[] finalMessage = new Object[message.length + 1];
        finalMessage[0] = messageTemp;
        
        if (LittleTrick.notEmpty(message)) {
            System.arraycopy(message, 0, finalMessage, 1, message.length);
        }
        
        return buildMsg(finalMessage);
    }
    
    public static String buildMsg(Object...message) {
        String template = message.length == 0 || message[0] == null ? "" : message[0].toString();
        if (1 == message.length) {
            return template;
        }

        StringBuilder finalMsg = new StringBuilder();
        int msgIndex = 1;
        char[] chz = template.toCharArray();
        for (int i=0; i<chz.length; i++) {
            if (chz[i] == '{' && i < chz.length - 1 && chz[i+1] == '}' && msgIndex < message.length) {
                finalMsg.append(message[msgIndex++]);
                i++;
            } else {
                finalMsg.append(chz[i]);
            }
        }
        return finalMsg.toString();
    }

    public static void print(List list) {
        System.out.print("[");
        System.out.print(list.stream().map(Object::toString).collect(Collectors.joining(",")));
        System.out.println("]");
    }

    public static String subStr(String val, int maxLex) {
        if (LittleTrick.isEmpty(val) || val.length() <= maxLex) {
            return val;
        } else {
            return val.substring(0, maxLex) + "...";
        }
    }
    
    public static String searchKey(String keyword) {
        if (LittleTrick.isEmpty(keyword)) {
            return null;
        } else {
            return "%" + keyword + "%";
        }
    }
    
    public static String nonStr(String value) {
        return value == null ? "" : value;
    }
    
    public static String dateToday() {
        return dateTrick("yyyy-MM-dd").toString(LocalDate.now());
    }
    
    public static String dateYesterday() {
        return dateFromToday(-1);
    }
    
    public static String dateTomorrow() {
        return dateFromToday(1);
    }
    
    public static String dateFromToday(int val) {
        return dateFromToday(val, "yyyy-MM-dd");
    }
    
    public static String dateFromToday(int val, String pattern) {
        LocalDate today = LocalDate.now();
        LocalDate theDate = today.plusDays(val);
        return dateTrick(pattern).toString(theDate);
    }
    
    public static DateTrick dateTrick(String pattern) {
        return new DateTrick(pattern);
    }
    
    public static class DateTrick {
        private String pattern;
    
        private DateTrick(String pattern) {
            this.pattern = pattern;
        }
        
        public String toString(LocalDate date) {
            return date.format(DateTimeFormatter.ofPattern(this.pattern));
        }
        
        public String toString(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }
    
    /**
     * 计算两个日期之间的天数
     */
    public static int daysBetween(LocalDate fromDate, LocalDate toDate) {
        long fromSeconds = fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond();
        long toSeconds = toDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond();
        return (int) ((fromSeconds - toSeconds) / (3600 * 24));
    }
    
    /**
     * 随机整数
     * 范围：[0, max)
     */
    public static int randomInt(int max) {
        return randomInt(0, max);
    }
    
    /**
     * 随机整数
     * 范围：[min, max)
     */
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max-min) + min);
    }
    
    /**
     * 随机生成指定长度等字符串
     * 可用字符：0～9,a~z,A~Z
     */
    public static String randomString(int length) {
        String code = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
       
        char[] randomCode = new char[length];
        for (int i = 0; i < randomCode.length; i++) {
            randomCode[i] = code.charAt(randomInt(code.length()));
        }
        return new String(randomCode);
    }
    
    /**
     * 随机生成指定长度等字符串
     * 可用字符：0～9,a~z,A~Z
     * 长度范围：[minLen, maxLen)
     */
    public static String randomString(int minLen, int maxLen){
        return randomString(randomInt(minLen, maxLen));
    }
}
