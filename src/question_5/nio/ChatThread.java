package question_5.nio;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatThread extends Thread {
    private final Selector selector;
    private final SocketChannel socket;

    public ChatThread(Selector selector, SocketChannel socket) {
        super();
        this.selector = selector;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);
        System.out.println("请输入需要请求的URL：");
        System.out.println("========================");
        while (in.hasNextLine()) {
            String s = in.nextLine();
            try {
                socket.register(selector, SelectionKey.OP_WRITE, ByteBuffer.wrap(s.getBytes()));
                selector.wakeup();
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }
    }
}
