package com.msy.mygame.client.view;

import com.msy.mygame.client.controller.RoomAction;

import javax.swing.*;
import java.awt.*;

public class WaitFrame extends JFrame implements Runnable{

    JLabel waitLabel;
    JProgressBar waitBar;
    String roomId = "";
    String personNum = "";
    public WaitFrame(String roomId, String personNum) {

        this.roomId = roomId;
        this.personNum = personNum;

        //添加等待标签
        waitLabel = new JLabel("等待其他玩家连接。。。");
        this.add(BorderLayout.CENTER, waitLabel);

        //添加等待进度条
        waitBar = new JProgressBar();
        waitBar.setIndeterminate(true);
        this.add(BorderLayout.SOUTH, waitBar);

        //设置窗体基本属性
        this.setSize(400,300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());
        this.setVisible(true);
    }

    @Override
    public void run() {

        //将房间号和玩家数发给服务器
        RoomAction.sendToServer(roomId, personNum);
        //等待服务器发送人满信号
        String fullFlag = RoomAction.waitOtherPlayer();

        //直到人满后才启动加载界面
        while (true) {
            if (fullFlag.equals("true")) {
                //关闭等待窗口
                dispose();
                System.out.println("关闭等待界面");
                //打开加载界面
                new Thread(new WindowFrame()).start();
                System.out.println("启动加载界面");
                break;
            }
        }
    }
}
