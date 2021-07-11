package question_5.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

public class NioClientMock {
    private static final int port = 9999;

    public static void main(String[] args) {
        try {
            SocketChannel socket = SocketChannel.open();
            socket.configureBlocking(false);
            Selector selector = Selector.open();

            socket.register(selector, SelectionKey.OP_CONNECT);
            socket.connect(new InetSocketAddress("localhost", port));

            new ChatThread(selector, socket).start();
            Calendar ca = Calendar.getInstance();

            while (true) {
                if (socket.isOpen()) {
                    selector.select();

                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isConnectable()) {
                            while (!socket.finishConnect()) {
                                System.out.println("正在连接中……");
                            }
                            socket.register(selector, SelectionKey.OP_READ);
                        }

                        if (key.isWritable()) {
                            socket.write((ByteBuffer) key.attachment());

                            socket.register(selector, SelectionKey.OP_READ);
                            System.out.println("====== " + ca.getTime() + " ======");
                        }

                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
                            int len;

                            try {
                                if ((len = socket.read(buffer)) > 0) {
                                    System.out.println("接收来自服务端的信息：");
                                    System.out.println(new String(buffer.array(), 0, len));
                                }
                            } catch (IOException e) {
                                System.out.println("服务端发生异常……");
                                key.cancel();
                                socket.close();
                            }
                            System.out.println("========================");
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("客户端异常……");
        }
    }
}
