package top.feadre.fctr;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public final class DroidConnection implements Closeable {


    private static Socket socket = null;
    private OutputStream outputStream;
    private InputStream inputStream;

    private DroidConnection(Socket socket) throws IOException {
        this.socket = socket;

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }


    private static Socket listenAndAccept() throws IOException {
        ServerSocket serverSocket = new ServerSocket(7007);
        Socket sock = null;
        try {
            sock = serverSocket.accept();
        } finally {
            serverSocket.close();
        }
        return sock;
    }

    public static DroidConnection open(String ip) throws IOException {
        ArrayList<String> res = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        res.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Ln.d("open ip_sockets = " + res); // /127.0.0.1

        socket = listenAndAccept();

//        String ip_socket = socket.getInetAddress().toString();
//        Ln.d("ip_socket = " + ip_socket); // /127.0.0.1
//        if (ip_socket.equals(ip)) {
//            connection = new DroidConnection(socket);
//        }

        DroidConnection connection = new DroidConnection(socket);
        return connection;
    }

    public void close() throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }


    public int[] NewreceiveControlEvent() throws IOException {

        byte[] buf = new byte[16];
        int n = inputStream.read(buf, 0, 16);
        if (n == -1) {
            throw new EOFException("Event controller socket closed");
        }

        final int[] array = new int[buf.length / 4];
        for (int i = 0; i < array.length; i++)
            array[i] = (((int) (buf[i * 4]) << 24) & 0xFF000000) |
                    (((int) (buf[i * 4 + 1]) << 16) & 0xFF0000) |
                    (((int) (buf[i * 4 + 2]) << 8) & 0xFF00) |
                    ((int) (buf[i * 4 + 3]) & 0xFF);
        return array;


    }

}

