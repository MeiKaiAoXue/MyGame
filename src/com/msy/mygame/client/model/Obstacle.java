package com.msy.mygame.client.model;

import java.awt.*;

/**
 * 障碍物父类
 */
public class Obstacle {
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int width;
    private int height;
    private int x;
    private int y;

    public Obstacle() {
        x = 1000;
    }

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

    //障碍物移动速度
    public void step() {
        this.x -= 20;
    }

    //判断移出屏幕
    public boolean isOutOfBounds() {
        if (x == 0) {
            return true;
        }
        return false;
    }

}
