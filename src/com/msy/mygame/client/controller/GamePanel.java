package com.msy.mygame.client.controller;

import com.msy.mygame.client.model.*;
import com.msy.mygame.client.view.EndFrame;
import com.msy.mygame.client.view.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
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
    public static boolean isOver = false;
    public static boolean flag = true;//游戏继续标记
    Image gameBackground;//背景图片
    Image score;//分数栏
    Image pause;//暂停
    Image proceed;//继续

    Queue<Monster_1> monster_1s = new LinkedList<>();//小螃蟹
    Queue<Missile> missiles = new LinkedList<>();//导弹
    Queue<Obstacle_1> obstacle_1s = new LinkedList<>();//鱼叉
    Queue<Gold> golds = new LinkedList<>();//金币


    public GamePanel() {
        //加载图片文件
        try {
            gameBackground = ImageIO.read(new File("Image/cc.png"));//背景图片
            score =ImageIO.read(new File("Image/a12.png"));//分数栏
            pause = ImageIO.read(new File("Image/b2.png"));//暂停图片
            proceed = ImageIO.read(new File("Image/b1.png"));//继续图片
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0;//背景初始位置

        //super.paint(g);
        //背景图片动态滚动
        if (flag) {
            x -= 20;
        }
        g.drawImage(gameBackground, x, 0, GameFrame.WIDTH, GameFrame.HEIGHT,null);//前一张
        g.drawImage(gameBackground, x + GameFrame.WIDTH, 0,GameFrame.WIDTH, GameFrame.HEIGHT, null);//后一张
        if (x <= -GameFrame.WIDTH) {
            x = 0;
        }

        //绘制玩家
        person.painElement(g);
        //绘制小螃蟹
        for (Monster_1 monster_1:
             monster_1s) {
            monster_1.painElement(g);
        }
        //绘制每一个鱼叉
        for (Obstacle_1 obstacle_1:
                obstacle_1s) {
            obstacle_1.painElement(g);
        }
        //绘制每一个导弹
        for (Missile missile:
                missiles) {
            missile.painElement(g);
        }
        //绘制每一个金币
        for (Gold gold:
                golds) {
            gold.painElement(g);
        }

        //绘制玩家分数
        g.drawImage(score, 120, 50 ,null);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("宋体", Font.BOLD, 30));
        g.drawString("玩家得分： " + Person.getPerson().getScore() + " 分", 133, 95);

        //绘制暂停、继续
        if (flag) {
            g.drawImage(proceed, 200,800,90,90, null);
        } else {
            g.drawImage(pause, 200,800,90,90,null);
        }

    }

    //每个一段时间产生各种障碍物
    public void obstacleGenerator() {
        ScheduledExecutorService obstacleExecutor = Executors.newScheduledThreadPool(4);
        Random random = new Random();
        //随机产生金币
        int goldY = random.nextInt(80 + 1) + 500;
        Runnable createGold = () -> {
            golds.add(new Gold(goldY));
        };
        obstacleExecutor.scheduleAtFixedRate(createGold, 0, 1, TimeUnit.SECONDS);
        //随机产生导弹
        int missileY = random.nextInt(100 + 1) + 500;
        Runnable createMissile = () -> {
            missiles.add(new Missile(missileY));
        };
        obstacleExecutor.scheduleAtFixedRate(createMissile, 0, 3, TimeUnit.SECONDS);
        //随机产生鱼叉
        Runnable createObstacle_1 = () -> {
            obstacle_1s.add(new Obstacle_1());
        };
        obstacleExecutor.scheduleAtFixedRate(createObstacle_1, 0, 2, TimeUnit.SECONDS);
        //随机产生螃蟹
        Runnable createMonster_1 = () -> {
            monster_1s.add(new Monster_1());
        };
        obstacleExecutor.scheduleAtFixedRate(createMonster_1, 0, 1, TimeUnit.SECONDS);

        //游戏结束时关闭线程池
        while (!isOver) {
            obstacleExecutor.shutdown();
        }

    }

    //所有元素都在移动
    public void stepAction() {
        //人物移动

        person.step();
        if (person.isOutOfBounds()) {
            isOver = true;
        }

        //金币移动,判断是否超出屏幕
        for (Gold gold:
             golds) {
            gold.step();
            if (gold.isOutOfBounds()) {
                golds.remove(gold);
            }
        }

        //导弹移动，判断是否超出屏幕
        for (Missile missile:
             missiles) {
            missile.step();
            if (missile.isOutOfBounds()) {
                missiles.remove(missile);
            }
        }

        //螃蟹移动，判断是否超出屏幕
        for (Monster_1 monster_1:
             monster_1s) {
            monster_1.step();
            if (monster_1.isOutOfBounds()) {
                monster_1s.remove(monster_1);
            }
        }

        //鱼叉移动，判断是否超出屏幕
        for (Obstacle_1 obstacle_1:
             obstacle_1s) {
            obstacle_1.step();
            if (obstacle_1.isOutOfBounds()) {
                obstacle_1s.remove(obstacle_1);
            }
        }

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
        for (Monster_1 monster_1:
             monster_1s) {
            if (isCollidingWithObstacle(monster_1)) {
                person.setHp(person.getHp() - 1);
                monster_1s.remove(monster_1);
            }
        }
        //和导弹碰撞，减去生命值,导弹消失
        for (Missile missile:
             missiles) {
            if (isCollidingWithObstacle(missile)) {
                person.setHp(person.getHp() - 1);
                missiles.remove(missile);
            }
        }
        //和鱼叉碰撞，被推着走
        for (Obstacle_1 obstacle_1:
             obstacle_1s) {
            if (isCollidingWithObstacle(obstacle_1)) {
                person.setX(person.getX() - 20);
            }
        }
        //和金币碰撞，金币消失，玩家金币数增加
        for (Gold gold:
             golds) {
            if (isCollidingWithObstacle(gold)) {
                person.setGold(person.getGold() + 10);
                golds.remove(gold);
            }
        }
    }

    //游戏结束方法
    public void gameOver() {
        if (person.isOutOfBounds()) {
            isOver = true;
            //将玩家的数据传递给结算界面
            new EndFrame(person);
            //数据要清空吗？
        }
    }

    //写一个启动方法，创建一个线程专门实时绘制游戏通向
    public void action() {
        new Thread(() -> {
            while (!isOver) {
                if (flag) {
                    //执行一系列游戏逻辑，其中包含了游戏元素位置变化的代码
                    stepAction();
                    collisionDifference();
                    gameOver();

                    //重新绘制，自动调用paintComponent方法
                    repaint();

                    try {
                        Thread.sleep(60);
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
                person.jump();
                break;
            case KeyEvent.VK_DOWN:
                person.slide();
                break;
            case KeyEvent.VK_RIGHT:
                person.rightStep();
                break;
            case KeyEvent.VK_LEFT:
                person.leftStep();
                break;
            case KeyEvent.VK_SPACE:
                flag = !flag;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
