package com.msy.mygame.client.model;

import com.msy.mygame.client.view.GameFrame;
import com.msy.mygame.client.view.LoginFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 游戏人物（人物撞到怪兽减 1 生命值，生命值为 0 或 触碰到屏幕左边缘 判定为该玩家游戏结束）
 * 1.状态：血量，距离，金币得分，总分数
 * 2.动作：跑，跳，滑翔，溜滑
 */
public class Person implements PaintElement{
    private int hp;//人物的生命值
    private int distance;//人物跑动的距离
    private int gold;//人物捡拾的金币
    private int score;//人物的总得分
    private int x;//人物的坐标位置
    private int y;

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    private final int WIDTH = 80;//人物的宽
    private final int HEIGHT = 80;//人物的高

    private Image image;//人物当前显示图片
    private ArrayList<Image> images = new ArrayList<>();//人物的所有图片
    private int index;//用来切换图片的下标

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private String userName = "";

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /*
        饿汉式单例模式
         */
    private static final Person person = new Person(new LoginFrame().usernameTextField.getText());
    private Person(String userName) {
        this.userName = userName;
        //加载人物所有图片
        loadImages();
        //默认初始状态显示图片为第一张图片
        image = images.get(0);

        //人物初始位置
        x = 90;
        y = 580;

        //玩家初始属性
        hp = 3;
        index = 0;
        score = 0;
        distance = 0;
        gold = 0;

    }
    public static Person getPerson() {
        return person;
    }

    //加载玩家所有图片
    private void loadImages() {
        for (int i = 0; i < 9; i++) {
            try {
                images.add(ImageIO.read(new File("Image/" + (i+1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //判断移出屏幕
    public boolean isOutOfBounds() {
        if (x == 0) {
            return true;
        }
        return false;
    }


    //人物一直向前移动
    public void step() {
        image = images.get(index ++  % images.size());//切换人物图片
        distance += 3;//玩家跑动距离自动增加
    }
    //人物跳跃与回落
    public void jump() {
        person.setY(y - 50);//可以加一个线程，让人物跳起有个动画效果，以后实现
        try {
            Thread.sleep(500);//模拟滞空
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        person.setY(y + 50);//回落
    }
    //滑翔需要监听连续两次的向上键，以后再实现，可以问GPT
    public void glide() {
    }
    //下滑需要新的素材来实现，留着以后实现
    public void slide() {
    }
    //向右移动
    public void rightStep() {
        if (x >= GameFrame.WIDTH - this.WIDTH) {
            person.setX(GameFrame.WIDTH - this.WIDTH);
        } else {
            person.setX(x + 50);
        }
    }
    //向左移动
    public void leftStep() {
        if (x >= 50) {
            person.setX(x - 50);
        } else {
            person.setX(0);
        }
    }

    @Override
    public void painElement(Graphics g) {
        g.drawImage(image, x, y, getWIDTH(), getHEIGHT(),null);
    }
}
