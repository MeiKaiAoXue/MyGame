package com.msy.mygame.client.view;

import com.msy.mygame.client.controller.GamePanel;
import com.msy.mygame.client.controller.ReceivePlayerInfoFromServer;
import com.msy.mygame.client.controller.SendPlayerInfoToServer;

import javax.swing.*;

/**
 * 正式游戏界面
 * 1.显示窗体
 * 2.承载panel容器
 */
public class GameFrame extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    public GameFrame() {

        //正式游戏开始时开启服务器实时通信
        new Thread(new SendPlayerInfoToServer()).start();
        new Thread(new ReceivePlayerInfoFromServer()).start();

        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        gamePanel.action();
        this.addKeyListener(gamePanel);
        //设置窗体属性
        this.setSize(WIDTH,HEIGHT);
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());//设置窗体图标
        this.setVisible(true);

        while(true){
            if(gamePanel.isOver){
                dispose();//关闭窗口
                System.out.println("关闭正式游戏界面");
                break;
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
