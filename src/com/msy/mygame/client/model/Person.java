package com.msy.mygame.client.model;

import com.msy.mygame.client.view.LoginFrame;

/**
 * 游戏人物（人物撞到怪兽减 1 生命值，生命值为 0 或 触碰到屏幕左边缘 判定为该玩家游戏结束）
 * 1.状态：血量，距离，金币得分，总分数
 * 2.动作：跳，滑翔，溜滑
 */
public class Person {
    private int hp = 3;
    private int distance = 0;
    private int gold = 0;
    private int score = 0;
    private int x = 0;
    private int y = 0;

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
    }

    public static Person getPerson() {
        return person;
    }



    public void jump() {}
    public void glide() {}
    public void slide() {}

}
