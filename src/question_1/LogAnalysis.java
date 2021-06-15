package question_1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LogAnalysis {
    public void countTotalRequests(File f) {
        long startTime = System.currentTimeMillis(); // 记录方法的执行时间，以备日后优化
        try {
            long lines = Files.lines(Paths.get(f.getPath())).count();
            System.out.println("The total requests is:");
            System.out.println(lines);
            System.out.println("The function finished in " + (System.currentTimeMillis() - startTime) + " ms");
            System.out.println("------------------------");
            System.out.println();
        } catch (IOException e) {
            System.out.println("No files found");
        }
    }

    private String getRequestString(String s) {
        int startOf = s.indexOf("/");
        int endOf = s.indexOf("?");
        if (endOf < 0) {
            return s.substring(startOf, s.length());
        } else {
            return s.substring(startOf, endOf);
        }
    }

    /**
     * 测试输出的HTTP请求URI
     *
     * @param s 输入的日志行
     */
    public void testString(String s) {
        System.out.println(getRequestString(s));
    }

    public void topFrequentRequests(File f) {
        long startTime = System.currentTimeMillis(); // 记录方法的执行时间，以备日后优化
        TreeMap<String, Integer> map = new TreeMap<>();
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            for (String l : lineList) {
                map.merge(getRequestString(l), 1, Integer::sum);
            }
            List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            System.out.println("The top 10 frequent requests are:");
            for (int i = 0; i < 10; i++) {
                String request = list.get(i).getKey();
                int count = map.get(request);
                System.out.println(request + " ------ " + count);
            }
            System.out.println("The function finished in " + (System.currentTimeMillis() - startTime) + " ms");
            System.out.println("------------------------");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LogAnalysis logAnalysis = new LogAnalysis();
        File logFile = new File("src/question_1/access.log");
        logAnalysis.countTotalRequests(logFile);
        logAnalysis.topFrequentRequests(logFile);
    }
}
