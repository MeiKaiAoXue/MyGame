package com.msy.mygame.server;

import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnect {
    private static final int PORT = 2023;//服务器侦听端口
    private static ExecutorService executor = Executors.newFixedThreadPool(10);//线程池
    private static final ConcurrentHashMap<Socket, ObjectOutputStream> clientOutputStreams = new ConcurrentHashMap<>();

    public ServerConnect() {

        ServerSocket serverSocket = null;
        try {
            //创建服务器侦听端口
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器就绪，等待客户端连接。。。");

            while (true) {
                //服务器等待连接
                Socket cSocket = serverSocket.accept();
                System.out.println("客户端连接成功" + cSocket.getInetAddress().getHostAddress());

                //用该套接字的输出流创建对象输出流，并加入到对象输出流的哈希表里
                ObjectOutputStream outputStream = new ObjectOutputStream(cSocket.getOutputStream());
                clientOutputStreams.put(cSocket, outputStream);

                //用接收到的客户端Socket来创建子线程
                Runnable clientHandler = new ClientHandler(cSocket, outputStream);
                executor.execute(clientHandler);//在线程池中执行子线程

                //服务器端到底需不需要显式关闭
//                if (cSockets.isEmpty()) {
//                    System.out.println("所有玩家已结束比赛，服务器端关闭");
//                    break;
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //服务器不需要断开吧
//        finally {
//            //直到最后所有玩家结束游戏时，ServerSocket才断开,存疑
//            //解答：当出现异常或手动关闭服务器端，这段结束代码才执行
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    /*
    线程处理器
     */
    class ClientHandler implements Runnable {
        private Socket cSocket = null;
        private ObjectOutputStream outputStream = null;
        private ObjectInputStream inputStream = null;
        public ClientHandler(Socket cSocket, ObjectOutputStream outputStream) {
            this.cSocket = cSocket;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            try {
                inputStream = new ObjectInputStream(cSocket.getInputStream());
                while (true) {
                    //服务器时时刻刻接收来自某个客户端的对象数据，并广播给其他所有客户端
//                    try {
//                        Object receivedData = inputStream.readObject();
//                        broadcastMessage(receivedData);
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress() + " 服务器输入流已断开");
                    }

                    if (outputStream != null) {
                        outputStream.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress() + " 服务器输出流已断开");
                    }
                    if (cSocket != null) {
                        cSocket.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress() + " 已断开");
                    }
                    //客户端断开连接时，移除对应的输出流
                    if (!clientOutputStreams.contains(cSocket)) {
                        clientOutputStreams.remove(cSocket);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //向所有客户端播报数据的方法
        private synchronized void broadcastMessage(Object receivedData) {
            for (ObjectOutputStream output : clientOutputStreams.values()) {
                try {
                    //写入缓存区并清空缓存，实现数据广播
                    output.writeObject(receivedData);
                    output.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new ServerConnect();
    }
}
