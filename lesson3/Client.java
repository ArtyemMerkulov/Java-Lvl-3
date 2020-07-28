package homework3;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client {

    private static final byte[] parsePhrase = {100, 101, 102, 103, 104, 105};

    public static void main(String[] args) {
        new Client();
    }

    private Socket socket;
    private DataOutputStream out;

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 12345;

    public Client() {
        connectToServer();
        sendMsg(getSerializableObject());
    }

    private void connectToServer() {
        try {
            socket = new Socket(IP_ADDR, PORT);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getSerializableObject() {
        byte[] sendBytes = null;

        Student student = new Student(1, "Bob");
        student.info();

        ObjectOutputStream oos;
        ByteArrayOutputStream baos;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(student);
            sendBytes = baos.toByteArray();

            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendBytes;
    }

    private void sendMsg(byte[] msg) {
        try {
            out.write(concatAll(parsePhrase, ByteBuffer.allocate(4).putInt(msg.length).array(), msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}