package com.msy.mygame.client.view;

import com.msy.mygame.client.controller.GamePanel;

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
        System.out.println("1");
        GamePanel gamePanel = new GamePanel();
        System.out.println("2");
        this.add(gamePanel);
        System.out.println("3");
        gamePanel.action();
        System.out.println("4");
        this.addKeyListener(gamePanel);
        System.out.println("5");
        //设置窗体属性
        this.setSize(WIDTH,HEIGHT);
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());//设置窗体图标
        System.out.println("游戏界面可显示");
        this.setVisible(true);

        while(true){
            if(gamePanel.isOver){
                dispose();//关闭窗口
                System.out.println("关闭正式游戏界面");
                break;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
