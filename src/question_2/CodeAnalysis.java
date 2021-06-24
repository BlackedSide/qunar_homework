package question_2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CodeAnalysis {
    private int[] countLines(File f) {
        int codeLines = 0;
        int invalidLines = 0;
        boolean flag = false;
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            for (String l : lineList) {
                String line = l.trim();
                if (line.matches("^[ ]*$")) {
                    invalidLines++;
                } else if (line.startsWith("//")) {
                    invalidLines++;
                } else if (line.startsWith("/*") && line.endsWith("*/")) {
                    invalidLines++;
                } else if (line.startsWith("/**") && !line.endsWith("*/")) {
                    invalidLines++;
                    flag = true;
                } else if (flag) {
                    invalidLines++;
                    if (line.endsWith("*/")) {
                        flag = false;
                    }
                } else {
                    codeLines++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{codeLines, invalidLines};
    }

    public void printResult(File f) {
        int[] result = countLines(f);
        System.out.println("文件中有效代码行数为： " + result[0]);
        System.out.println("文件中无效代码行数为： " + result[1]);
    }

    public static void main(String[] args) {
        CodeAnalysis codeAnalysis = new CodeAnalysis();
        File file = new File("src/question_2/StringUtils.txt");
        codeAnalysis.printResult(file);
    }
}
