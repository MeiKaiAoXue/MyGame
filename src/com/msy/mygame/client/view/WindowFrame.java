package com.msy.mygame.client.view;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏加载界面
 * 1.加载背景
 * 2.加载进度条
 * 3.玩家数未满时，显示等待信息，直到玩家数满足一开始选择多人游戏的条件
 */
public class WindowFrame extends JFrame implements Runnable{
    JLabel windowBackground;
    JProgressBar bar;

    public WindowFrame() {
        windowBackground = new JLabel(new ImageIcon("Image/hbg.jpg"));
        this.add(BorderLayout.NORTH, windowBackground);

        bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setBackground(Color.BLUE);
        this.add(BorderLayout.SOUTH, bar);

        //设置窗体基本属性
        this.setSize(500,300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());;
        this.setVisible(true);
    }


    @Override
    public void run() {
        //利用线程来呈现动态进度条的效果

        int [] values = {0,0,1,3,10,11,15,18,23,32,40,47,55,66,76,86,89,95,99,99,99,100};
        for (int i = 0; i < values.length; i++) {
            bar.setValue(values[i]);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //显示下一界面
        new GameFrame();
        //关闭加载界面
        dispose();
    }
}
