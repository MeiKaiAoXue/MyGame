package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.OtherPerson;
import com.msy.mygame.client.model.Room;

import javax.print.attribute.standard.MediaSize;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceivePlayerInfoFromServer implements Runnable{
//    OtherPerson otherPerson = OtherPerson.getOtherPerson();
//    private Socket cSocket = ClientConnect.cSocket;
    private BufferedReader in = ClientConnect.in;
    @Override
    public void run() {
        //时刻获取其他玩家的位置信息
        try {
//            in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            while (!GamePanel.isOver) {
                //在此处要判断是哪位其他玩家传来的位置消息，信息格式"playerInfo", "哪个player", "playerX", "playerY"
                System.out.println("客户端正在接收其他玩家的位置数据");
                String receiveInput = in.readLine();
                String delimiter = ",";
                String[] tokens = receiveInput.split(delimiter);
                if (tokens[1].equals("1")) {
                    synchronized (Room.others) {
                        //当此处改变位置的时候，其他玩家的绘图不能拿到锁
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherX(Integer.parseInt(tokens[2]));
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherY(Integer.parseInt(tokens[3]));
                    }
                } else if (tokens[1].equals("2")) {
                    synchronized (Room.others) {
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherX(Integer.parseInt(tokens[2]));
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherY(Integer.parseInt(tokens[3]));
                    }
                } else if (tokens[1].equals("3")) {
                    synchronized (Room.others) {
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherX(Integer.parseInt(tokens[2]));
                        Room.others.get(Integer.parseInt(tokens[1]) - 1).setOtherY(Integer.parseInt(tokens[3]));
                    }
                }
            }

            Thread.sleep(5);
            //下一步是绘制其他玩家的位置图像，在哪里写这段代码，怎么写啊，想用一个点代替
            //在某一时刻，服务器将所有玩家的位置信息用集合存起来，然后遍历这个集合，
            //将此刻所有玩家的位置信息一起发送出去，最后再在每个客户端上用这些信息画出所有当前的位置图像
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
