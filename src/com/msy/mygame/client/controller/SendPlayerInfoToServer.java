package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.OtherPerson;
import com.msy.mygame.client.model.Person;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendPlayerInfoToServer implements Runnable{
//    private Socket cSocket = ClientConnect.cSocket;
    private PrintWriter out = ClientConnect.out;
    @Override
    public void run() {
        System.out.println("进入发送本地玩家位置的方法");
        Person person = Person.getPerson();
        String sendString = "";

        try {
            while (!GamePanel.isOver) {
                sendString = sendString.concat("playerInfo");
                sendString = sendString.concat(",");
                sendString = sendString.concat(Person.getPerson().getShadowOther().getId());
                sendString = sendString.concat(",");
                sendString = sendString.concat(Integer.toString(person.getX()));
                sendString = sendString.concat(",");
                sendString = sendString.concat(Integer.toString(person.getY()));
//                out = new PrintWriter(cSocket.getOutputStream(), true);
                out.println(sendString);
                System.out.println("客户端： " + ClientConnect.cSocket.getInetAddress().getHostAddress() + " 发送位置信息");

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            if (out != null) {
//                out.close();
//            }
        }
    }
}
