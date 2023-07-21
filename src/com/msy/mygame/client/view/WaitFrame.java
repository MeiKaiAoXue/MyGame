package com.msy.mygame.client.view;

import javax.swing.*;
import java.awt.*;

public class WaitFrame extends JFrame implements Runnable{

    JLabel waitLabel;
    JProgressBar waitBar;
    public WaitFrame() {

        //添加等待标签
        waitLabel = new JLabel("等待其他玩家连接。。。");
        this.add(BorderLayout.CENTER, waitLabel);

        //添加等待进度条
        waitBar = new JProgressBar();
        waitBar.setIndeterminate(true);
        this.add(BorderLayout.SOUTH, waitBar);

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
        while (true) {
            if (isFull) {
                //关闭等待窗口
                dispose();
                //打开加载界面
                new Thread(new WindowFrame()).start();
                break;
            }
        }
    }
}
