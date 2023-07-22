package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.*;
import com.msy.mygame.client.view.EndFrame;
import com.msy.mygame.client.view.GameFrame;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPNE;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 正式游戏的容器
 * 1.加载背景
 * 2.加载音乐(未完成)
 * 3.加载人物动态效果，障碍物和怪兽的自动生成
 * 4.游戏逻辑：地图自动移动，碰撞检测，游戏暂停与继续逻辑，游戏结束标志
 */
public class GamePanel extends JPanel implements KeyListener {
    Person person = Person.getPerson();//获取人物对象
//    OtherPerson otherPerson = OtherPerson.getOtherPerson();
    public static boolean isOver = false;
    public static boolean flag = true;//游戏继续标记
    Image gameBackground;//背景图片
    Image score;//分数栏
    Image pause;//暂停
    Image proceed;//继续
    Image hp;

    Queue<Monster_1> monster_1s = new LinkedList<>();//小螃蟹
    Queue<Missile> missiles = new LinkedList<>();//导弹
    Queue<Obstacle_1> obstacle_1s = new LinkedList<>();//鱼叉
    Queue<Gold> golds = new LinkedList<>();//金币


//    private static final GamePanel gamePanel = new GamePanel();
//    public static GamePanel getGamePanel() {return gamePanel;}
    public GamePanel() {

        //加载图片文件
        try {
            gameBackground = ImageIO.read(new File("Image/cc.png"));//背景图片
            score =ImageIO.read(new File("Image/a12.png"));//分数栏
//            pause = ImageIO.read(new File("Image/b2.png"));//暂停图片
//            proceed = ImageIO.read(new File("Image/b1.png"));//继续图片
            hp = ImageIO.read(new File("Image/a12.png"));//继续图片
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int x = 0;//背景初始位置
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        //super.paint(g);
        //背景图片动态滚动
        if(flag){
            x-=12;//图片滚动的速度
        }
        //2.3绘制背景图片(动态切换很流畅)
        g.drawImage(gameBackground, x, 0, GameFrame.WIDTH, GameFrame.HEIGHT, null);
        g.drawImage(gameBackground, x+GameFrame.WIDTH, 0, GameFrame.WIDTH, GameFrame.HEIGHT,null);
        if(x<=-GameFrame.WIDTH){//实现两张图片之间的切换
            x = 0;
        }
        System.out.println("背景滚动绘制");

        //绘制玩家
        person.paintElement(g);
        System.out.println("玩家绘制");
//        otherPerson.paintElement(g);
        synchronized (Room.others) {//防止画的时候，ReceivePlayerInfoFromServer改变位置
            for (OtherPerson otherN:
                 Room.others) {
                if (!(otherN.getId().equals(Person.getPerson().getShadowOther().getId()))) {
                    otherN.paintElement(g);
                }
            }
        }
        System.out.println("其他玩家绘制");
        //绘制小螃蟹
        for (Monster_1 monster_1:
             monster_1s) {
            monster_1.paintElement(g);
        }
        System.out.println("螃蟹绘制");
        //绘制每一个鱼叉
        for (Obstacle_1 obstacle_1:
                obstacle_1s) {
            obstacle_1.paintElement(g);
        }
        System.out.println("鱼叉绘制");
        //绘制每一个导弹
        for (Missile missile:
                missiles) {
            missile.paintElement(g);
        }
        System.out.println("导弹绘制");
        //绘制每一个金币
        for (Gold gold:
                golds) {
            gold.paintElement(g);
        }
        System.out.println("金币绘制");

        //绘制玩家分数
        g.drawImage(score, 400, 50 ,null);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("宋体", Font.BOLD, 30));
        g.drawString("玩家得分： " + person.getScore() + " 分", 400, 95);
        System.out.println("得分绘制");

        //绘制玩家血量
        g.drawImage(hp, 120, 50 ,null);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("宋体", Font.BOLD, 30));
        g.drawString("血量： " + person.getHp(), 133, 95);
        //绘制暂停、继续
        if (flag) {
            g.drawImage(proceed, 200,800,90,90, null);
            System.out.println("游戏继续");
        } else {
            g.drawImage(pause, 200,800,90,90,null);
            System.out.println("游戏暂停");
        }

    }

    private int index = 0;
    private Random random = new Random();
    private String[] types = {"Missile", "Monster_1", "Obstacle_1"};
    //每个一段时间产生各种障碍物
    public void obstacleGenerator() {;
        index++;
        String type = types[random.nextInt(3)];
        if (index % 20 == 0) {
            if (type.equals("Obstacle_1")) {
                obstacle_1s.add(new Obstacle_1());
            }
            if (type.equals("Monster_1")) {
                monster_1s.add(new Monster_1());
            }
            if (type.equals("Missile")) {
                missiles.add(new Missile(random.nextInt(300) + 200));
            }
        }

        if (index % 10 == 0) {
            golds.add(new Gold(random.nextInt(500)));
        }
        System.out.println("产生金币");
    }

    //所有元素都在移动
    public void stepAction() {
        //人物移动

        person.step();//切换玩家图片
//        otherPerson.step();
        // 切换其他玩家图片
        for (OtherPerson otherN:
                Room.others) {
            otherN.step();
        }
        System.out.println("人物移动");
        if (person.isOutOfBounds()) {
            isOver = true;
            System.out.println("人物死亡");
        }


        //金币移动,判断是否超出屏幕
        Gold tempGold = null;
        for (Gold gold:
             golds) {
            gold.step();
            System.out.println("金币移动");
            if (gold.isOutOfBounds()) {
                tempGold = gold;
                System.out.println("金币死亡");
            }
        }
        golds.remove(tempGold);
        //导弹移动，判断是否超出屏幕
        Missile tempMissile = null;
        for (Missile missile:
             missiles) {
            missile.step();
            System.out.println("导弹移动");
            if (missile.isOutOfBounds()) {
                tempMissile = missile;
                System.out.println("导弹死亡");
            }
        }
        missiles.remove(tempMissile);
        //螃蟹移动，判断是否超出屏幕
        Monster_1 tempMonster_1 = null;
        for (Monster_1 monster_1:
             monster_1s) {
            monster_1.step();
            System.out.println("螃蟹移动");
            if (monster_1.isOutOfBounds()) {
                tempMonster_1 = monster_1;
                System.out.println("螃蟹死亡");
            }
        }
        monster_1s.remove(tempMonster_1);
        //鱼叉移动，判断是否超出屏幕
        Obstacle_1 tempObstacle_1 = null;
        for (Obstacle_1 obstacle_1:
             obstacle_1s) {
            obstacle_1.step();
            System.out.println("鱼叉移动");
            if (obstacle_1.isOutOfBounds()) {
               tempObstacle_1 = obstacle_1;
                System.out.println("鱼叉死亡");
            }
        }
        obstacle_1s.remove(tempObstacle_1);
    }

    //判断人物与各种障碍物是否碰撞
    public boolean isCollidingWithObstacle(Obstacle obstacle) {
        Rectangle runnerRect = new Rectangle(person.getX(), person.getY(),person.getWIDTH(),person.getHEIGHT());
        Rectangle obstacleRect = new Rectangle(obstacle.getX(), obstacle.getY(), obstacle.getWidth(),obstacle.getHeight());
        return runnerRect.intersects(obstacleRect);
    }

    //处理碰撞的后果
    public void collisionDifference() {
        //和螃蟹碰撞，减去生命值，螃蟹消失
        Monster_1 tempMonster_1 = null;
        for (Monster_1 monster_1:
             monster_1s) {
            if (isCollidingWithObstacle(monster_1)) {
                person.setHp(person.getHp() - 1);
                tempMonster_1 = monster_1;

                System.out.println("人物撞上螃蟹，螃蟹消失");
            }
        }
        monster_1s.remove(tempMonster_1);
        //和导弹碰撞，减去生命值,导弹消失
        Missile tempMissile = null;
        for (Missile missile:
             missiles) {
            if (isCollidingWithObstacle(missile)) {
                person.setHp(person.getHp() - 1);
                tempMissile = missile;
                System.out.println("人物撞上导弹，导弹消失");
            }
        }
        missiles.remove(tempMissile);
        //和鱼叉碰撞，被推着走
        for (Obstacle_1 obstacle_1:
             obstacle_1s) {
            if (isCollidingWithObstacle(obstacle_1)) {
                person.setX(person.getX() - 20);
                System.out.println("人物撞上鱼叉，被推着走");
            }
            if (person.isOutOfBounds()) {
                isOver = true;
            }
        }
        //和金币碰撞，金币消失，玩家金币数增加
        Gold tempGold = null;
        for (Gold gold:
             golds) {
            if (isCollidingWithObstacle(gold)) {
                person.setGold(person.getGold() + 20);
                tempGold = gold;
                System.out.println("人物撞上金币，金币消失");
            }
        }
        golds.remove(tempGold);
    }

    //游戏结束方法
    public void gameOver() {
        if (person.isOutOfBounds() || person.getHp() == 0) {
            isOver = true;
            //将玩家的数据传递给结算界面
            new EndFrame(person);
            System.out.println("正式游戏结束，结束界面启动");
            //数据要清空吗？
        }
    }


    //写一个启动方法，创建一个线程专门实时绘制游戏
    public void action() {
        new Thread(() -> {
            System.out.println("正式游戏界面的绘图线程启动");
            while (!isOver) {
                if (flag) {
                    //执行一系列游戏逻辑，其中包含了游戏元素位置变化的代码
                    obstacleGenerator();
                    System.out.println("障碍物生成成功");
                    stepAction();
                    System.out.println("所有游戏元素移动执行成功");
                    collisionDifference();
                    System.out.println("碰撞处理成功");
                    gameOver();
                    System.out.println("判断游戏结束");

                    //重新绘制，自动调用paintComponent方法
                    repaint();
                    System.out.println("重新绘图");

                    try {
                        Thread.sleep(5);
                        System.out.println("绘图线程休息0.05秒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
    键盘监听器
     */
    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
//        int x = Person.getPerson().getX();
//        int y = Person.getPerson().getY();
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (person.getY() <= 0) {
                    person.setY(person.getY());
                } else {
                    person.setY(person.getY() - 100);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (person.getY() >= 380) {
                    person.setY(person.getY());
                } else {
                    person.setY(person.getY() + 100);
                }
                break;
            case KeyEvent.VK_RIGHT:
                person.rightStep();
                break;
            case KeyEvent.VK_LEFT:
                person.leftStep();
                break;
//            case KeyEvent.VK_SPACE:
//                flag = !flag;
//                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
