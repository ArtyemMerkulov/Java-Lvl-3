package homework3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    private static final byte[] parsePhrase = {100, 101, 102, 103, 104, 105};

    public static void main(String[] args) {
        new Server();
    }

    private static final int PORT = 12345;

    private ServerSocket server;
    private Socket socket;
    private DataInputStream in;

    public Server() {
        try {
            server = new ServerSocket(PORT);

            while (true) {
                socket = server.accept();
                in = new DataInputStream(socket.getInputStream());

                byte[] receivedInitMsg = new byte[6];
                in.read(receivedInitMsg);

                if (Arrays.equals(receivedInitMsg, parsePhrase)) {
                    byte[] receivedMsg = new byte[in.readInt()];
                    in.read(receivedMsg);

                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(receivedMsg));

                    Student student = null;
                    try {
                        student = (Student) ois.readObject();
                        ois.close();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    student.info();

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}