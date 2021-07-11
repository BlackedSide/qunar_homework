package question_5.nio.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistic {
    public static int countChinese(String s) {
        int count = 0;
        Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(s);
        while (m.find()) count++;
        return count;
    }

    public static int countLetter(String s) {
        int count = 0;
        Matcher m = Pattern.compile("[a-zA-Z]").matcher(s);
        while (m.find()) count++;
        return count;
    }

    public static int countMark(String s) {
        int count = 0;
        Matcher m = Pattern.compile("\\pP").matcher(s);
        while (m.find()) count++;
        return count;
    }
}
