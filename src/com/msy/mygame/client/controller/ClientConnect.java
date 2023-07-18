package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.Person;
import com.msy.mygame.client.view.LoginFrame;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnect {
    //网络变量
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 2023;
    private Socket cSocket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;
    public ClientConnect() {
        try {
            //创建客户端Socket
            cSocket = new Socket("127.0.0.1", 2021);
            System.out.println("已连接服务器" + SERVER_IP);

            //获取对象输入输出流
            outputStream = new ObjectOutputStream(cSocket.getOutputStream());
            inputStream = new ObjectInputStream(cSocket.getInputStream());

            /*多线程处理实现同时发送自身数据，接收来自服务器的播报数据*/
            Thread inputThread = new Thread(() -> {
                //时刻获取其他玩家的位置信息

                   try {
                       while (true) {
                           Data data_receive = (Data) inputStream.readObject();
                       }
                       //下一步是绘制其他玩家的位置图像，在哪里写这段代码，怎么写啊，想用一个点代替
                       //在某一时刻，服务器将所有玩家的位置信息用集合存起来，然后遍历这个集合，
                       //将此刻所有玩家的位置信息一起发送出去，最后再在每个客户端上用这些信息画出所有当前的位置图像

                   } catch (IOException | ClassNotFoundException e) {
                       throw new RuntimeException(e);
                   } finally {
                       try {
                           inputStream.close();
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }
                   }
            });
            inputThread.start();

            //时刻发送玩家的总得分数据进行实时滚动排行，
            // 发送玩家的位置信息，在各个客户端同步显示其他玩家的位置
            try {
                while (true) {
                    Data data_send = new Data();
                    outputStream.writeObject(data_send);
                    outputStream.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //玩家阵亡后，不再向服务器发送数据时，表示客户端（输入输出流，套接字）与服务器断开连接
            try {
                outputStream.close();
                inputStream.close();
                cSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /*
    打包要发送的数据，包括玩家目前总得分 和 实时位置信息
     */
    class Data {
        private int score = 0;
        private int x = 0;
        private int y = 0;

        Person person = Person.getPerson();
        public Data() {
            this.score = person.getScore();
            this.x = person.getX();
            this.y = person.getY();
        }
    }
}
