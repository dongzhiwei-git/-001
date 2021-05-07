package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String host;
    private static List<PrintWriter> allOut = new ArrayList<PrintWriter>();
    private static Map<Integer, List<PrintWriter>> allPw = new HashMap<>();
    private int connction;
    private boolean flag;
    private boolean flag2;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ClientHandler(Socket socket, String host, int connction) {
        this.socket = socket;
        this.host = host;
        this.connction = connction;
    }

    public void run() {
        System.out.println("启动一个线程处理客户端");
        System.out.println("等待客户端连接");
        try (OutputStream out = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(out, true);
             InputStream in = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(isr);) {
            allOut.add(pw);
            if (allOut.size() == 2) {
                allPw.put(connction / 2, allOut);
                allOut = new ArrayList<PrintWriter>();
            }
            System.out.println("一个客户端连接了，地址为" + host + ",这是第" + connction + "个客户端");
            if (!flag) {
                pw.println("setChess:" + connction % 2);
                flag = true;
            }
            if (connction % 2 == 1 && !flag2) {
                allPw.get(connction / 2).get(0).println("setChess:start");
            }
            String message = null;
            while ((message = br.readLine()) != null) {
                System.out.println(message);
                for (PrintWriter out1 : allPw.get(connction / 2)) {
                    if (pw != out1) {
                        out1.println(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(host + "客户端下线了");
            try {
                for (PrintWriter out1 : allPw.get(connction / 2)) {
                    out1.println("talkChess:Your opponent has lost connection,please quit and restart the game!~~");
                }
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
