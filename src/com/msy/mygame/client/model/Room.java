package com.msy.mygame.client.model;

import java.net.Socket;
import java.util.ArrayList;

/*
允许两人或三人的房间对象
 */
public class Room {
    private String roomId = "";
    private String personNum = "";
    private Socket socket1, socket2 = null, socket3 = null;
    public static ArrayList<OtherPerson> others = new ArrayList<>(3);;//目前只实现本地开一个房间

    public Socket getSocket1() {
        return socket1;
    }

    public Socket getSocket2() {
        return socket2;
    }

    public Socket getSocket3() {
        return socket3;
    }

    public Room(String roomId, String personNum, Socket socket) {
        this.roomId = roomId;
        this.personNum = personNum;
        this.socket1 = socket;

        OtherPerson otherPerson = new OtherPerson(socket1, "1");
        others.add(otherPerson);
        Person.getPerson().setShadowOther(otherPerson);
        System.out.println("房主已确定");
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    //将客户端加入已存在的房间
    public void addPerson(Socket socket) {
        if (socket2 == null) {
            socket2 = socket;
            OtherPerson otherPerson = new OtherPerson(socket2, "2");
            others.add(otherPerson);
            Person.getPerson().setShadowOther(otherPerson);
            System.out.println("第二位玩家进入房间");
        } else {
            socket3 = socket;
            OtherPerson otherPerson = new OtherPerson(socket3, "3");
            others.add(otherPerson);
            Person.getPerson().setShadowOther(otherPerson);
            System.out.println("第三位玩家进入房间");
        }

        System.out.println("其他玩家加入房间");
    }

    //判断房间是否满员
    public String isFull() {
        if (personNum.equals("3")) {
            if (socket2 != null && socket3 == null) {
                return "false";
            } else if (socket2 != null && socket3 != null) {
                return "true";
            }
        } else if (personNum.equals("2")) {
            if (socket2 != null && socket3 == null) {
                System.out.println("判断方法： 人数为 ture");
                return "true";
            }
        }
        System.out.println("判断方法： 人数为 false");
        return "false";
    }
}
