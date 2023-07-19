package com.msy.mygame.client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

/**
 * 游戏菜单界面
 * 1.单人游戏
 * 2.多人游戏（输入房间号)
 * 3.帮助
 * 4.退出
 */
public class MainFrame extends JFrame implements MouseListener {
    JLabel start = null, help = null, exit = null;

    public MainFrame() {
        start = new JLabel(new ImageIcon("Image/hh1.png"));
        start.setBounds(360, 250, 100, 40);
        start.setEnabled(false);
        start.addMouseListener(this);
        this.add(start);

        help = new JLabel(new ImageIcon("Image/hh2.png"));
        help.setBounds(360, 300, 100, 40);
        help.setEnabled(false);
        help.addMouseListener(this);
        this.add(help);

        exit = new JLabel(new ImageIcon("Image/hh3.png"));
        exit.setBounds(360, 350, 100, 40);
        exit.setEnabled(false);
        exit.addMouseListener(this);
        this.add(exit);


        //添加背景面板
        this.add(new MainPanel());

        //设置窗体基本属性
        this.setSize(1000,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());;
        this.setVisible(true);
    }

    class MainPanel extends JPanel {
        Image mainBackground;
        public MainPanel() {
            try {
                mainBackground = ImageIO.read(new File("Image/main.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(mainBackground, 0,0,1000,600,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(start)) {
            //关闭当前界面
            //dispose();
            //跳转下一界面
            new Thread(new WindowFrame()).start();

        } else if (e.getSource().equals(help)) {
            JOptionPane.showMessageDialog(null, "本开发者也不知道怎么帮助你！！！");
        } else if (e.getSource().equals(exit)){
            System.out.println("退出主界面");
            dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource().equals(start)) {
            start.setEnabled(true);
        } else if (e.getSource().equals(help)) {
            help.setEnabled(true);
        } else if (e.getSource().equals(exit)){
            exit.setEnabled(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource().equals(start)) {
            start.setEnabled(false);
        } else if (e.getSource().equals(help)) {
            help.setEnabled(false);
        } else if (e.getSource().equals(exit)){
            exit.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
