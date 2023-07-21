package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.OtherPerson;
import com.msy.mygame.client.model.Person;
import com.msy.mygame.client.view.LoginFrame;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnect implements Runnable{
    //网络变量
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 2023;
    private Socket cSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public void run() {
        try {
            //创建客户端Socket
            cSocket = new Socket("127.0.0.1", PORT);
            System.out.println("已连接服务器 " + SERVER_IP);


            OtherPerson otherPerson = OtherPerson.getOtherPerson();

            /*多线程处理实现同时发送自身数据，接收来自服务器的播报数据*/
            Thread inputThread = new Thread(() -> {
                //时刻获取其他玩家的位置信息
                   try {
                       in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                       while (!GamePanel.isOver) {
                           System.out.println("客户端正在接收数据");
                          String receiveInput = in.readLine();
                          String delimiter = ",";
                          String[] tokens = receiveInput.split(delimiter);
                          int num = tokens.length;
                           for (int i = 0; i < num; i++) {
                               otherPerson.setOtherX(0);
                               otherPerson.setOtherY(1);
                           }
                       }
                       //下一步是绘制其他玩家的位置图像，在哪里写这段代码，怎么写啊，想用一个点代替
                       //在某一时刻，服务器将所有玩家的位置信息用集合存起来，然后遍历这个集合，
                       //将此刻所有玩家的位置信息一起发送出去，最后再在每个客户端上用这些信息画出所有当前的位置图像
                   } catch (IOException e) {
                       e.printStackTrace();
                   } finally {
                       try {
                           if (in != null) {
                               in.close();
                           }
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }
                   }
            });
            inputThread.start();


            //时刻发送玩家的总得分数据进行实时滚动排行，
            // 发送玩家的位置信息，在各个客户端同步显示其他玩家的位置
            Thread outputThread = new Thread(() -> {
                Person person = Person.getPerson();
                String sendString = "";

                try {
                    while (!GamePanel.isOver) {
                        sendString = sendString.concat(Integer.toString(person.getX()));
                        sendString = sendString.concat(",");
                        sendString = sendString.concat(Integer.toString(person.getY()));
                        out = new PrintWriter(cSocket.getOutputStream(), true);
                        out.println(sendString);

                        Thread.sleep(50);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            });
            outputThread.start();

            //等待游戏结束时，关闭套接字
            while (true) {
                if (GamePanel.isOver) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //玩家阵亡后，不再向服务器发送数据时，表示客户端（输入输出流，套接字）与服务器断开连接
            try {
                if (cSocket != null) {
                    cSocket.close();
                    System.out.println("客户端 " + cSocket.getInetAddress() + " ： 我的套接字关闭");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /*
    打包要发送本地用户的数据，包括玩家目前总得分 和 实时位置信息
     */
//    class Data implements Serializable{
//        private int score = 0;
//        private int x = 0;
//        private int y = 0;
//
//        Person person = Person.getPerson();
//        public Data() {
//            //this.score = person.getScore();
//            this.x = person.getX();
//            this.y = person.getY();
//        }
//    }

}
