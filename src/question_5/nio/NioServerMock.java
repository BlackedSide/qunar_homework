package question_5.nio;

import question_5.nio.utils.Statistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;

public class NioServerMock {
    private static final int port = 9999;

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            Calendar ca = Calendar.getInstance();
            System.out.println("服务端开启了……");
            System.out.println("========================");

            while (true) {
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socket = serverSocketChannel.accept();
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                        String message = "连接成功！您是第 " + (selector.keys().size() - 1) + " 个用户。";
                        socket.write(ByteBuffer.wrap(message.getBytes()));
                        InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                        System.out.println(ca.getTime() + "\t" + address.getHostString() +
                                ":" + address.getPort() + "\t");
                        System.out.println("客户端已连接……");
                        System.out.println("========================");
                    }

                    if (key.isReadable()) {
                        SocketChannel socket = (SocketChannel) key.channel();
                        InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                        System.out.println(ca.getTime() + "\t" + address.getHostString() +
                                ":" + address.getPort() + "\t");
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
                        int len;
                        byte[] res = new byte[1024 * 4];
                        try {
                            while ((len = socket.read(buffer)) != 0) {
                                buffer.flip();
                                buffer.get(res, 0, len);
                                String url = new String(res, 0, len);
                                URL obj = new URL(url);
                                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                con.setRequestMethod("GET");
                                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                                int responseCode = con.getResponseCode();
                                if (responseCode == 200) {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                    String inputLine;
                                    StringBuilder response = new StringBuilder();
                                    while ((inputLine = in.readLine()) != null) {
                                        response.append(inputLine);
                                    }
                                    String output = "汉字个数为： " + Statistic.countChinese(response.toString()) + "\n"
                                            + "字母个数为： " + Statistic.countLetter(response.toString()) + "\n"
                                            + "标点个数为： " + Statistic.countMark(response.toString()) + "\n"
                                            + "总字符数为： " + response.length();
                                    System.out.println(output);
                                    socket.write(ByteBuffer.wrap(output.getBytes()));
                                }
                                buffer.clear();
                            }
                            System.out.println("========================");
                        } catch (IOException e) {
                            key.cancel();
                            socket.close();
                            System.out.println("客户端已断开……");
                            System.out.println("========================");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务端异常，已断开……");
            System.out.println("========================");
        }
    }
}
