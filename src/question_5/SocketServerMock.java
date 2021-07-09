package question_5;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket sever = new ServerSocket(port);
        while (true) {
            Socket socket = sever.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String url = (String) ois.readObject();

            /*
              请求 http 并解析响应体
             */
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
//                System.out.println(response.toString());
                System.out.println("汉字个数为： " + countChinese(response.toString()));
                System.out.println("字母个数为： " + countLetter(response.toString()));
                System.out.println("标点个数为： " + countMark(response.toString()));
                System.out.println("总字符数为： " + response.length());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(
                        "汉字个数为： " + countChinese(response.toString()) + "\n"
                                + "字母个数为： " + countLetter(response.toString()) + "\n"
                                + "标点个数为： " + countMark(response.toString()) + "\n"
                                + "总字符数为： " + response.length()
                );
                in.close();
            } else {
//                System.out.println(responseCode);
                System.out.println("请求http数据时发生了错误……");
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("请求http数据时发生了错误，请检查……");
            }
            ois.close();
            socket.close();
        }
    }
}
