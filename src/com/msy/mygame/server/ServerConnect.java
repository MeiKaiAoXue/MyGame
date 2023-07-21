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
    private static final int MAX = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX);//线程池
    private static final ConcurrentHashMap<Socket, PrintWriter> clientOutputStreams = new ConcurrentHashMap<>();

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
                in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                while (true) {
                    //服务器时时刻刻接收来自某个客户端的对象数据，并广播给其他所有客户端
                    String receivedString = in.readLine();
                    broadcastMessage(receivedString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                        System.out.println("服务器： 客户端 " + cSocket.getInetAddress() + " 服务器输入流已断开");
                    }

                    if (out != null) {
                        out.close();
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
                    e.printStackTrace();
                }
            }
        }

        //向所有客户端播报数据的方法
        private void broadcastMessage(String receivedString) {
            //给客户端集合上锁，避免一个客户端同时接收两个其他客户端的位置，因为客户端接收时只有一个otherPerson对象
            synchronized (clientOutputStreams) {
                for (PrintWriter out : clientOutputStreams.values()) {
                    //写入缓存区并清空缓存，实现数据广播
                    out.println(receivedString);
                }
                clientOutputStreams.notify();
            }
        }
    }

    public static void main(String[] args) {
        new ServerConnect();
    }
}
