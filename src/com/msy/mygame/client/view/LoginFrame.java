package com.msy.mygame.client.view;

import com.msy.mygame.client.controller.ClientConnect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 游戏登陆界面
 * 1.输入用户名和密码
 * 2.输入服务器ip,端口
 * 3.TCP连接服务器
 */
public class LoginFrame extends JFrame {


    //GUI变量
    JLabel usernameLabel = null;
    JLabel pwLabel = null;
    JLabel networkLabel = null;
    public JTextField usernameTextField = null;
    JTextField pwTextField = null;
    JTextField networkTextField = null;
    JButton loginButton = null;
    JButton cancelButton = null;
    JButton signupButton = null;



    public LoginFrame() {
        //初始化GUI变量
        networkLabel = new JLabel("ip: ");
        networkLabel.setBounds(70,200,40, 30);
        //this.add(networkLabel);

        networkTextField = new JTextField();
        networkTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        networkTextField.setOpaque(false);
        networkTextField.setBounds(110,200,150,30);
        //this.add(networkTextField);

        usernameLabel = new JLabel("username: ");
        usernameLabel.setBounds(70,250,100,40);
        this.add(usernameLabel);

        pwLabel = new JLabel("password: ");
        pwLabel.setBounds(70,300,100,40);
        this.add(pwLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        usernameTextField.setBounds(200,250,100,40);
        usernameTextField.setOpaque(false);//设置背景透明
        this.add(usernameTextField);

        pwTextField = new JTextField();
        pwTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        pwTextField.setBounds(200,300,100,40);
        pwTextField.setOpaque(false);//设置背景透明
        this.add(pwTextField);

        signupButton = new JButton("signup");
        signupButton.setBounds(310,360,100,30);
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("已点击注册按钮");
            }
        });
        this.add(signupButton);

        loginButton = new JButton("login");
        loginButton.setBounds(90,360,100,30);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("已点击登陆按钮");
                String userName = usernameTextField.getText();
                String password = pwTextField.getText();
                if (userName.equals("mei") && password.equals("123456")) {
                    //登陆成功
                    JOptionPane.showMessageDialog(null, "欢迎 " + userName + " 来到我的游戏");
                    //跳转到下一界面
                    dispose();
                    new MainFrame();
                    //关闭当前界面

                } else if("".equals(userName) || "".equals(password)){
                    //不能为空
                    JOptionPane.showMessageDialog(null, "用户名 / 密码不能为空，请重新输入！");
                }else{
                    JOptionPane.showMessageDialog(null, "用户名 / 密码输入错误，请重新输入！");
                }


            }
        });
        this.add(loginButton);


        cancelButton = new JButton("cancel");
        cancelButton.setBounds(200,360,100,30);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("已点击退出按钮");
                //关闭当前界面
                dispose();
            }
        });
        this.add(cancelButton);




        //创建背景面板，添加到窗体上
        LoginPanel loginPanel = new LoginPanel();
        this.add(loginPanel);

        //设置窗体属性
        this.setSize(800,500);
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setIconImage(new ImageIcon("Image/115.png").getImage());//设置窗体图标
        this.setVisible(true);

        //连接服务器
        new ClientConnect();

    }

    //画板
    class LoginPanel extends JPanel {
        //背景图片变量
        Image loginBackground;
        public LoginPanel() {
            try {//加载背景图片
                loginBackground = ImageIO.read(new File("Image/login.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //绘制背景图片方法
        public void paint(Graphics g) {
            super.paint(g);
            //绘制背景图片
            g.drawImage(loginBackground,0,0,800,500,null);
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }

}
