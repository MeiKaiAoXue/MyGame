package com.msy.mygame.server;

import com.msy.mygame.client.model.Room;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnect {
    private static final int PORT = 2023;//服务器侦听端口
    private static final int MAX = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX);//线程池
    private static ConcurrentHashMap<Socket, PrintWriter> clientOutputStreams = new ConcurrentHashMap<>();

    public ServerConnect() {

        ServerSocket serverSocket = null;
        try {
            //创建服务器侦听端口
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器就绪，等待客户端连接。。。");

            while (true) {
                //服务器等待连接
                Socket cSocket = serverSocket.accept();
                System.out.println("客户端 : "+ cSocket.getInetAddress().getHostAddress() + " 连接成功" );

                //用该套接字的输出流创建对象输出流，并加入到对象输出流的哈希表里
                PrintWriter out = new PrintWriter(cSocket.getOutputStream(),true);
                clientOutputStreams.put(cSocket, out);

                //用接收到的客户端Socket来创建子线程
                Runnable clientHandler = new ClientHandler(cSocket, out);
                executor.execute(clientHandler);//在线程池中执行子线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    线程处理器
     */
    class ClientHandler implements Runnable {
        private RoomManager roomManager;
        private Socket cSocket = null;
        private PrintWriter out = null;
        private BufferedReader in = null;
        public ClientHandler(Socket cSocket, PrintWriter out) {
            this.cSocket = cSocket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                String delimiter = ",";
                in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                while (true) {
                    String receiveInput = null;
                    while (true) {
                            receiveInput = in.readLine();
                            if (receiveInput != null) {
                                break;
                            }
                        System.out.println("服务器接收到null");

                    }
                    String[] tokens = receiveInput.split(delimiter);
                    System.out.println("服务器切割字符串");
                    //客户端传来有关房间创建的数据
                    if (tokens[0].equals("roomInfo") ) {
                        System.out.println("服务器创建房间中");
                        Room room = RoomManager.createRoom(tokens[1], tokens[2],cSocket);
                        System.out.println("房间创建成功");
                        if (!(room.getSocket1() == cSocket)) {//如果第一人不是当前客户端，则将该客户端添加到房间里；如果第一人是当前客户端，则不再添加该客户端到房间中，因为是以第一人来创建房间的
                            room.addPerson(cSocket);
                        }
                        String fullFlag = room.isFull();//每次添加一个客户端进入房间后，都要判断是否满员
                        //将满员信息发给所有客户端，告诉他们是否开始游戏
                        broadcastMessageToAll(fullFlag);
                    }

                    //客户端传来有关玩家位置的数据
                    if (tokens[0].equals("playerInfo") ) {
                        //服务器时时刻刻接收来自某个客户端的对象数据，并广播给其他所有客户端
                        System.out.println("接收客户端" + cSocket.getInetAddress().getHostAddress() + "的位置信息");
                        broadcastMessageToOther(receiveInput);
                    }

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress().getHostAddress() + " 服务器输入流已断开");
                    }

                    if (out != null) {
                        out.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress().getHostAddress() + " 服务器输出流已断开");
                    }
                    if (cSocket != null) {
                        cSocket.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress().getHostAddress() + " 已断开");
                    }
                    //客户端断开连接时，移除对应的输出流
                    if (!clientOutputStreams.contains(cSocket)) {
                        clientOutputStreams.remove(cSocket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //向所有客户端播报数据的方法
        private void broadcastMessageToOther(String receivedString) {
            //给客户端集合上锁，避免一个客户端同时接收两个其他客户端的位置，因为客户端接收时只有一个otherPerson对象
            synchronized (clientOutputStreams) {
                for (PrintWriter output : clientOutputStreams.values()) {
                    //实现数据广播
                    if (output != out) {
                        output.println(receivedString);
                        System.out.println("服务器转发PlayerInfo...");
                    }
                }
            }
        }

        private void broadcastMessageToAll(String receivedString) {
            //给客户端集合上锁，避免一个客户端同时接收两个其他客户端的位置，因为客户端接收时只有一个otherPerson对象
            synchronized (clientOutputStreams) {
                for (PrintWriter output : clientOutputStreams.values()) {
                    //实现数据广播
                    output.println(receivedString);
                    System.out.println("服务器转发RoomInfo...");
                }
            }
        }

    }

    public static void main(String[] args) {
        new ServerConnect();
    }
}
