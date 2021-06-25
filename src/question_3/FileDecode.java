package question_3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDecode {
    public static Map<String, String> dicMap = new HashMap<>();
    public static List<String> dicList = new ArrayList<>();
    public static List<String> dicCharList = new ArrayList<>();
    public static int listSize = 0;

    public FileDecode(File f) {
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            for (String l : lineList) {
                if (!l.equals("")) {
                    String[] line = l.trim().split("\t");
                    dicMap.put(line[0], line[1]);
                    dicList.add(line[1]);
                    dicCharList.add(line[1]);
                }
            }
            dicCharList.sort(Comparator.comparingInt(o -> o.charAt(0)));
            listSize = dicCharList.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replaceFile(File temp, File out) {
        try {
            FileWriter writer = new FileWriter(out);
            List<String> lineList = Files.readAllLines(Paths.get(temp.getPath()));
            for (String l : lineList) {
                Pattern naturePattern = Pattern.compile("(?:\\$natureOrder\\()([0-9]+)(?:\\))");
                Pattern indexPattern = Pattern.compile("(?:\\$indexOrder\\()([0-9]+)(?:\\))");
                Pattern charPattern = Pattern.compile("(?:\\$charOrder\\()([0-9]+)(?:\\))");
                Pattern charDescPattern = Pattern.compile("(?:\\$charOrderDESC\\()([0-9]+)(?:\\))");
                Matcher[] matchers = new Matcher[]{
                        naturePattern.matcher(l),
                        indexPattern.matcher(l),
                        charPattern.matcher(l),
                        charDescPattern.matcher(l)
                };
                if (matchers[0].find()) {
                    String newL = l.replaceAll("\\$natureOrder\\([0-9]+\\)", dicList.get(Integer.parseInt(matchers[0].group(1))));
                    writer.write(newL + "\n");
                } else if (matchers[1].find()) {
                    String newL = l.replaceAll("\\$indexOrder\\([0-9]+\\)", dicMap.get(matchers[1].group(1)));
                    writer.write(newL + "\n");
                } else if (matchers[2].find()) {
                    String newL = l.replaceAll("\\$charOrder\\([0-9]+\\)", dicCharList.get(Integer.parseInt(matchers[2].group(1))));
                    writer.write(newL + "\n");
                } else if (matchers[3].find()) {
                    String newL = l.replaceAll("\\$charOrderDESC\\([0-9]+\\)", dicCharList.get(listSize - Integer.parseInt(matchers[3].group(1))));
                    writer.write(newL + "\n");
                } else {
                    writer.write(l + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File propFile = new File("src/question_3/sdxl_prop.txt");
        File tempFile = new File("src/question_3/sdxl_template.txt");
        File outFile = new File("src/question_3/sdxl.txt");
        FileDecode fileDecode = new FileDecode(propFile);
        fileDecode.replaceFile(tempFile, outFile);
        System.out.println("文本解密完成……");
    }
}
