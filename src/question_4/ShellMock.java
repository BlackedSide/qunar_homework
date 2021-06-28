package question_4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ShellMock {
    public void cat(File f) {
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            for (String l : lineList) {
                System.out.println(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void grep(File f, String s) {
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            for (String l : lineList) {
                if (l.contains(s)) {
                    System.out.println(l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wc(File f, String s) {
        try {
            List<String> lineList = Files.readAllLines(Paths.get(f.getPath()));
            int location = 1;
            for (String l : lineList) {
                if (l.contains(s)) {
                    System.out.println(location);
                }
                location++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void input() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine();
            if (line.trim().equals("exit") || line.trim().equals("quit")) {
                break;
            }
            String[] orders = line.split("(\\s+)\\|(\\s+)");
            int orderLength = orders.length;
            String absolutePath = "src/question_4/";
            try {
                if (orderLength == 1) {
                    String[] s = orders[0].split("\\s+");
                    if (s[0].equals("cat")) {
                        cat(new File(absolutePath + s[1]));
                    } else if (s[0].equals("grep")) {
                        grep(new File(absolutePath + s[1]), s[2]);
                    } else if (s[0].equals("wc") && s[1].equals("-l")) {
                        wc(new File(absolutePath + s[2]), s[3]);
                    }
                } else if (orderLength == 2 && orders[0].startsWith("cat")) {
                    String[] s1 = orders[0].split("\\s+");
                    String[] s2 = orders[1].split("\\s+");
                    if (s2[0].equals("grep")) {
                        grep(new File(absolutePath + s1[1]), s2[1]);
                    } else if (s2[0].equals("wc") && s2[1].equals("-l")) {
                        wc(new File(absolutePath + s1[1]), s2[2]);
                    }
                } else if (orderLength == 3 && orders[0].startsWith("cat")) {
                    String[] s1 = orders[0].split("\\s+");
                    String[] s2 = orders[1].split("\\s+");
                    String[] s3 = orders[2].split("\\s+");
                    if (s2[0].equals("grep") && s3[0].equals("wc") && s3[1].equals("-l")) {
                        wc(new File(absolutePath + s1[1]), s2[1]);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong input......");
            }
        }
    }

    public static void main(String[] args) {
        ShellMock shellMock = new ShellMock();
        shellMock.input();
    }
}
