package question_5;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientMock {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getLocalHost();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            Socket socket = new Socket(host.getHostName(), 8888);
            String message = in.nextLine();
            if (message.equalsIgnoreCase("exit")) break;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        }
    }
}
