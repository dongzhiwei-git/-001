package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket server;
    private static int player = 0;
    private static int ipNum = 2;
    ClientHandler handler;

    public Server() {
        try {
            this.server = new ServerSocket(8088);
            System.out.println("服务器端启动了");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            while (true) {
                Socket socket = server.accept();
                System.out.println("一个客服端连接了");
                String ip = socket.getInetAddress().getHostAddress();
                handler = new ClientHandler(socket, ip, player++);
                Thread t1 = new Thread(handler);
                t1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
