package com.msy.mygame.server;

import com.msy.mygame.client.model.Room;

import java.net.Socket;
import java.util.LinkedList;

/*
服务器端房间管理器
 */
public class RoomManager {
    private static LinkedList<Room> rooms = new LinkedList<>();

    public static synchronized Room createRoom(String roomId, String personNum, Socket socket) {
        for (Room room:
             rooms) {
            if (room.getRoomId().equals(roomId)) {
                System.out.println("返回已存在房间");
                return room;
            }
        }

        //创建新房间，以房主为第一人
        Room newRoom = new Room(roomId, personNum, socket);
        rooms.add(newRoom);
        System.out.println("返回新创建的房间");
        return newRoom;
    }

    private Socket cSocket = null;

    public RoomManager(Socket cSocket) {
        this.cSocket = cSocket;
    }
}
