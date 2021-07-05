package question_5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketServerMock {
    private static int countChinese(String s) {
        int count = 0;
        Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(s);
        while (m.find()) count++;
        return count;
    }

    private static int countLetter(String s) {
        int count = 0;
        Matcher m = Pattern.compile("[a-zA-Z]").matcher(s);
        while (m.find()) count++;
        return count;
    }

    private static int countMark(String s) {
        int count = 0;
        Matcher m = Pattern.compile("\\pP").matcher(s);
        while (m.find()) count++;
        return count;
    }

    private static final int port = 8888;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket sever = new ServerSocket(port);
        while (true) {
            Socket socket = sever.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            if (message.equalsIgnoreCase("exit")) break;
            System.out.println("汉字个数为： " + countChinese(message));
            System.out.println("字母个数为： " + countLetter(message));
            System.out.println("标点个数为： " + countMark(message));
            System.out.println("总字符数为： " + message.length());
            ois.close();
            socket.close();
        }
        System.out.println("Bye");
        sever.close();
    }
}
