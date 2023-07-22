package com.msy.mygame.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RoomAction {
    private static PrintWriter out = null;
    private static BufferedReader in = null;

    //将房间号和玩家数发给服务器
    public static void sendToServer(String roomId, String personNum) {
        try {
            out =  new PrintWriter(ClientConnect.cSocket.getOutputStream(), true);
            String sendString = "";
            sendString = sendString.concat("roomInfo");
            sendString = sendString.concat(",");
            sendString = sendString.concat(roomId);
            sendString = sendString.concat(",");
            sendString = sendString.concat(personNum);
            out.println(sendString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    //等待服务器发送人满信号
    public static String waitOtherPlayer() {
        String fullFlag = "";
        try {
            in = new BufferedReader(new InputStreamReader(ClientConnect.cSocket.getInputStream()));
            while (true) {
                 fullFlag = in.readLine();
                if (fullFlag.equals("true")) {
                    System.out.println("玩家已满，开始游戏");
                    break;
                }
                System.out.println("等待其他玩家加入房间");
                Thread.sleep(5);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return fullFlag;

    }
}
