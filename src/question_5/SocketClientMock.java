package question_5;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientMock {
    private static final int port = 8888;

    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getLocalHost();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            Socket socket = new Socket(host.getHostName(), port);
            String message = in.nextLine();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String response = (String) ois.readObject();
            System.out.println(response);
        }
    }
}
