package Client;

import Client.Ms.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Client extends JFrame implements Runnable {
    private static Image boardImg;
    private Ms ms;
    private Socket Socket;

    private boolean ableChess;
    private int x1;
    private int y1;
    private int win;
    private int chess;
    private int huiqiNum;
    private int huiqibuNum;
    private boolean start;
    private boolean firstClick;
    private boolean reStart;
    private boolean otherReStart;
    private boolean flag;// �������¿�ʼ��Ϣflag
    private boolean flag2;// ����������Ϣflag
    private boolean flag3;// ���ͻ�����Ϣflag
    private boolean flag4;// ����ͬ�����flag
    private boolean flag5;// ���Ͳ�ͬ�����flag
    private boolean surrender;// �ͻ�������flag
    private boolean beSurrender;// ���ܶԷ�����flag
    private boolean huiqi;// �ͻ��˻���flag
    private boolean behuiqi;// �ͻ��˽��ܻ���flag
    private boolean huiqiSuccess;
    private boolean huiqiFailure;
    private boolean huiqiShangxian;
    //�����Ƿ񲥷�
    private Boolean isPlay = false;
    //������Ϸ��ʾ��Ϣ

    //�������ӵ��ʱ��
    int maxTime = 0;
    //������ʱ���߳���
    Thread time = new Thread(new DoTime());
    //�׷�ʱ��
    int whiteTime = 0;
    //�ڷ�ʱ��
    int blackTime = 0;
    //����˫��ʣ��ʱ�����ʾ��Ϣ
    String blackMessage = "��ʱ������";
    String whiteMessage = "��ʱ������";
    //���������߳�
    Music mu;


    private String nickname;


    static {
        boardImg = Toolkit.getDefaultToolkit().getImage("�򵥺����������(1).png");
    }

    public Client() {
        System.out.println("�ͻ���������");
        ms = new Ms();
        try {
            this.Socket = new Socket("yandage.top", 8088);
            System.out.println("�Ѿ����ӷ����");
        } catch (UnknownHostException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }

    private void start() {

        this.setSize(1096, 808); // ���ô����С
        this.setTitle("��������Ϸ"); // ���ô������

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;// ��ȡ��Ļ�Ŀ��
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;// ��ȡ��Ļ�ĸ߶�
        this.setLocation((width - 1096) / 2, (height - 808) / 2); // ���ô����λ�ã����У�
        this.setResizable(false); // ���ô��岻���ԷŴ�
        this.setVisible(true);


        ServerHandler handler = new ServerHandler();
        Thread t = new Thread(handler);
        t.start();

        PostMessage post = new PostMessage();
        Thread t1 = new Thread(post);
        t1.start();

        Talk talk = new Talk();
        Thread t3 = new Thread(talk);
        t3.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int i = 0;

            public void run() {
                i++;
                if (i < 10) {
                    repaint();
                }
            }
        }, 100, 100);

        MouseAdapter l = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("x = " + x);
                System.out.println("y = " + y);
                // ������¿�ʼ
                if (e.getX() >= 711 && e.getX() <= 948 && e.getY() >= 153 && e.getY() <= 217) {
                    blackTime = maxTime;
                    whiteTime = maxTime;

                    if (maxTime > 0) {
                        blackMessage = blackTime / 3600 + ":" +
                                (blackTime / 60 - blackTime / 3600 * 60) + ":" +
                                (blackTime - blackTime / 60 * 60);
                        whiteMessage = whiteTime / 3600 + ":" +
                                (whiteTime / 60 - whiteTime / 3600 * 60) + ":" +
                                (whiteTime - whiteTime / 60 * 60);
                        time.resume();
                    } else {
                        blackMessage = "��ʱ������";
                        whiteMessage = "��ʱ������";
                    }
                    flag = true;
                    reStart = true;
                    post.huanxing();
                    if (reStart && otherReStart) {
                        reStart();
                    }
                }
                //�˳���Ϸ
                if (e.getX() >= 710 && e.getX() <= 945 && e.getY() >= 249 && e.getY() <= 308) {
                    JOptionPane.showMessageDialog(null, "�˳�");
                    System.exit(0);
                }
                // ����
                if (e.getX() >= 708 && e.getX() <= 946 && e.getY() >= 337 && e.getY() <= 399 && !surrender && !beSurrender) {
                    surrender = true;
                    flag2 = true;
                    win = 2;
                    post.huanxing();
                }

                //��������


                if (isPlay == false && e.getX() >= 868 && e.getX() <= 1075 && e.getY() >= 442 && e.getY() <= 503) {
                    mu = new Music();
                    Thread music = new Thread(mu);
                    music.start();
                    isPlay = true;

                }
                if (isPlay&& e.getX() >= 868 && e.getX() <= 1075 && e.getY() >= 442 && e.getY() <= 503) {
                    if (mu.player != null) {
                        mu.player.close();
                        isPlay = false;

                    }


                }
                //��Ϸ����
                if (e.getX() >= 872 && e.getX() <= 1075 && e.getY() >= 632 && e.getY() <= 694) {
                    String input = JOptionPane.showInputDialog("��������Ϸ�����ʱ�䣨��λ�����ӣ���");

                    try {
                        maxTime = Integer.parseInt(input) * 60;
                        if (maxTime < 0) {
                            JOptionPane.showMessageDialog(null, "��������ȷ��Ϣ�����������븺��");
                        }
                        if (maxTime > 0) {
                            blackTime = maxTime;
                            whiteTime = maxTime;
                            blackMessage = maxTime / 3600 + ":" +
                                    (maxTime / 60 - maxTime / 3600 * 60) + ":" +
                                    (maxTime - maxTime / 60 * 60);
                            whiteMessage = maxTime / 3600 + ":" +
                                    (maxTime / 60 - maxTime / 3600 * 60) + ":" +
                                    (maxTime - maxTime / 60 * 60);

                            time.start();
                            t.suspend();


                            repaint();


                        }
                        if (maxTime == 0) {

                            blackTime = maxTime;
                            whiteTime = maxTime;
                            blackMessage = "��ʱ������";
                            whiteMessage = "��ʱ������";

                            repaint();


                        }
//                if (maxTime > 0){
//                    JOptionPane.showMessageDialog(this,"�������");
//                }


                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(null, "����ȷ������Ϣ��");

                    }


                }


                //��Ϸ˵��
                if (e.getX() >= 946 && e.getX() <= 1072 && e.getY() >= 723 && e.getY() <= 776) {
                    JOptionPane.showMessageDialog(null, "tools�����Ʒ");
                }

                //����
                if (e.getX() >= 871 && e.getX() <= 1072 && e.getY() >= 536 && e.getY() <= 603) {
                    JOptionPane.showMessageDialog(null, "����һ����������Ϸ���ڰ�˫��˭�����ӳ���˭��Ӯ");
                }
                // �������
                if (x >= 670 && x <= 810 && y <= 502 && y >= 444 && !behuiqi) {
                    huiqiNum++;
                    if (!ableChess) {
                        huiqibuNum = 2;
                    } else {
                        huiqibuNum = 1;
                    }
                    if (huiqiNum > 3) {
                        huiqiShangxian = true;
                    } else {
                        flag3 = true;
                        huiqi = true;
                        post.huanxing();
                    }
                    repaint();
                }
                // ���ͬ�����
                if (behuiqi && x >= 669 && x <= 813 && y <= 593 && y >= 537) {
                    flag4 = true;
                    huiqi = true;
                    post.huanxing();
                    if (huiqi && behuiqi) {
                        huiqiSuccess = true;
                        removeChess();
                        huiqi = false;
                        behuiqi = false;
                    }
                    repaint();
                }
                //��ͬ�����
                if (behuiqi && x >= 669 && x <= 813 && y <= 693 && y >= 636) {
                    flag5 = true;
                    post.huanxing();
                    huiqiFailure = true;
                    behuiqi = false;
                    huiqi = false;
                    repaint();
                }


                // ����
                if (!ableChess && !huiqi && !behuiqi) {
                    if (x >= 66 && x <= 605 && y >= 141 && y <= 681) {
                        firstClick = true;
                        if (!ableChess && !ms.printed[(x - 66 + 18) / 36][(y - 141 + 18) / 36]) {
                            x1 = (x - 66 + 18) / 36;
                            y1 = (y - 141 + 18) / 36;
                            ms.allChess[(x - 66 + 18) / 36][(y - 141 + 18) / 36] = 1;
                            ms.printed[(x - 66 + 18) / 36][(y - 141 + 18) / 36] = true;
                            post.huanxing();
                            ableChess = true;
                            ms.addPoint(x1, y1);
                        }
                    }
                }


            }
        };
        this.addMouseListener(l);
    }


    private void reStart() {
        System.out.println("�Ծ����¿�ʼ");
        ms.chushihua();
        start = true;
        if (chess == 0) {
            ableChess = true;
            chess = 1;
        } else {
            ableChess = false;
            chess = 0;
        }
        firstClick = false;
        reStart = false;
        otherReStart = false;
        surrender = false;
        beSurrender = false;
        win = 0;
        huiqiNum = 0;
        huiqi = false;
        behuiqi = false;
        huiqiSuccess = false;
        huiqiFailure = false;
        huiqiShangxian = false;
        repaint();
    }

    public void paint(Graphics g) {
        paintBG(g);
        paintLine(g);
        paintChess(g);
        if (win != 0) {
            paintWin(g);
        }
        paintTip(g);
        paintLast(g);
    }


    private void paintLast(Graphics g) {
        g.setColor(Color.blue);
        if (firstClick) {
            g.drawRect(36 * x1 + 66 - 16, 36 * y1 + 144 - 18, 32, 32);
        }
        g.setColor(Color.black);
    }

    private void paintTip(Graphics g) {
        Font f = new Font("������ʽ", Font.BOLD, 25);
        g.setFont(f);
        String tip1 = "";
        String tip2 = "";
        if (reStart) {
            tip1 = "�����������¿�ʼ���ȴ��Է�ͬ��";
        } else if (otherReStart) {
            tip1 = "�Է��������¿�ʼ��ͬ������";
        } else if (huiqiSuccess) {
            if (ableChess) {
                tip1 = "����ɹ��������ֵ��Է�����";
                tip2 = "";
            } else {
                tip1 = "����ɹ��������ֵ�������";
                tip2 = "";
            }
            huiqiSuccess = false;
        } else if (huiqiFailure) {
            if (ableChess) {
                tip1 = "����ʧ�ܣ������ֵ��Է�����";
                tip2 = "";
            } else {
                tip1 = "����ʧ�ܣ������ֵ�������";
                tip2 = "";
            }
            huiqiFailure = false;
        } else if (huiqiShangxian) {
            tip1 = "һ����Ϸ���ֻ������3�λ���";
            huiqiShangxian = false;
        } else if (huiqi) {
            tip1 = "����������壬�ȴ��Է�ͬ��";
        } else if (behuiqi) {
            tip1 = "�Է�������壬ͬ��������";
        } else {
            if (chess == 0) {
                tip1 = "���Ǻڷ�";
                if (!start) {
                    tip2 = "������Ҫ�ȴ��׷�����";
                } else {
                    if (ableChess) {
                        tip2 = "�����ְ׷�����";
                    } else {
                        tip2 = "�����ֺڷ�����";
                    }
                }
            } else {
                tip1 = "���ǰ׷�";
                if (ableChess) {
                    tip2 = "�����ֺڷ�����";
                } else {
                    tip2 = "�����ְ׷�����";
                }
            }
            if (surrender) {
                tip2 = "����Ͷ�����Է���ʤ";
            } else if (beSurrender) {
                tip2 = "�Է�Ͷ�����ҷ���ʤ";
            }
        }
        g.drawString(tip1 + "  " + tip2, 215, 92);
    }

    private void paintWin(Graphics g) {
        Font f = new Font("������ʽ", Font.BOLD, 50);
        g.setFont(f);
        g.setColor(Color.RED);
        if (win == 1) {
            if (chess == 0) {
                g.drawString("����ʤ��", 130, 280);
            } else {
                g.drawString("����ʤ��", 130, 280);
            }
            ableChess = true;
        } else {
            if (chess == 0) {
                g.drawString("����ʤ��", 130, 280);
            } else {
                g.drawString("����ʤ��", 130, 280);
            }
            ableChess = true;
        }
        g.setColor(Color.black);
    }

    //��������
    private void paintChess(Graphics g) {
        int R = 16;
        int doubleR = 32;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int tempX = i * 36 + 66;
                int tempY = j * 36 + 141;
                if (ms.allChess[i][j] == 1) {
                    if (chess == 0) {
                        g.setColor(Color.black);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillOval(tempX - R, tempY - R, doubleR, doubleR);
                } else if (ms.allChess[i][j] == 2) {
                    if (chess == 0) {
                        g.setColor(Color.white);
                    } else {
                        g.setColor(Color.black);
                    }
                    g.fillOval(tempX - R, tempY - R, doubleR, doubleR);
                }
            }
        }
        g.setColor(Color.black);
        if (win == 0) {
            win = checkWin();
        }
    }

    private void paintBG(Graphics g) {
        g.drawImage(boardImg, 10, 32, null);
        g.setFont(new Font("����", 0, 20));
        g.setColor(Color.cyan);
        g.drawString("�ڷ�ʱ�䣺" + blackMessage, 105, 759);
        g.drawString("�׷�ʱ�䣺" + whiteMessage, 499, 759);
        g.setColor(Color.black);
    }

    private void paintLine(Graphics g) {
        for (int i = 0; i < 16; i++) {
            g.drawLine(66, 141 + 36 * i, 605, 141 + 36 * i);
            g.drawLine(66 + 36 * i, 141, 66 + 36 * i, 681);
        }
    }

    @Override
    public void run() {

    }

    class Talk implements Runnable {

        public void run() {
            try (OutputStream out = Socket.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                 PrintWriter pw = new PrintWriter(osw, true);) {
                System.out.println("����Ը��Է��������죬������������ǳ�");
                nickname = new Scanner(System.in).nextLine();
                System.out.println("���ڿ��Կ�ʼ������");
                while (true) {
                    String str = new Scanner(System.in).nextLine();
                    System.out.println(nickname + "˵:" + str);
                    pw.println("talkChess:" + nickname + "˵��" + str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PostMessage implements Runnable {

        @Override
        public synchronized void run() {
            try (OutputStream out = Socket.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                 PrintWriter pw = new PrintWriter(osw, true);) {
                while (true) {
                    wait();
                    if (flag) {
                        pw.println("setChess:reStart");
                        flag = false;
                        repaint();
                    } else if (flag2) {
                        pw.println("setChess:surrender");
                        flag2 = false;
                        repaint();
                    } else if (flag3) {
                        pw.println("setChess:huiqi");
                        flag3 = false;
                        repaint();
                    } else if (flag4) {
                        pw.println("setChess:agreehuiqi");
                        flag4 = false;
                    } else if (flag5) {
                        pw.println("setChess:disagreehuiqi");
                        flag5 = false;
                    }
//                    else if (flag6) {
//                        pw.println("setChess:ba");
//                    }
                    else {
                        pw.println("playChess:" + x1 + "," + y1);
                        repaint();
                    }
                }
            } catch (IOException | InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        public synchronized void huanxing() {
            notify();
        }

    }

    class DoTime implements Runnable {

        @Override
        public void run() {
            if (maxTime > 0) {
                while (true) {
                    if (chess == 0) {
                        blackTime--;
                        chess = 1;
                        if (blackTime == 0) {
                            JOptionPane.showMessageDialog(null, "��Ϸ�����׷�ʤ��");
                            try {
                                time.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        whiteTime--;
                        chess = 0;
                        if (whiteTime == 0) {
                            JOptionPane.showMessageDialog(null, "��Ϸ�����ڷ�ʤ��");
                            try {
                                time.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    blackMessage = blackTime / 3600 + ":" +
                            (blackTime / 60 - blackTime / 3600 * 60) + ":" +
                            (blackTime - blackTime / 60 * 60);
                    whiteMessage = whiteTime / 3600 + ":" +
                            (whiteTime / 60 - whiteTime / 3600 * 60) + ":" +
                            (whiteTime - whiteTime / 60 * 60);
                    System.out.println(whiteTime + blackTime);
                    repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }


        }

    }

    class ServerHandler implements Runnable {
        public void run() {
            try (InputStream in = Socket.getInputStream();
                 InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                 BufferedReader br = new BufferedReader(isr);) {
                String message = null;
                while ((message = br.readLine()) != null) {
                    String[] messHead = message.split(":");
                    if ("setChess".equals(messHead[0])) {
                        if ("start".equals(messHead[1])) {
                            start = true;
                            ableChess = false;
                        } else if ("reStart".equals(messHead[1])) {
                            otherReStart = true;
                            if (reStart && otherReStart) {
                                reStart();
                            }
                        } else if ("surrender".equals(messHead[1]) && !surrender) {
                            beSurrender = true;
                            win = 1;
                        } else if ("huiqi".equals(messHead[1])) {
                            behuiqi = true;
                            if (!huiqi) {
                                if (!ableChess) {
                                    huiqibuNum = 1;
                                } else {
                                    huiqibuNum = 2;
                                }
                            }
                        } else if ("agreehuiqi".equals(messHead[1])) {
                            behuiqi = true;
                            if (huiqi && behuiqi) {
                                huiqiSuccess = true;
                                removeChess();
                                huiqi = false;
                                behuiqi = false;
                            }
                        } else if ("disagreehuiqi".equals(messHead[1])) {
                            huiqiFailure = true;
                            behuiqi = false;
                            huiqi = false;
                        } else {
                            chess = Integer.parseInt(messHead[1]);
                            ableChess = true;
                        }
                        repaint();
                    } else if ("playChess".equals(messHead[0])) {
                        String[] str = messHead[1].split(",");
                        int x = Integer.parseInt(str[0]);
                        int y = Integer.parseInt(str[1]);
                        x1 = x;
                        y1 = y;
                        ms.addPoint(x1, y1);
                        if (ms.printed[x][y] == false) {
                            ms.allChess[x][y] = 2;
                            ms.printed[x][y] = true;
                            if (win == 0) {
                                ableChess = false;
                            }
                            repaint();
                        }
                    } else if ("talkChess".equals(messHead[0])) {
                        System.out.println(messHead[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int checkWin() {
        for (int i = 0; i != 16; ++i) {
            for (int j = 0; j != 16; ++j) {
                // ��
                if (j <= 10 && ms.allChess[i][j] != 0 && ms.allChess[i][j] == ms.allChess[i][j + 1]
                        && ms.allChess[i][j] == ms.allChess[i][j + 2]
                        && ms.allChess[i][j] == ms.allChess[i][j + 3]
                        && ms.allChess[i][j] == ms.allChess[i][j + 4]) {
                    return ms.allChess[i][j];
                }
                // ��
                if (i <= 10 && ms.allChess[i][j] != 0 && ms.allChess[i][j] == ms.allChess[i + 1][j]
                        && ms.allChess[i][j] == ms.allChess[i + 2][j]
                        && ms.allChess[i][j] == ms.allChess[i + 3][j]
                        && ms.allChess[i][j] == ms.allChess[i + 4][j]) {
                    return ms.allChess[i][j];
                }
                // ������б
                if (i <= 10 && j <= 10 && ms.allChess[i][j] != 0
                        && ms.allChess[i][j] == ms.allChess[i + 1][j + 1]
                        && ms.allChess[i][j] == ms.allChess[i + 2][j + 2]
                        && ms.allChess[i][j] == ms.allChess[i + 3][j + 3]
                        && ms.allChess[i][j] == ms.allChess[i + 4][j + 4]) {
                    return ms.allChess[i][j];
                }
                // ������б
                if (i <= 10 && j >= 4 && ms.allChess[i][j] != 0
                        && ms.allChess[i][j] == ms.allChess[i + 1][j - 1]
                        && ms.allChess[i][j] == ms.allChess[i + 2][j - 2]
                        && ms.allChess[i][j] == ms.allChess[i + 3][j - 3]
                        && ms.allChess[i][j] == ms.allChess[i + 4][j - 4]) {
                    return ms.allChess[i][j];
                }
            }
        }
        return 0;

    }

    private void removeChess() {
        for (int i = 0; i < huiqibuNum; i++) {
            Point p = ms.removePoint();
            if (p != null) {
                ms.allChess[p.x][p.y] = 0;
                ms.printed[p.x][p.y] = false;
            }
        }
        if (huiqibuNum == 1) {
            if (ableChess) {
                ableChess = false;
            } else {
                ableChess = true;
            }
        }
        Point p = ms.getPoint();
        if (p != null) {
            x1 = p.x;
            y1 = p.y;
        }
        repaint();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
